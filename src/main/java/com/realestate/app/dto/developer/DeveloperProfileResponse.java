package com.realestate.app.dto.developer;

import com.realestate.app.entity.enums.DeveloperExpertise;
import com.realestate.app.entity.enums.VerificationStatus;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Full developer profile response including all projects and headline stats.
 * Returned by both public and admin endpoints.
 */
public record DeveloperProfileResponse(
    UUID profileId,
    String companyName,
    String contactPersonName,
    String contactEmail,
    String contactPhone,
    String websiteUrl,
    String reraNumber,
    String gstNumber,
    String description,
    Integer establishedYear,
    Set<DeveloperExpertise> coreExpertise,
    Set<String> operatingCities,
    VerificationStatus verificationStatus,

    // ── Headline Stats ─────────────────────────────────────────────────────────
    /** Admin-set: years the developer has been in the market (e.g. 17). */
    int yearsOfExperience,
    /** Admin-set: total happy/satisfied customers (e.g. 2500). */
    int happyCustomers,
    /** Computed: count of projects with status = DELIVERED. */
    int projectsCompleted,
    /** Computed: count of projects with status = UPCOMING. */
    int upcomingProjects,

    List<ProjectResponse> projects,
    Instant createdAt
) {}
