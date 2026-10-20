package com.realestate.app.service;

import com.realestate.app.dto.admin.CreateDeveloperRequest;
import com.realestate.app.dto.admin.AdminStatsResponse;
import com.realestate.app.dto.admin.ProjectRequest;
import com.realestate.app.dto.developer.DeveloperProfileResponse;
import com.realestate.app.dto.developer.ProjectResponse;
import com.realestate.app.entity.enums.VerificationStatus;

import java.util.List;
import java.util.UUID;

/**
 * Service contract for admin operations on the platform.
 */
public interface AdminService {

    // ── Stats ──────────────────────────────────────────────────────────────────

    AdminStatsResponse getStats();

    // ── Developer CRUD ─────────────────────────────────────────────────────────

    /** Creates a new developer profile manually. */
    DeveloperProfileResponse createDeveloper(CreateDeveloperRequest request);

    /** Lists all developers, optionally filtered by verification status. */
    List<DeveloperProfileResponse> listDevelopers(VerificationStatus status);

    /** Updates developer profile info and/or verification status. */
    DeveloperProfileResponse updateDeveloper(UUID profileId, CreateDeveloperRequest request);

    /** Deletes a developer and all their projects. */
    void deleteDeveloper(UUID profileId);

    // ── Project CRUD ───────────────────────────────────────────────────────────

    /** Adds a new project to a developer's portfolio. */
    ProjectResponse addProject(UUID developerId, ProjectRequest request);

    /** Updates an existing project. */
    ProjectResponse updateProject(UUID developerId, UUID projectId, ProjectRequest request);

    /** Removes a project from a developer's portfolio. */
    void deleteProject(UUID developerId, UUID projectId);
}
