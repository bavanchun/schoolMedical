package com.schoolhealth.schoolmedical.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "health_check_consent_form")
public class HealthCheckConsentForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consent_form_id", nullable = false, unique = true)
    private int consentFormId;

    @Column(name = "school_year", nullable = false)
    private int schoolYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pupil_id", nullable = false)
    private Pupil pupil;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "health_check_history_id", nullable = false)
    private HealthCheckHistory healthCheckHistory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", nullable = false)
    private HealCheckCampaign healCheckCampaign;

    @Column(name = "is_active", nullable = false, columnDefinition = "boolean default true")
    private boolean isActive;
}
