package com.realestate.app.dto.auth;

import com.realestate.app.entity.enums.UserRole;
import jakarta.validation.constraints.*;

/**
 * Request DTO for POST /api/v1/auth/send-otp
 * Allowed roles: OWNER, AGENT, ADMIN.
 * DEVELOPER accounts are admin-managed and cannot self-register.
 */
public record SendOtpRequest(

    /**
     * Full name of the user.
     * Required on first registration; ignored on subsequent OTP requests.
     */
    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    String name,

    @NotBlank(message = "Email address is required")
    @Email(message = "Email must be a valid address (e.g. user@example.com)")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    String email,

    /**
     * Allowed values: OWNER, AGENT, ADMIN.
     * DEVELOPER is not permitted — developers are curated manually by admins.
     */
    @NotNull(message = "Role is required (OWNER, AGENT, or ADMIN)")
    UserRole role

) {}
