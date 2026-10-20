package com.realestate.app.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for POST /api/v1/auth/verify-otp
 */
public record VerifyOtpRequest(

    @NotBlank(message = "Email address is required")
    @Email(message = "Email must be a valid address (e.g. user@example.com)")
    String email,

    @NotBlank(message = "OTP is required")
    @Size(min = 6, max = 6, message = "OTP must be exactly 6 digits")
    @Pattern(regexp = "^\\d{6}$", message = "OTP must contain only digits")
    String otp

) {}
