package com.realestate.app.service.impl;

import com.realestate.app.dto.land.CreateLandParcelRequest;
import com.realestate.app.dto.land.LandParcelResponse;
import com.realestate.app.dto.land.UpdateLandParcelRequest;
import com.realestate.app.dto.land.UpdateLandStatusRequest;
import com.realestate.app.entity.LandParcel;
import com.realestate.app.entity.User;
import com.realestate.app.entity.enums.LandCategory;
import com.realestate.app.entity.enums.LandStatus;
import com.realestate.app.entity.enums.UserRole;
import com.realestate.app.exception.BusinessRuleException;
import com.realestate.app.exception.EntityNotFoundException;
import com.realestate.app.mapper.LandParcelMapper;
import com.realestate.app.repository.LandParcelRepository;
import com.realestate.app.repository.UserRepository;
import com.realestate.app.service.LandParcelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Implementation of {@link LandParcelService}.
 */
@Service
@Slf4j
public class LandParcelServiceImpl implements LandParcelService {

    private final LandParcelRepository landParcelRepository;
    private final UserRepository userRepository;
    private final LandParcelMapper landParcelMapper;

    public LandParcelServiceImpl(
            LandParcelRepository landParcelRepository,
            UserRepository userRepository,
            LandParcelMapper landParcelMapper) {
        this.landParcelRepository = landParcelRepository;
        this.userRepository = userRepository;
        this.landParcelMapper = landParcelMapper;
    }

    @Override
    @Transactional
    public LandParcelResponse createLandParcel(UUID userId, CreateLandParcelRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User", userId));

        if (user.getRole() == UserRole.DEVELOPER) {
            throw new BusinessRuleException("DEVELOPER accounts cannot submit land parcels.");
        }

        LandParcel parcel = landParcelMapper.toEntity(request, user);
        LandParcel saved = landParcelRepository.save(parcel);
        log.info("Land parcel submitted by user [{}] with id [{}]", userId, saved.getId());

        return landParcelMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LandParcelResponse> getMyLandParcels(UUID userId) {
        return landParcelRepository.findAllBySubmittedByIdOrderByCreatedAtDesc(userId)
            .stream()
            .map(landParcelMapper::toResponse)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public LandParcelResponse getLandParcelById(UUID parcelId, UUID currentUserId, boolean isAdmin) {
        LandParcel parcel = landParcelRepository.findById(parcelId)
            .orElseThrow(() -> new EntityNotFoundException("LandParcel", parcelId));

        // Anyone can view if it's VERIFIED, or if current user is owner or admin
        boolean isCreator = parcel.getSubmittedBy() != null && parcel.getSubmittedBy().getId().equals(currentUserId);
        if (!isAdmin && !isCreator && parcel.getStatus() != LandStatus.VERIFIED) {
            throw new BusinessRuleException("You do not have permission to view this pending land parcel.");
        }

        return landParcelMapper.toResponse(parcel);
    }

    @Override
    @Transactional
    public LandParcelResponse updateLandParcel(
            UUID parcelId, UUID currentUserId, boolean isAdmin, UpdateLandParcelRequest request) {
        LandParcel parcel = landParcelRepository.findById(parcelId)
            .orElseThrow(() -> new EntityNotFoundException("LandParcel", parcelId));

        boolean isCreator = parcel.getSubmittedBy() != null && parcel.getSubmittedBy().getId().equals(currentUserId);
        if (!isAdmin && !isCreator) {
            throw new BusinessRuleException("You do not have permission to modify this land parcel.");
        }

        landParcelMapper.updateEntityFromRequest(parcel, request);
        LandParcel updated = landParcelRepository.save(parcel);
        log.info("Land parcel [{}] updated by user [{}]", parcelId, currentUserId);

        return landParcelMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteLandParcel(UUID parcelId, UUID currentUserId, boolean isAdmin) {
        LandParcel parcel = landParcelRepository.findById(parcelId)
            .orElseThrow(() -> new EntityNotFoundException("LandParcel", parcelId));

        boolean isCreator = parcel.getSubmittedBy() != null && parcel.getSubmittedBy().getId().equals(currentUserId);
        if (!isAdmin && !isCreator) {
            throw new BusinessRuleException("You do not have permission to delete this land parcel.");
        }

        landParcelRepository.delete(parcel);
        log.info("Land parcel [{}] deleted by user [{}]", parcelId, currentUserId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LandParcelResponse> listPublicVerifiedParcels(String city, LandCategory zoning) {
        List<LandParcel> parcels;
        if (city != null && !city.isBlank()) {
            parcels = landParcelRepository.findAllByStatusAndCityIgnoreCaseOrderByCreatedAtDesc(
                LandStatus.VERIFIED, city.trim());
        } else if (zoning != null) {
            parcels = landParcelRepository.findAllByStatusAndZoningOrderByCreatedAtDesc(
                LandStatus.VERIFIED, zoning);
        } else {
            parcels = landParcelRepository.findAllByStatusOrderByCreatedAtDesc(LandStatus.VERIFIED);
        }
        return parcels.stream().map(landParcelMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LandParcelResponse> listAllParcelsForAdmin(LandStatus status) {
        List<LandParcel> parcels = (status != null)
            ? landParcelRepository.findAllByStatusOrderByCreatedAtDesc(status)
            : landParcelRepository.findByOrderByCreatedAtDesc();

        return parcels.stream().map(landParcelMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public LandParcelResponse updateParcelStatusByAdmin(UUID parcelId, UpdateLandStatusRequest request) {
        LandParcel parcel = landParcelRepository.findById(parcelId)
            .orElseThrow(() -> new EntityNotFoundException("LandParcel", parcelId));

        parcel.setStatus(request.status());
        if (request.adminNotes() != null) {
            parcel.setAdminNotes(request.adminNotes());
        }

        LandParcel updated = landParcelRepository.save(parcel);
        log.info("Land parcel [{}] status changed to [{}] by admin", parcelId, request.status());

        return landParcelMapper.toResponse(updated);
    }
}
