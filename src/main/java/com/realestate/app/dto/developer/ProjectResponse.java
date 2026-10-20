package com.realestate.app.dto.developer;

import com.realestate.app.entity.enums.ProjectStatus;
import com.realestate.app.entity.enums.ProjectType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for a single project belonging to a developer.
 */
public record ProjectResponse(
    UUID projectId,
    String name,
    String description,
    ProjectType projectType,
    ProjectStatus status,
    String city,
    String address,
    String pincode,
    Integer areaSqFtMin,
    Integer areaSqFtMax,
    Integer totalUnits,
    String reraProjectNumber,
    Integer deliveredYear,
    List<String> amenities,
    Instant createdAt
) {}
