package com.schoolhealth.schoolmedical.entity;

import com.schoolhealth.schoolmedical.entity.enums.StatusSendMedication;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;

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
    @Column(name = "send_medication_id", nullable = false, unique = true)
    private Long sendMedicationId;

    @Column(name = "disease_name", nullable = false, length = 100)
    private String diseaseName;

    @Column(name = "medication_img", nullable = true, length = 255)
    private String medicationImg;

    @Column(name = "note", nullable = true, columnDefinition = "TEXT")
    private String note;

    @Column(name = "confirmed_date", nullable = true)
    private LocalDate confirmedDate;

    @Column(name = "requested_date", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDate requestedDate;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private StatusSendMedication status;

    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean active;

    @OneToMany(
            mappedBy = "sendMedication",
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            orphanRemoval = true
    )
    private List<MedicationLogs> medicationLogs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pupil_id", nullable = false)
    private Pupil pupil;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sendMedication", cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    private List<MedicationItem> medicationItems;

}
