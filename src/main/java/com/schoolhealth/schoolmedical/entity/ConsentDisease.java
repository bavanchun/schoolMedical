package com.schoolhealth.schoolmedical.entity;

import com.schoolhealth.schoolmedical.entity.enums.DiseaseConsentStatus;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "consent_disease")
public class ConsentDisease {
    @EmbeddedId
    @Column(name = "id")
    private ConsentDiseaseId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("consentFormId")
    @JoinColumn(name = "consent_form_id", nullable = false)
    private HealthCheckConsentForm healthCheckConsentForm;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("diseaseId")
    @JoinColumn(name = "disease_id", nullable = false)
    private Disease disease;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;
}
