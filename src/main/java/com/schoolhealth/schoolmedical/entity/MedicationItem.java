package com.schoolhealth.schoolmedical.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "medication_item")
public class MedicationItem {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "medication_id", nullable = false)
    private Long medicationId;
    @Column(name = "medication_name", nullable = false, length = 255)
    private String medicationName;
    @Column(name = "unit_and_usage", nullable = false, columnDefinition = "TEXT")
    private String unitAndUsage;
    @Column(name = "medication_schedule", nullable = false, length = 255)
    private String medicationSchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "send_medication_id", nullable = false)
    private SendMedication sendMedication;
}
