package com.realestate.app.entity;

import com.realestate.app.entity.enums.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Represents a land parcel submitted by a landowner (OWNER) or broker (AGENT).
 * Once verified by platform admins, developers can review it for acquisition or Joint Venture (JV).
 */
@Entity
@Table(
    name = "land_parcels",
    indexes = {
        @Index(name = "idx_land_city", columnList = "city"),
        @Index(name = "idx_land_status", columnList = "status"),
        @Index(name = "idx_land_user", columnList = "user_id")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LandParcel {

    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    private UUID id;

    // ── Submitter / Owner Relationship ─────────────────────────────────────────

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
        foreignKey = @ForeignKey(name = "fk_land_user"))
    private User submittedBy;

    // ── Location Details ───────────────────────────────────────────────────────

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    /**
     * Sector, Ward, Tehsil, or micro-market name (e.g., "Sector 82", "Whitefield Phase 2").
     */
    @Column(name = "sector", length = 150)
    private String sector;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "pincode", length = 20)
    private String pincode;

    /**
     * Official registry identifier: Plot number, Khasra number, or Survey number.
     */
    @Column(name = "plot_survey_number", length = 100)
    private String plotSurveyNumber;

    @Column(name = "landmark", length = 255)
    private String landmark;

    @Column(name = "location_url", length = 500)
    private String locationUrl;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    // ── Physical Dimensions ───────────────────────────────────────────────────

    @Column(name = "total_area", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalArea;

    @Enumerated(EnumType.STRING)
    @Column(name = "area_unit", nullable = false, length = 20)
    private AreaUnit areaUnit;

    /**
     * Width of the approach road directly in front of the parcel (in feet).
     */
    @Column(name = "road_width_feet")
    private Double roadWidthFeet;

    /**
     * Plot facing / open sides (e.g., "North", "East", "Corner Plot", "2-Side Open").
     */
    @Column(name = "plot_facing", length = 50)
    private String plotFacing;

    // ── Zoning & Proposal Structure ───────────────────────────────────────────

    @Enumerated(EnumType.STRING)
    @Column(name = "zoning", nullable = false, length = 30)
    private LandCategory zoning;

    @Enumerated(EnumType.STRING)
    @Column(name = "deal_type", nullable = false, length = 30)
    private ProposedDealType dealType;

    // ── Commercial Expectations ───────────────────────────────────────────────

    @Column(name = "expected_price", precision = 15, scale = 2)
    private BigDecimal expectedPrice;

    @Column(name = "price_per_unit", precision = 15, scale = 2)
    private BigDecimal pricePerUnit;

    @Column(name = "is_price_negotiable", nullable = false)
    @Builder.Default
    private Boolean isPriceNegotiable = true;

    /**
     * Expected revenue/area share percentage if applying for Joint Venture / JDA (e.g. 45.0%).
     */
    @Column(name = "expected_revenue_share_percentage")
    private Double expectedRevenueSharePercentage;

    // ── Legal & Title Status ──────────────────────────────────────────────────

    @Enumerated(EnumType.STRING)
    @Column(name = "ownership_type", length = 30)
    private OwnershipType ownershipType;

    @Column(name = "has_clear_title", nullable = false)
    @Builder.Default
    private Boolean hasClearTitle = true;

    @Column(name = "is_litigation_free", nullable = false)
    @Builder.Default
    private Boolean isLitigationFree = true;

    // ── Agent Specifics (if submitted by AGENT) ───────────────────────────────

    @Column(name = "agency_name", length = 255)
    private String agencyName;

    @Column(name = "brokerage_percentage")
    private Double brokeragePercentage;

    // ── Descriptive & Review Lifecycle ────────────────────────────────────────

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    @Builder.Default
    private LandStatus status = LandStatus.PENDING_REVIEW;

    @Column(name = "admin_notes", columnDefinition = "TEXT")
    private String adminNotes;

    // ── Timestamps ────────────────────────────────────────────────────────────

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
