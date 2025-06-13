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
@Table(name = "disease")
public class Disease {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "disease_id")
    private int diseaseId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT", nullable = true)
    private String description;

    @Column(name = "is_injected_in_vaccination", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isInjectedVaccination;

    @Column(name = "dose_quantity")
    private int doseQuantity;

    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive = true;

    @OneToMany(mappedBy = "disease", cascade = CascadeType.ALL)
    private List<VaccinationCampagin> campaigns;

    @OneToMany(mappedBy = "disease", cascade = CascadeType.ALL)
    private List<DiseaseVaccine> diseaseVaccines;

}
