package com.realestate.app.service;

import com.realestate.app.dto.developer.DeveloperProfileResponse;

import java.util.List;
import java.util.UUID;

/**
 * Public-facing service for browsing approved developer profiles.
 * No authentication required.
 */
public interface DeveloperService {

    /**
     * Returns all developers with APPROVED verification status.
     */
    List<DeveloperProfileResponse> listApprovedDevelopers();

    /**
     * Returns full profile (with projects) for a specific developer.
     *
     * @param profileId the developer profile UUID
     */
    DeveloperProfileResponse getDeveloper(UUID profileId);
}
