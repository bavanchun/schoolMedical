package com.schoolhealth.schoolmedical.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "health_check_history")
public class HealthCheckHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "health_check_history_id", nullable = false, unique = true)
    private Long healthId;

    @Column(name = "height", precision = 5, scale = 2, nullable = true)
    private BigDecimal height;

    @Column(name = "weight", precision = 5, scale = 2, nullable = true)
    private BigDecimal weight;

    @Column(name = "right_eye_vision", length = 20, nullable = true)
    private String rightEyeVision;

    @Column(name = "left_eye_vision", length = 20, nullable = true)
    private String leftEyeVision;

    @Column(name = "blood_pressure", length = 20, nullable = true)
    private String bloodPressure;

    @Column(name = "heart_rate", nullable = true)
    private int heartRate;

    @Column(name = "dental_check", length = 100, nullable = true)
    private String dentalCheck;

    @Column(name = "ear_condition", length = 100, nullable = true)
    private String earCondition;

    @Column(name = "nose_condition", length = 100, nullable = true)
    private String noseCondition;

    @Column(name = "throat_condition", length = 100, nullable = true)
    private String throatCondition;

    @Column(name = "skin_and_mucosa", length = 100, nullable = true)
    private String skinAndMucosa;

    @Column(name = "hear_anuscultaion", length = 100, nullable = true)
    private String hearAnuscultaion;

    @Column(name = "chest_shape", length = 100, nullable = true)
    private String chestShape;

    @Column(name = "lungs", length = 100, nullable = true)
    private String lungs;

    @Column(name = "digestive_system", length = 100, nullable = true)
    private String digestiveSystem;

    @Column(name = "urinary_system", length = 100, nullable = true)
    private String urinarySystem;

    @Column(name = "musculoskeletal_system", length = 100, nullable = true)
    private String musculoskeletalSystem;

    @Column(name = "neurology_and_psychiatry", length = 100, nullable = true)
    private String neurologyAndPsychiatry;

    @Column(name = "genital_examination", length = 100, nullable = true)
    private String genitalExamination;

    @Column(name = "additional_notes", length = 255, nullable = true)
    private String additionalNotes;

    @Column(name = "unusual_signs", length = 255, nullable = true)
    private String unusualSigns;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDate createdAt;

    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean active;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "consent_id", nullable = true)
    @JsonIgnore
    private HealthCheckConsentForm healthCheckConsentForm;
}
