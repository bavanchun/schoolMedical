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
    private Long healthCheckDiseaseId;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private HealthCheckDiseaseStatus status = HealthCheckDiseaseStatus.REJECTED;

    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disease_id")
    private Disease disease;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "health_check_campaign_id")
    private HealthCheckCampaign healthCheckCampaign;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "health_check_consent_form_id")
    private HealthCheckConsentForm healthCheckConsentForm;

}
