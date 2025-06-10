package com.schoolhealth.schoolmedical.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "vaccine")
public class Vaccine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vaccine_id")
    private int vaccineId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "manufacturer", length = 255)
    private String manufacturer;

    @Column(name = "recommended_age", length = 100)
    private String recommendedAge;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "dose_number")
    private int doseNumber;

    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive = true;

    @OneToMany(mappedBy = "vaccine", cascade = CascadeType.ALL)
    private List<VaccinationCampagin> campaigns;

    @OneToMany(mappedBy = "vaccine", cascade = CascadeType.ALL)
    private List<VaccinationHistory> vaccinationHistories;

    @OneToMany(mappedBy = "vaccine", cascade = CascadeType.ALL)
    private List<VaccinationConsentForm> consentForms;

    @OneToMany(mappedBy = "vaccine", cascade = CascadeType.ALL)
    private List<DiseaseVaccine> diseaseVaccines;
}
