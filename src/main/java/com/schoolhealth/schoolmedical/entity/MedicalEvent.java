package com.schoolhealth.schoolmedical.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.schoolhealth.schoolmedical.entity.enums.MedicalEventStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "medical_event")
public class MedicalEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medical_event_id")
    private Long medicalEventId;

    @Column(name = "detailed_information", columnDefinition = "TEXT")
    private String detailedInformation;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    private MedicalEventStatus status;

    @Column(name = "date_time", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy: HH:mm:ss")
    private LocalDateTime dateTime;

    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_nurse_id", referencedColumnName = "user_id", nullable = false)
    private User schoolNurse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pupil_id", referencedColumnName = "pupil_id", nullable = false)
    private Pupil pupil;

    @OneToMany(mappedBy = "medicalEvent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicalEventSupplyUsed> supplyUsages;
}
