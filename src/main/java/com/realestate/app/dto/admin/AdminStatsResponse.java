package com.realestate.app.dto.admin;

/**
 * Summary statistics returned by GET /api/v1/admin/stats
 */
public record AdminStatsResponse(
    long totalDevelopers,
    long pendingVerification,
    long approvedDevelopers,
    long rejectedDevelopers
) {}
