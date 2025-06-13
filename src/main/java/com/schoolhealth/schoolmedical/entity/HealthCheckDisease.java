package com.schoolhealth.schoolmedical.entity;

import com.schoolhealth.schoolmedical.entity.enums.HealthCheckDiseaseStatus;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "health_check_disease")
public class HealthCheckDisease {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "health_check_disease_id")
    private int healthCheckDiseaseId;

    @Column(name = "health_check_campaign_id")
    private int healthCheckCampaignId;

    @Column(name = "disease_id")
    private int diseaseId;

    @Column(name = "health_check_consent_form_id")
    private int healthCheckConsentFormId;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private HealthCheckDiseaseStatus status;

    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive = true;

    @Column(name = "is_sensitive", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isSensitive = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disease_id", referencedColumnName = "disease_id", insertable = false, updatable = false)
    private Disease disease;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "health_check_campaign_id", referencedColumnName = "campaign_id", insertable = false, updatable = false)
    private HealCheckCampaign healthCheckCampaign;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "health_check_consent_form_id", referencedColumnName = "consent_form_id", insertable = false, updatable = false)
    private HealthCheckConsentForm healthCheckConsentForm;

}
