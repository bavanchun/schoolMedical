package com.schoolhealth.schoolmedical.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.List;

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
    private Long consentFormId;

    @Column(name = "school_year", nullable = false)
    private int schoolYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pupil_id", nullable = false)
    private Pupil pupil;

    @OneToOne(mappedBy = "healthCheckConsentForm", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private HealthCheckHistory healthCheckHistory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", nullable = false)
    private HealthCheckCampaign healthCheckCampaign;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "healthCheckConsentForm")
    private List<ConsentDisease> consentDiseases;

    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(0) DEFAULT 0")
    private boolean active;
}
