package com.realestate.app.dto.land;

import com.realestate.app.entity.enums.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * Request payload for submitting a new land parcel (by OWNER or AGENT).
 */
public record CreateLandParcelRequest(

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    String title,

    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City must not exceed 100 characters")
    String city,

    @Size(max = 150, message = "Sector/Area name must not exceed 150 characters")
    String sector,

    @Size(max = 100, message = "State must not exceed 100 characters")
    String state,

    @Size(max = 20, message = "Pincode must not exceed 20 characters")
    String pincode,

    @Size(max = 100, message = "Plot / Survey number must not exceed 100 characters")
    String plotSurveyNumber,

    @Size(max = 255, message = "Landmark must not exceed 255 characters")
    String landmark,

    @Size(max = 500, message = "Location URL must not exceed 500 characters")
    String locationUrl,

    Double latitude,

    Double longitude,

    @NotNull(message = "Total area is required")
    @Positive(message = "Total area must be greater than zero")
    BigDecimal totalArea,

    @NotNull(message = "Area unit is required (e.g. ACRES, SQ_YARDS, SQ_FEET, HECTARES)")
    AreaUnit areaUnit,

    @Positive(message = "Road width must be a positive number")
    Double roadWidthFeet,

    @Size(max = 50, message = "Plot facing must not exceed 50 characters")
    String plotFacing,

    @NotNull(message = "Zoning / Land category is required")
    LandCategory zoning,

    @NotNull(message = "Deal type is required (e.g. OUTRIGHT_SALE, JOINT_VENTURE, JOINT_DEVELOPMENT, LONG_TERM_LEASE)")
    ProposedDealType dealType,

    @Positive(message = "Expected price must be positive")
    BigDecimal expectedPrice,

    @Positive(message = "Price per unit must be positive")
    BigDecimal pricePerUnit,

    Boolean isPriceNegotiable,

    @DecimalMin(value = "0.0", message = "Revenue share cannot be negative")
    @DecimalMax(value = "100.0", message = "Revenue share cannot exceed 100%")
    Double expectedRevenueSharePercentage,

    OwnershipType ownershipType,

    Boolean hasClearTitle,

    Boolean isLitigationFree,

    @Size(max = 255, message = "Agency name must not exceed 255 characters")
    String agencyName,

    @DecimalMin(value = "0.0", message = "Brokerage cannot be negative")
    @DecimalMax(value = "100.0", message = "Brokerage cannot exceed 100%")
    Double brokeragePercentage,

    String description

) {}
