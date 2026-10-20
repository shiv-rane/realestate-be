package com.realestate.app.service;

import com.realestate.app.dto.land.CreateLandParcelRequest;
import com.realestate.app.dto.land.LandParcelResponse;
import com.realestate.app.dto.land.UpdateLandParcelRequest;
import com.realestate.app.dto.land.UpdateLandStatusRequest;
import com.realestate.app.entity.enums.LandCategory;
import com.realestate.app.entity.enums.LandStatus;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing land parcels submitted by landowners and agents.
 */
public interface LandParcelService {

    LandParcelResponse createLandParcel(UUID userId, CreateLandParcelRequest request);

    List<LandParcelResponse> getMyLandParcels(UUID userId);

    LandParcelResponse getLandParcelById(UUID parcelId, UUID currentUserId, boolean isAdmin);

    LandParcelResponse updateLandParcel(UUID parcelId, UUID currentUserId, boolean isAdmin, UpdateLandParcelRequest request);

    void deleteLandParcel(UUID parcelId, UUID currentUserId, boolean isAdmin);

    List<LandParcelResponse> listPublicVerifiedParcels(String city, LandCategory zoning);

    List<LandParcelResponse> listAllParcelsForAdmin(LandStatus status);

    LandParcelResponse updateParcelStatusByAdmin(UUID parcelId, UpdateLandStatusRequest request);
}
