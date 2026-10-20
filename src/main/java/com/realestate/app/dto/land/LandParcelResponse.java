package com.realestate.app.dto.land;

import com.realestate.app.entity.enums.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Standard response representation of a Land Parcel.
 */
public record LandParcelResponse(

    UUID id,

    // Submitter info
    SubmitterInfo submittedBy,

    // Location
    String title,
    String city,
    String sector,
    String state,
    String pincode,
    String plotSurveyNumber,
    String landmark,
    String locationUrl,
    Double latitude,
    Double longitude,

    // Dimensions
    BigDecimal totalArea,
    AreaUnit areaUnit,
    Double roadWidthFeet,
    String plotFacing,

    // Zoning & Deal
    LandCategory zoning,
    ProposedDealType dealType,

    // Commercials
    BigDecimal expectedPrice,
    BigDecimal pricePerUnit,
    Boolean isPriceNegotiable,
    Double expectedRevenueSharePercentage,

    // Legal
    OwnershipType ownershipType,
    Boolean hasClearTitle,
    Boolean isLitigationFree,

    // Agent specifics
    String agencyName,
    Double brokeragePercentage,

    // Status & Notes
    String description,
    LandStatus status,
    String adminNotes,

    Instant createdAt,
    Instant updatedAt

) {
    public record SubmitterInfo(
        UUID id,
        String name,
        String email,
        UserRole role
    ) {}
}
