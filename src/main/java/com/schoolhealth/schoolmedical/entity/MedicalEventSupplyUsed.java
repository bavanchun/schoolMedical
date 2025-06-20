package com.schoolhealth.schoolmedical.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "medical_event_supply_used")
public class MedicalEventSupplyUsed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supply_used_id")
    private Long supplyUsedId;

    @Column(name = "equipment_used_id")
    private int equipmentUsedId;

    @Column(name = "medication_used_id")
    private int medicationUsedId;

    @Column(name = "medical_event_id", nullable = false)
    private int medicalEventId;

    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive = true;

    // Relationship with MedicalEvent
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_event_id", referencedColumnName = "medical_event_id", insertable = false, updatable = false)
    private MedicalEvent medicalEvent;

    // Relationship with MedicalEventEquipmentUsed
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_used_id", referencedColumnName = "equipment_used_id", insertable = false, updatable = false)
    private MedicalEventEquipmentUsed medicalEventEquipmentUsed;

    // Relationship with MedicalEventMedicationUsed
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medication_used_id", referencedColumnName = "medication_used_id", insertable = false, updatable = false)
    private MedicalEventMedicationUsed medicalEventMedicationUsed;
}
