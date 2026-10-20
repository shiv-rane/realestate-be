package com.realestate.app.controller;

import com.realestate.app.dto.auth.AuthResponse;
import com.realestate.app.dto.auth.SendOtpRequest;
import com.realestate.app.dto.auth.SendOtpResponse;
import com.realestate.app.dto.auth.VerifyOtpRequest;
import com.realestate.app.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication Controller — handles passwordless email OTP login.
 * All endpoints are public (no JWT required).
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Passwordless email OTP authentication endpoints")
public class AuthController {

    private final AuthService authService;

    // -------------------------------------------------------
    // POST /api/v1/auth/send-otp
    // -------------------------------------------------------

    @PostMapping("/send-otp")
    @Operation(
        summary = "Send OTP via Email",
        description = "Sends a 6-digit OTP to the provided email address. " +
                      "Creates a new user account (with name and role) if one does not exist. " +
                      "OTP is valid for 10 minutes. No password required — this is passwordless auth."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OTP sent successfully",
            content = @Content(schema = @Schema(implementation = SendOtpResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid email address or missing required fields",
            content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content)
    })
    public ResponseEntity<SendOtpResponse> sendOtp(@Valid @RequestBody SendOtpRequest request) {
        return ResponseEntity.ok(authService.sendOtp(request));
    }

    // -------------------------------------------------------
    // POST /api/v1/auth/verify-otp
    // -------------------------------------------------------

    @PostMapping("/verify-otp")
    @Operation(
        summary = "Verify OTP & Issue JWT",
        description = "Verifies the OTP for the given email address. " +
                      "On success, marks the email as verified and returns a Bearer JWT token."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "OTP verified. JWT token returned.",
            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request body",
            content = @Content),
        @ApiResponse(responseCode = "401", description = "Invalid or expired OTP",
            content = @Content),
        @ApiResponse(responseCode = "404", description = "Email address not found",
            content = @Content)
    })
    public ResponseEntity<AuthResponse> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        return ResponseEntity.ok(authService.verifyOtp(request));
    }
}
