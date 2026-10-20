package com.realestate.app.entity;

import com.realestate.app.entity.enums.DeveloperExpertise;
import com.realestate.app.entity.enums.VerificationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.*;

/**
 * Admin-managed developer profile.
 * Developers do NOT have login accounts — they are curated manually by admins.
 * Each developer can have multiple {@link Project} records.
 */
@Entity
@Table(
    name = "developer_profiles",
    indexes = {
        @Index(name = "idx_dev_rera_number", columnList = "rera_number", unique = true)
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeveloperProfile {

    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    private UUID id;

    // ── Identity ───────────────────────────────────────────────────────────────

    @Column(name = "company_name", nullable = false, length = 255)
    private String companyName;

    @Column(name = "contact_person_name", length = 255)
    private String contactPersonName;

    @Column(name = "contact_email", length = 255)
    private String contactEmail;

    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    @Column(name = "website_url", length = 500)
    private String websiteUrl;

    // ── Regulatory ─────────────────────────────────────────────────────────────

    @Column(name = "rera_number", nullable = false, unique = true, length = 50)
    private String reraNumber;

    @Column(name = "gst_number", length = 20)
    private String gstNumber;

    // ── About ──────────────────────────────────────────────────────────────────

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "established_year")
    private Integer establishedYear;

    /**
     * Number of years the developer has been active in the market.
     * Set by admin (e.g. 17).
     */
    @Column(name = "years_of_experience")
    @Builder.Default
    private Integer yearsOfExperience = 0;

    /**
     * Total number of happy / satisfied customers.
     * Set by admin (e.g. 2500).
     */
    @Column(name = "happy_customers")
    @Builder.Default
    private Integer happyCustomers = 0;

    /**
     * Stored in a separate join table: developer_core_expertise.
     */
    @ElementCollection(targetClass = DeveloperExpertise.class, fetch = FetchType.EAGER)
    @CollectionTable(
        name = "developer_core_expertise",
        joinColumns = @JoinColumn(name = "developer_profile_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "expertise", length = 30)
    @Builder.Default
    private Set<DeveloperExpertise> coreExpertise = new HashSet<>();

    /**
     * Stored in a separate join table: developer_operating_cities.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "developer_operating_cities",
        joinColumns = @JoinColumn(name = "developer_profile_id")
    )
    @Column(name = "city", length = 100)
    @Builder.Default
    private Set<String> operatingCities = new HashSet<>();

    // ── Status ─────────────────────────────────────────────────────────────────

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false, length = 20)
    @Builder.Default
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    // ── Projects ───────────────────────────────────────────────────────────────

    /**
     * All projects delivered or in pipeline by this developer.
     * Managed exclusively by admins.
     */
    @OneToMany(mappedBy = "developer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Project> projects = new ArrayList<>();

    // ── Audit ──────────────────────────────────────────────────────────────────

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
