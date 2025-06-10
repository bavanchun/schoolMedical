package com.schoolhealth.schoolmedical.entity;

import com.schoolhealth.schoolmedical.entity.enums.VaccinationCampaignStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "vaccination_campaign")
public class VaccinationCampagin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "campaign_id")
    private int campaignId;

    @Column(name = "vaccine_id")
    private int vaccineId;

    @Column(name = "disease_id")
    private int diseaseId;

    @Column(name = "sent_date")
    private LocalDate sentDate;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "stop_date")
    private LocalDate stopDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private VaccinationCampaignStatus status;

    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaccine_id", referencedColumnName = "vaccine_id", insertable = false, updatable = false)
    private Vaccine vaccine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disease_id", referencedColumnName = "disease_id", insertable = false, updatable = false)
    private Disease disease;

    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL)
    private List<VaccinationHistory> vaccinationHistories;

    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL)
    private List<VaccinationConsentForm> consentForms;
}
