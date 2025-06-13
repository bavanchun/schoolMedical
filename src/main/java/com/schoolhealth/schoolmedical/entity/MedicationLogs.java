package com.schoolhealth.schoolmedical.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.schoolhealth.schoolmedical.entity.enums.StatusMedLogs;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @Column(nullable = true, columnDefinition = "TEXT")
    private String note;

    @Column(name = "given_time", nullable = true)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime givenTime;

    @Enumerated(EnumType.STRING)
    private StatusMedLogs status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "send_medication_id", nullable = false)
    private SendMedication sendMedication;

}
