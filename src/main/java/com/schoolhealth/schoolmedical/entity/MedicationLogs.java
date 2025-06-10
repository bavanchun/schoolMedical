package com.schoolhealth.schoolmedical.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "medication_logs")
public class MedicationLogs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int logId;

    private int sendMedicationId;

    private String note;



}
