package com.realestate.app.dto.developer;

import com.realestate.app.entity.enums.DeveloperExpertise;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * Request DTO for POST /api/v1/developers/register
 * Requires a valid JWT — userId is extracted from the security context, not this DTO.
 */
public record DeveloperRegistrationRequest(

    @NotBlank(message = "Company name is required")
    @Size(max = 255, message = "Company name must not exceed 255 characters")
    String companyName,

    @NotBlank(message = "RERA registration number is required")
    @Size(max = 50, message = "RERA number must not exceed 50 characters")
    String reraNumber,

    @Pattern(
        regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$",
        message = "GST number must be in a valid format (e.g., 22AAAAA0000A1Z5)"
    )
    String gstNumber,

    @NotEmpty(message = "At least one area of core expertise is required")
    Set<DeveloperExpertise> coreExpertise,

    @NotEmpty(message = "At least one operating city is required")
    Set<String> operatingCities

) {}
