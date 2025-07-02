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
@Table(name = "medication")
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medication_id")
    private Long medicationId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "dosage", length = 50)
    private String dosage;

    @Column(name = "unit", length = 20)
    private String unit;

    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive = true;

    // Many-to-many relationship with medical events
    @ManyToMany(mappedBy = "medicationUsed")
    private List<MedicalEvent> medicalEvents;
}
