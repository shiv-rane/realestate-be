package com.realestate.app.dto.admin;

import com.realestate.app.entity.enums.DeveloperExpertise;
import com.realestate.app.entity.enums.VerificationStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * Request DTO for POST /api/v1/admin/developers — create a new developer manually.
 */
public record CreateDeveloperRequest(

    @NotBlank(message = "Company name is required")
    @Size(max = 255, message = "Company name must not exceed 255 characters")
    String companyName,

    @Size(max = 255, message = "Contact person name must not exceed 255 characters")
    String contactPersonName,

    @Size(max = 255, message = "Contact email must not exceed 255 characters")
    String contactEmail,

    @Size(max = 20, message = "Contact phone must not exceed 20 characters")
    String contactPhone,

    @Size(max = 500, message = "Website URL must not exceed 500 characters")
    String websiteUrl,

    @NotBlank(message = "RERA registration number is required")
    @Size(max = 50, message = "RERA number must not exceed 50 characters")
    String reraNumber,

    @Size(max = 20, message = "GST number must not exceed 20 characters")
    String gstNumber,

    String description,

    Integer establishedYear,

    /** Number of years the developer has been in the market (e.g. 17). */
    @Min(value = 0, message = "Years of experience must be 0 or greater")
    Integer yearsOfExperience,

    /** Total happy / satisfied customers (e.g. 2500). */
    @Min(value = 0, message = "Happy customers must be 0 or greater")
    Integer happyCustomers,

    @NotEmpty(message = "At least one area of core expertise is required")
    Set<DeveloperExpertise> coreExpertise,

    @NotEmpty(message = "At least one operating city is required")
    Set<String> operatingCities,

    @NotNull(message = "Verification status is required")
    VerificationStatus verificationStatus

) {}
