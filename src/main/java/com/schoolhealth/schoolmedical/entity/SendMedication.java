package com.schoolhealth.schoolmedical.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "send_medication")
public class SendMedication {
    @Id
    private String sendMedicationId;
}
