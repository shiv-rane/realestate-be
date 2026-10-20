package com.realestate.app.dto.auth;

/**
 * Response DTO for POST /api/v1/auth/send-otp
 */
public record SendOtpResponse(
    String message,
    String email
) {}
