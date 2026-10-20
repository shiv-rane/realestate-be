package com.realestate.app.service.impl;

import com.realestate.app.dto.developer.DeveloperProfileResponse;
import com.realestate.app.entity.DeveloperProfile;
import com.realestate.app.entity.enums.VerificationStatus;
import com.realestate.app.exception.EntityNotFoundException;
import com.realestate.app.mapper.DeveloperProfileMapper;
import com.realestate.app.repository.DeveloperProfileRepository;
import com.realestate.app.service.DeveloperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Public read-only implementation of {@link DeveloperService}.
 * No authentication required — exposes only APPROVED developers.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DeveloperServiceImpl implements DeveloperService {

    private final DeveloperProfileRepository developerProfileRepository;
    private final DeveloperProfileMapper developerProfileMapper;

    @Override
    @Transactional(readOnly = true)
    public List<DeveloperProfileResponse> listApprovedDevelopers() {
        List<DeveloperProfile> profiles =
            developerProfileRepository.findAllByVerificationStatus(VerificationStatus.APPROVED);
        log.info("Public developer listing — {} approved developers", profiles.size());
        return profiles.stream().map(developerProfileMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DeveloperProfileResponse getDeveloper(UUID profileId) {
        DeveloperProfile profile = developerProfileRepository.findById(profileId)
            .orElseThrow(() -> new EntityNotFoundException("DeveloperProfile", profileId));
        return developerProfileMapper.toResponse(profile);
    }
}
