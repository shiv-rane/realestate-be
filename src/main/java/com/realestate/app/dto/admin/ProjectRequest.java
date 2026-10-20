package com.realestate.app.dto.admin;

import com.realestate.app.entity.enums.ProjectStatus;
import com.realestate.app.entity.enums.ProjectType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Request DTO for POST and PATCH on /api/v1/admin/developers/{devId}/projects
 */
public record ProjectRequest(

    @NotBlank(message = "Project name is required")
    @Size(max = 255, message = "Project name must not exceed 255 characters")
    String name,

    String description,

    @NotNull(message = "Project type is required (RESIDENTIAL, COMMERCIAL, MIXED_USE, PLOTTED)")
    ProjectType projectType,

    @NotNull(message = "Project status is required (UPCOMING, ONGOING, DELIVERED)")
    ProjectStatus status,

    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City must not exceed 100 characters")
    String city,

    @Size(max = 500, message = "Address must not exceed 500 characters")
    String address,

    @Size(max = 10, message = "Pincode must not exceed 10 characters")
    String pincode,

    @Min(value = 0, message = "Minimum area must be 0 or greater")
    Integer areaSqFtMin,

    @Min(value = 0, message = "Maximum area must be 0 or greater")
    Integer areaSqFtMax,

    @Min(value = 1, message = "Total units must be at least 1")
    Integer totalUnits,

    @Size(max = 50, message = "RERA project number must not exceed 50 characters")
    String reraProjectNumber,

    Integer deliveredYear,

    List<String> amenities

) {}
