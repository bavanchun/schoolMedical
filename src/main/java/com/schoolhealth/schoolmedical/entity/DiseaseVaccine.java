package com.schoolhealth.schoolmedical.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@IdClass(DiseaseVaccineId.class)
@Entity
@Table(name = "disease_vaccine")
public class DiseaseVaccine {

    @Id
    @Column(name = "disease_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long diseaseId;

    @Id
    @Column(name = "vaccine_id")
    private int vaccineId;

    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disease_id", referencedColumnName = "disease_id", insertable = false, updatable = false)
    private Disease disease;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaccine_id", referencedColumnName = "vaccine_id", insertable = false, updatable = false)
    private Vaccine vaccine;
}
