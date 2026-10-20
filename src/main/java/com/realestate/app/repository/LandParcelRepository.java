package com.realestate.app.repository;

import com.realestate.app.entity.LandParcel;
import com.realestate.app.entity.enums.LandCategory;
import com.realestate.app.entity.enums.LandStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for {@link LandParcel} operations.
 */
@Repository
public interface LandParcelRepository extends JpaRepository<LandParcel, UUID> {

    List<LandParcel> findAllBySubmittedByIdOrderByCreatedAtDesc(UUID userId);

    Optional<LandParcel> findByIdAndSubmittedById(UUID id, UUID userId);

    List<LandParcel> findAllByStatusOrderByCreatedAtDesc(LandStatus status);

    List<LandParcel> findAllByStatusAndCityIgnoreCaseOrderByCreatedAtDesc(LandStatus status, String city);

    List<LandParcel> findAllByStatusAndZoningOrderByCreatedAtDesc(LandStatus status, LandCategory zoning);

    List<LandParcel> findByOrderByCreatedAtDesc();

    long countByStatus(LandStatus status);

    long countBySubmittedById(UUID userId);
}
