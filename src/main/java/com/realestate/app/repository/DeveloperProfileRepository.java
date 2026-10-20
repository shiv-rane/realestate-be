package com.realestate.app.repository;

import com.realestate.app.entity.DeveloperProfile;
import com.realestate.app.entity.enums.VerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for {@link DeveloperProfile} entity operations.
 */
@Repository
public interface DeveloperProfileRepository extends JpaRepository<DeveloperProfile, UUID> {

    boolean existsByReraNumber(String reraNumber);

    /** Public listing — only show APPROVED developers. */
    List<DeveloperProfile> findAllByVerificationStatus(VerificationStatus status);

    /** Admin stats. */
    long countByVerificationStatus(VerificationStatus status);
}
