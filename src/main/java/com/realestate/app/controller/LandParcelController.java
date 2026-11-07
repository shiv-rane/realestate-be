package com.realestate.app.controller;

import com.realestate.app.dto.land.CreateLandParcelRequest;
import com.realestate.app.dto.land.LandParcelResponse;
import com.realestate.app.dto.land.UpdateLandParcelRequest;
import com.realestate.app.dto.land.UpdateLandStatusRequest;
import com.realestate.app.entity.enums.LandCategory;
import com.realestate.app.entity.enums.LandStatus;
import com.realestate.app.service.LandParcelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller for Landowners (OWNER) and Agents (AGENT) to submit and manage land parcels.
 * Also provides endpoints to browse verified parcels and for admins to verify submissions.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/lands")
@Tag(name = "Land Parcels", description = "Submit and manage land information (Owner, Agent & Admin)")
@SecurityRequirement(name = "bearerAuth")
public class LandParcelController {

    private final LandParcelService landParcelService;

    public LandParcelController(LandParcelService landParcelService) {
        this.landParcelService = landParcelService;
    }

    // -------------------------------------------------------
    // POST /api/v1/lands (Submit Land Info)
    // -------------------------------------------------------

    @PostMapping
    @PreAuthorize("hasAnyRole('OWNER', 'AGENT', 'ADMIN')")
    @Operation(
        summary = "Submit Land Information",
        description = "Allows a logged-in Landowner or Agent to submit a new land parcel with location, dimension, and commercial details."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Land parcel submitted successfully",
            content = @Content(schema = @Schema(implementation = LandParcelResponse.class))),
        @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public ResponseEntity<LandParcelResponse> submitLand(
            @Valid @RequestBody CreateLandParcelRequest request,
            Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        LandParcelResponse response = landParcelService.createLandParcel(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // -------------------------------------------------------
    // GET /api/v1/lands/my-parcels (View Submitted Lands)
    // -------------------------------------------------------

    @GetMapping("/my-parcels")
    @PreAuthorize("hasAnyRole('OWNER', 'AGENT', 'ADMIN')")
    @Operation(
        summary = "Get My Submitted Land Parcels",
        description = "Returns all land parcels submitted by the currently authenticated user."
    )
    @ApiResponse(responseCode = "200", description = "List of user's submitted parcels",
        content = @Content(schema = @Schema(implementation = LandParcelResponse.class)))
    public ResponseEntity<List<LandParcelResponse>> getMyParcels(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(landParcelService.getMyLandParcels(userId));
    }

    // -------------------------------------------------------
    // GET /api/v1/lands/{id} (Get Parcel Details)
    // -------------------------------------------------------

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(
        summary = "Get Land Parcel by ID",
        description = "Fetches complete details of a land parcel."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Land parcel found",
            content = @Content(schema = @Schema(implementation = LandParcelResponse.class))),
        @ApiResponse(responseCode = "404", description = "Land parcel not found", content = @Content)
    })
    public ResponseEntity<LandParcelResponse> getParcelById(
            @PathVariable UUID id,
            Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        boolean isAdmin = authentication.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        return ResponseEntity.ok(landParcelService.getLandParcelById(id, userId, isAdmin));
    }

    // -------------------------------------------------------
    // PUT /api/v1/lands/{id} (Update Land Info)
    // -------------------------------------------------------

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'AGENT', 'ADMIN')")
    @Operation(
        summary = "Update Land Parcel",
        description = "Updates land parcel details. Only the submitter or an admin can update."
    )
    public ResponseEntity<LandParcelResponse> updateParcel(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateLandParcelRequest request,
            Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        boolean isAdmin = authentication.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        return ResponseEntity.ok(landParcelService.updateLandParcel(id, userId, isAdmin, request));
    }

    // -------------------------------------------------------
    // DELETE /api/v1/lands/{id}
    // -------------------------------------------------------

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'AGENT', 'ADMIN')")
    @Operation(
        summary = "Delete Land Parcel",
        description = "Deletes a land parcel. Only the submitter or an admin can delete."
    )
    public ResponseEntity<Void> deleteParcel(
            @PathVariable UUID id,
            Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        boolean isAdmin = authentication.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        landParcelService.deleteLandParcel(id, userId, isAdmin);
        return ResponseEntity.noContent().build();
    }

    // -------------------------------------------------------
    // GET /api/v1/lands/verified (Marketplace Listing)
    // -------------------------------------------------------

    @GetMapping("/verified")
    @PreAuthorize("isAuthenticated()")
    @Operation(
        summary = "Browse Verified Land Parcels",
        description = "Allows developers and users to view verified land opportunities across cities and zoning types."
    )
    public ResponseEntity<List<LandParcelResponse>> listVerifiedParcels(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) LandCategory zoning) {
        return ResponseEntity.ok(landParcelService.listPublicVerifiedParcels(city, zoning));
    }

    // -------------------------------------------------------
    // ADMIN ENDPOINTS
    // -------------------------------------------------------

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Admin: List All Land Parcels",
        description = "Fetches all parcels across the platform with optional status filtering."
    )
    public ResponseEntity<List<LandParcelResponse>> adminListAllParcels(
            @RequestParam(required = false) LandStatus status) {
        return ResponseEntity.ok(landParcelService.listAllParcelsForAdmin(status));
    }

    @PatchMapping("/admin/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Admin: Update Land Status",
        description = "Verifies, approves, or rejects a submitted land parcel with admin feedback."
    )
    public ResponseEntity<LandParcelResponse> adminUpdateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateLandStatusRequest request) {
        return ResponseEntity.ok(landParcelService.updateParcelStatusByAdmin(id, request));
    }
}
