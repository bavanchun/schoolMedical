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
@Table(name = "medical_event_medication_used")
public class MedicalEventMedicationUsed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medication_used_id")
    private Long medicationUsedId;

    @Column(name = "name_medication", nullable = false)
    private String nameMedication;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive = true;

//    @OneToMany(mappedBy = "medicalEventMedicationUsed", cascade = CascadeType.ALL)
//    private List<MedicalEventSupplyUsed> medicalEventSuppliesUsed;
// Many-to-many relationship with medical events
@ManyToMany(mappedBy = "medicationUsed")
private List<MedicalEvent> medicalEvents;
}
