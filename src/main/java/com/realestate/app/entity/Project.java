package com.realestate.app.entity;

import com.realestate.app.entity.enums.ProjectStatus;
import com.realestate.app.entity.enums.ProjectType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a real estate project delivered or in pipeline by a {@link DeveloperProfile}.
 * Managed exclusively by admins — public consumers read this via the developer listing API.
 */
@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {

    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    private UUID id;

    // ── Ownership ──────────────────────────────────────────────────────────────

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "developer_profile_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_project_developer"))
    private DeveloperProfile developer;

    // ── Core Info ──────────────────────────────────────────────────────────────

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "project_type", nullable = false, length = 20)
    private ProjectType projectType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private ProjectStatus status = ProjectStatus.UPCOMING;

    // ── Location ───────────────────────────────────────────────────────────────

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "pincode", length = 10)
    private String pincode;

    // ── Area & Units ───────────────────────────────────────────────────────────

    /** Minimum unit size in sq. ft. */
    @Column(name = "area_sq_ft_min")
    private Integer areaSqFtMin;

    /** Maximum unit size in sq. ft. */
    @Column(name = "area_sq_ft_max")
    private Integer areaSqFtMax;

    @Column(name = "total_units")
    private Integer totalUnits;

    // ── Regulatory ─────────────────────────────────────────────────────────────

    @Column(name = "rera_project_number", length = 50)
    private String reraProjectNumber;

    /** Year the project was delivered (applicable when status = DELIVERED). */
    @Column(name = "delivered_year")
    private Integer deliveredYear;

    // ── Amenities ──────────────────────────────────────────────────────────────

    /**
     * List of amenity names stored in a join table: project_amenities.
     * Examples: "Swimming Pool", "Gym", "Clubhouse", "24/7 Security"
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "project_amenities",
        joinColumns = @JoinColumn(name = "project_id")
    )
    @Column(name = "amenity", length = 150)
    @Builder.Default
    private List<String> amenities = new ArrayList<>();

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
