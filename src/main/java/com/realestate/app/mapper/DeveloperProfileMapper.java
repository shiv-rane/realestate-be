package com.realestate.app.mapper;

import com.realestate.app.dto.developer.DeveloperProfileResponse;
import com.realestate.app.dto.developer.ProjectResponse;
import com.realestate.app.entity.DeveloperProfile;
import com.realestate.app.entity.Project;
import com.realestate.app.entity.enums.ProjectStatus;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Manual mapper for converting {@link DeveloperProfile} and {@link Project} entities to DTOs.
 * Replaced MapStruct to avoid re-generating after the entity redesign.
 */
@Component
public class DeveloperProfileMapper {

    public ProjectResponse toProjectResponse(Project project) {
        return new ProjectResponse(
            project.getId(),
            project.getName(),
            project.getDescription(),
            project.getProjectType(),
            project.getStatus(),
            project.getCity(),
            project.getAddress(),
            project.getPincode(),
            project.getAreaSqFtMin(),
            project.getAreaSqFtMax(),
            project.getTotalUnits(),
            project.getReraProjectNumber(),
            project.getDeliveredYear(),
            project.getAmenities() != null ? project.getAmenities() : Collections.emptyList(),
            project.getCreatedAt()
        );
    }

    public DeveloperProfileResponse toResponse(DeveloperProfile dev) {
        List<ProjectResponse> projectResponses = dev.getProjects() == null
            ? Collections.emptyList()
            : dev.getProjects().stream().map(this::toProjectResponse).toList();

        int projectsCompleted = (int) projectResponses.stream()
            .filter(p -> p.status() == ProjectStatus.DELIVERED)
            .count();

        int upcomingProjects = (int) projectResponses.stream()
            .filter(p -> p.status() == ProjectStatus.UPCOMING)
            .count();

        return new DeveloperProfileResponse(
            dev.getId(),
            dev.getCompanyName(),
            dev.getContactPersonName(),
            dev.getContactEmail(),
            dev.getContactPhone(),
            dev.getWebsiteUrl(),
            dev.getReraNumber(),
            dev.getGstNumber(),
            dev.getDescription(),
            dev.getEstablishedYear(),
            dev.getCoreExpertise(),
            dev.getOperatingCities(),
            dev.getVerificationStatus(),
            dev.getYearsOfExperience() != null ? dev.getYearsOfExperience() : 0,
            dev.getHappyCustomers() != null ? dev.getHappyCustomers() : 0,
            projectsCompleted,
            upcomingProjects,
            projectResponses,
            dev.getCreatedAt()
        );
    }
}
