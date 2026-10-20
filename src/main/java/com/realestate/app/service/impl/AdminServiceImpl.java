package com.realestate.app.service.impl;

import com.realestate.app.dto.admin.AdminStatsResponse;
import com.realestate.app.dto.admin.CreateDeveloperRequest;
import com.realestate.app.dto.admin.ProjectRequest;
import com.realestate.app.dto.developer.DeveloperProfileResponse;
import com.realestate.app.dto.developer.ProjectResponse;
import com.realestate.app.entity.DeveloperProfile;
import com.realestate.app.entity.Project;
import com.realestate.app.entity.enums.VerificationStatus;
import com.realestate.app.exception.BusinessRuleException;
import com.realestate.app.exception.EntityNotFoundException;
import com.realestate.app.mapper.DeveloperProfileMapper;
import com.realestate.app.repository.DeveloperProfileRepository;
import com.realestate.app.repository.ProjectRepository;
import com.realestate.app.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Full admin implementation for managing developers and their projects.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final DeveloperProfileRepository developerProfileRepository;
    private final ProjectRepository projectRepository;
    private final DeveloperProfileMapper developerProfileMapper;

    // =========================================================
    // Stats
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public AdminStatsResponse getStats() {
        long total    = developerProfileRepository.count();
        long pending  = developerProfileRepository.countByVerificationStatus(VerificationStatus.PENDING);
        long approved = developerProfileRepository.countByVerificationStatus(VerificationStatus.APPROVED);
        long rejected = developerProfileRepository.countByVerificationStatus(VerificationStatus.REJECTED);
        log.info("Admin stats — total: {}, pending: {}, approved: {}, rejected: {}", total, pending, approved, rejected);
        return new AdminStatsResponse(total, pending, approved, rejected);
    }

    // =========================================================
    // Developer CRUD
    // =========================================================

    @Override
    @Transactional
    public DeveloperProfileResponse createDeveloper(CreateDeveloperRequest request) {
        if (developerProfileRepository.existsByReraNumber(request.reraNumber())) {
            throw new BusinessRuleException(
                "RERA number '" + request.reraNumber() + "' is already registered.");
        }
        DeveloperProfile profile = DeveloperProfile.builder()
            .companyName(request.companyName())
            .contactPersonName(request.contactPersonName())
            .contactEmail(request.contactEmail())
            .contactPhone(request.contactPhone())
            .websiteUrl(request.websiteUrl())
            .reraNumber(request.reraNumber())
            .gstNumber(request.gstNumber())
            .description(request.description())
            .establishedYear(request.establishedYear())
            .yearsOfExperience(request.yearsOfExperience() != null ? request.yearsOfExperience() : 0)
            .happyCustomers(request.happyCustomers() != null ? request.happyCustomers() : 0)
            .coreExpertise(request.coreExpertise())
            .operatingCities(request.operatingCities())
            .verificationStatus(request.verificationStatus())
            .projects(new ArrayList<>())
            .build();

        DeveloperProfile saved = developerProfileRepository.save(profile);
        log.info("Admin created developer: {} ({})", saved.getCompanyName(), saved.getId());
        return developerProfileMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeveloperProfileResponse> listDevelopers(VerificationStatus status) {
        List<DeveloperProfile> profiles = status != null
            ? developerProfileRepository.findAllByVerificationStatus(status)
            : developerProfileRepository.findAll();
        log.info("Admin listing developers — filter: {}, count: {}", status, profiles.size());
        return profiles.stream().map(developerProfileMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public DeveloperProfileResponse updateDeveloper(UUID profileId, CreateDeveloperRequest request) {
        DeveloperProfile profile = developerProfileRepository.findById(profileId)
            .orElseThrow(() -> new EntityNotFoundException("DeveloperProfile", profileId));

        // Check RERA uniqueness only if it changed
        if (!profile.getReraNumber().equals(request.reraNumber())
                && developerProfileRepository.existsByReraNumber(request.reraNumber())) {
            throw new BusinessRuleException(
                "RERA number '" + request.reraNumber() + "' is already in use by another developer.");
        }

        profile.setCompanyName(request.companyName());
        profile.setContactPersonName(request.contactPersonName());
        profile.setContactEmail(request.contactEmail());
        profile.setContactPhone(request.contactPhone());
        profile.setWebsiteUrl(request.websiteUrl());
        profile.setReraNumber(request.reraNumber());
        profile.setGstNumber(request.gstNumber());
        profile.setDescription(request.description());
        profile.setEstablishedYear(request.establishedYear());
        profile.setYearsOfExperience(request.yearsOfExperience() != null ? request.yearsOfExperience() : 0);
        profile.setHappyCustomers(request.happyCustomers() != null ? request.happyCustomers() : 0);
        profile.setCoreExpertise(request.coreExpertise());
        profile.setOperatingCities(request.operatingCities());
        profile.setVerificationStatus(request.verificationStatus());

        DeveloperProfile saved = developerProfileRepository.save(profile);
        log.info("Admin updated developer: {}", profileId);
        return developerProfileMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteDeveloper(UUID profileId) {
        if (!developerProfileRepository.existsById(profileId)) {
            throw new EntityNotFoundException("DeveloperProfile", profileId);
        }
        developerProfileRepository.deleteById(profileId);
        log.info("Admin deleted developer: {}", profileId);
    }

    // =========================================================
    // Project CRUD
    // =========================================================

    @Override
    @Transactional
    public ProjectResponse addProject(UUID developerId, ProjectRequest request) {
        DeveloperProfile developer = developerProfileRepository.findById(developerId)
            .orElseThrow(() -> new EntityNotFoundException("DeveloperProfile", developerId));

        Project project = Project.builder()
            .developer(developer)
            .name(request.name())
            .description(request.description())
            .projectType(request.projectType())
            .status(request.status())
            .city(request.city())
            .address(request.address())
            .pincode(request.pincode())
            .areaSqFtMin(request.areaSqFtMin())
            .areaSqFtMax(request.areaSqFtMax())
            .totalUnits(request.totalUnits())
            .reraProjectNumber(request.reraProjectNumber())
            .deliveredYear(request.deliveredYear())
            .amenities(request.amenities() != null ? request.amenities() : new ArrayList<>())
            .build();

        Project saved = projectRepository.save(project);
        log.info("Admin added project '{}' to developer {}", saved.getName(), developerId);
        return developerProfileMapper.toProjectResponse(saved);
    }

    @Override
    @Transactional
    public ProjectResponse updateProject(UUID developerId, UUID projectId, ProjectRequest request) {
        Project project = projectRepository.findByIdAndDeveloperId(projectId, developerId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Project " + projectId + " not found under developer " + developerId));

        project.setName(request.name());
        project.setDescription(request.description());
        project.setProjectType(request.projectType());
        project.setStatus(request.status());
        project.setCity(request.city());
        project.setAddress(request.address());
        project.setPincode(request.pincode());
        project.setAreaSqFtMin(request.areaSqFtMin());
        project.setAreaSqFtMax(request.areaSqFtMax());
        project.setTotalUnits(request.totalUnits());
        project.setReraProjectNumber(request.reraProjectNumber());
        project.setDeliveredYear(request.deliveredYear());
        project.setAmenities(request.amenities() != null ? request.amenities() : new ArrayList<>());

        Project saved = projectRepository.save(project);
        log.info("Admin updated project {} under developer {}", projectId, developerId);
        return developerProfileMapper.toProjectResponse(saved);
    }

    @Override
    @Transactional
    public void deleteProject(UUID developerId, UUID projectId) {
        Project project = projectRepository.findByIdAndDeveloperId(projectId, developerId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Project " + projectId + " not found under developer " + developerId));
        projectRepository.delete(project);
        log.info("Admin deleted project {} from developer {}", projectId, developerId);
    }
}
