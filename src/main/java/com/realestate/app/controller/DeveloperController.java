package com.realestate.app.controller;

import com.realestate.app.dto.developer.DeveloperProfileResponse;
import com.realestate.app.service.DeveloperService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Public Developer Controller — no authentication required.
 * Exposes approved developer profiles and their project portfolios to the public.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/developers")
@Tag(name = "Developers (Public)", description = "Browse approved developers and their projects — no auth required")
public class DeveloperController {

    private final DeveloperService developerService;

    public DeveloperController(DeveloperService developerService) {
        this.developerService = developerService;
    }

    // -------------------------------------------------------
    // GET /api/v1/developers
    // -------------------------------------------------------

    @GetMapping
    @Operation(
        summary = "List Approved Developers",
        description = "Returns all APPROVED developer profiles along with their project portfolios."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Developer list returned",
            content = @Content(schema = @Schema(implementation = DeveloperProfileResponse.class)))
    })
    public ResponseEntity<List<DeveloperProfileResponse>> listDevelopers() {
        return ResponseEntity.ok(developerService.listApprovedDevelopers());
    }

    // -------------------------------------------------------
    // GET /api/v1/developers/{profileId}
    // -------------------------------------------------------

    @GetMapping("/{profileId}")
    @Operation(
        summary = "Get Developer Profile",
        description = "Returns the full profile of a specific developer including all their projects."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Developer profile found",
            content = @Content(schema = @Schema(implementation = DeveloperProfileResponse.class))),
        @ApiResponse(responseCode = "404", description = "Developer not found", content = @Content)
    })
    public ResponseEntity<DeveloperProfileResponse> getDeveloper(@PathVariable UUID profileId) {
        return ResponseEntity.ok(developerService.getDeveloper(profileId));
    }
}
