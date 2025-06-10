package com.schoolhealth.schoolmedical.entity;

import com.schoolhealth.schoolmedical.entity.enums.StatusSendMedication;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "send_medication")
public class SendMedication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sendMedicationId;

    @Column(name = "medication_img", nullable = true, length = 255)
    private String medicationImg;

    @Column(name = "note", nullable = true, columnDefinition = "TEXT")
    private String note;

    @Column(name = "unit_measure", nullable = false, length = 50)
    private String unitMeasure;

//    @Column(name = "medication_schedule", nullable = false, length = 255)
//    private String medicationSchedule;

    @Column(name = "confirmed_date", nullable = false)
    private Date confirmedDate;

    @Column(name = "requested_date", nullable = false)
    private Date requestedDate;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Enumerated(EnumType.STRING)
    private StatusSendMedication status;

    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive;


}
