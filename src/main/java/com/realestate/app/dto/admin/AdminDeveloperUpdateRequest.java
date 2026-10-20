package com.realestate.app.dto.admin;

import com.realestate.app.entity.enums.VerificationStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for PATCH /api/v1/admin/developers/{profileId}
 * Allows an ADMIN to update verification status, total projects done,
 * and free-text project details for a developer profile.
 */
public record AdminDeveloperUpdateRequest(

    /**
     * New verification status for the developer. Must not be null.
     */
    @NotNull(message = "Verification status is required")
    VerificationStatus verificationStatus,

    /**
     * Total number of completed projects. Must be >= 0.
     */
    @NotNull(message = "totalProjectsDone is required")
    @Min(value = 0, message = "totalProjectsDone must be 0 or greater")
    Integer totalProjectsDone,

    /**
     * Optional free-text description of notable projects.
     */
    @Size(max = 5000, message = "Project details must not exceed 5000 characters")
    String projectDetails

) {}
