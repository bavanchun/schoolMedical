package com.schoolhealth.schoolmedical.entity;

import com.schoolhealth.schoolmedical.entity.enums.ConsentFormStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "vaccination_consent_form")
public class VaccinationConsentForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consent_form_id")
    private Long consentFormId;

    @Column(name = "campaign_id")
    private int campaignId;

    @Column(name = "pupil_id", length = 255)
    private String pupilId;

    @Column(name = "vaccine_id")
    private int vaccineId;

    @Column(name = "dose_number")
    private int doseNumber;

    @Column(name = "responded_at")
    private LocalDateTime respondedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ConsentFormStatus status;

    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", referencedColumnName = "campaign_id", insertable = false, updatable = false)
    private VaccinationCampagin campaign;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pupil_id", referencedColumnName = "pupil_id", insertable = false, updatable = false)
    private Pupil pupil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaccine_id", referencedColumnName = "vaccine_id", insertable = false, updatable = false)
    private Vaccine vaccine;
}
