package com.realestate.app.dto.auth;

import com.realestate.app.entity.enums.UserRole;

import java.util.UUID;

/**
 * Response DTO for POST /api/v1/auth/verify-otp
 * Contains the JWT token and basic user information.
 */
public record AuthResponse(
    String accessToken,
    String tokenType,
    long expiresInMs,
    UUID userId,
    String name,
    String email,
    UserRole role
) {
    public AuthResponse(String accessToken, long expiresInMs, UUID userId, String name, String email, UserRole role) {
        this(accessToken, "Bearer", expiresInMs, userId, name, email, role);
    }
}
