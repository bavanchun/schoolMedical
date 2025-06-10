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
@Table(name = "medical_event_equipment_used")
public class MedicalEventEquipmentUsed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_used_id")
    private Integer equipmentUsedId;

    @Column(name = "equipment_id", nullable = false)
    private Integer equipmentId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "medicalEventEquipmentUsed", cascade = CascadeType.ALL)
    private List<MedicalEventSupplyUsed> medicalEventSuppliesUsed;
}
