package com.realestate.app.mapper;

import com.realestate.app.dto.land.CreateLandParcelRequest;
import com.realestate.app.dto.land.LandParcelResponse;
import com.realestate.app.dto.land.UpdateLandParcelRequest;
import com.realestate.app.entity.LandParcel;
import com.realestate.app.entity.User;
import com.realestate.app.entity.enums.LandStatus;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between {@link LandParcel} entities and their corresponding DTOs.
 */
@Component
public class LandParcelMapper {

    public LandParcel toEntity(CreateLandParcelRequest request, User user) {
        return LandParcel.builder()
            .submittedBy(user)
            .title(request.title())
            .city(request.city())
            .sector(request.sector())
            .state(request.state())
            .pincode(request.pincode())
            .plotSurveyNumber(request.plotSurveyNumber())
            .landmark(request.landmark())
            .locationUrl(request.locationUrl())
            .latitude(request.latitude())
            .longitude(request.longitude())
            .totalArea(request.totalArea())
            .areaUnit(request.areaUnit())
            .roadWidthFeet(request.roadWidthFeet())
            .plotFacing(request.plotFacing())
            .zoning(request.zoning())
            .dealType(request.dealType())
            .expectedPrice(request.expectedPrice())
            .pricePerUnit(request.pricePerUnit())
            .isPriceNegotiable(request.isPriceNegotiable() != null ? request.isPriceNegotiable() : true)
            .expectedRevenueSharePercentage(request.expectedRevenueSharePercentage())
            .ownershipType(request.ownershipType())
            .hasClearTitle(request.hasClearTitle() != null ? request.hasClearTitle() : true)
            .isLitigationFree(request.isLitigationFree() != null ? request.isLitigationFree() : true)
            .agencyName(request.agencyName())
            .brokeragePercentage(request.brokeragePercentage())
            .description(request.description())
            .status(LandStatus.PENDING_REVIEW)
            .build();
    }

    public void updateEntityFromRequest(LandParcel parcel, UpdateLandParcelRequest request) {
        parcel.setTitle(request.title());
        parcel.setCity(request.city());
        parcel.setSector(request.sector());
        parcel.setState(request.state());
        parcel.setPincode(request.pincode());
        parcel.setPlotSurveyNumber(request.plotSurveyNumber());
        parcel.setLandmark(request.landmark());
        parcel.setLocationUrl(request.locationUrl());
        parcel.setLatitude(request.latitude());
        parcel.setLongitude(request.longitude());
        parcel.setTotalArea(request.totalArea());
        parcel.setAreaUnit(request.areaUnit());
        parcel.setRoadWidthFeet(request.roadWidthFeet());
        parcel.setPlotFacing(request.plotFacing());
        parcel.setZoning(request.zoning());
        parcel.setDealType(request.dealType());
        parcel.setExpectedPrice(request.expectedPrice());
        parcel.setPricePerUnit(request.pricePerUnit());
        if (request.isPriceNegotiable() != null) {
            parcel.setIsPriceNegotiable(request.isPriceNegotiable());
        }
        parcel.setExpectedRevenueSharePercentage(request.expectedRevenueSharePercentage());
        parcel.setOwnershipType(request.ownershipType());
        if (request.hasClearTitle() != null) {
            parcel.setHasClearTitle(request.hasClearTitle());
        }
        if (request.isLitigationFree() != null) {
            parcel.setIsLitigationFree(request.isLitigationFree());
        }
        parcel.setAgencyName(request.agencyName());
        parcel.setBrokeragePercentage(request.brokeragePercentage());
        parcel.setDescription(request.description());
    }

    public LandParcelResponse toResponse(LandParcel parcel) {
        User submitter = parcel.getSubmittedBy();
        LandParcelResponse.SubmitterInfo submitterInfo = submitter != null
            ? new LandParcelResponse.SubmitterInfo(
                submitter.getId(),
                submitter.getName(),
                submitter.getEmail(),
                submitter.getRole()
            )
            : null;

        return new LandParcelResponse(
            parcel.getId(),
            submitterInfo,
            parcel.getTitle(),
            parcel.getCity(),
            parcel.getSector(),
            parcel.getState(),
            parcel.getPincode(),
            parcel.getPlotSurveyNumber(),
            parcel.getLandmark(),
            parcel.getLocationUrl(),
            parcel.getLatitude(),
            parcel.getLongitude(),
            parcel.getTotalArea(),
            parcel.getAreaUnit(),
            parcel.getRoadWidthFeet(),
            parcel.getPlotFacing(),
            parcel.getZoning(),
            parcel.getDealType(),
            parcel.getExpectedPrice(),
            parcel.getPricePerUnit(),
            parcel.getIsPriceNegotiable(),
            parcel.getExpectedRevenueSharePercentage(),
            parcel.getOwnershipType(),
            parcel.getHasClearTitle(),
            parcel.getIsLitigationFree(),
            parcel.getAgencyName(),
            parcel.getBrokeragePercentage(),
            parcel.getDescription(),
            parcel.getStatus(),
            parcel.getAdminNotes(),
            parcel.getCreatedAt(),
            parcel.getUpdatedAt()
        );
    }
}
