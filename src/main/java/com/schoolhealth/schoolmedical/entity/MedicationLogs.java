package com.schoolhealth.schoolmedical.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.schoolhealth.schoolmedical.entity.enums.StatusMedLogs;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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
    private Long logId;

    @Column(name = "given_time", nullable = false)
    @CreationTimestamp
    private LocalDateTime givenTime;

    @Enumerated(EnumType.STRING)
    private StatusMedLogs status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "send_medication_id", nullable = false)
    private SendMedication sendMedication;

}
