package com.realestate.app.dto.land;

import com.realestate.app.entity.enums.LandStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request payload used by Admins to verify, approve, or reject a submitted land parcel.
 */
public record UpdateLandStatusRequest(

    @NotNull(message = "Status is required")
    LandStatus status,

    @Size(max = 1000, message = "Admin notes must not exceed 1000 characters")
    String adminNotes

) {}
