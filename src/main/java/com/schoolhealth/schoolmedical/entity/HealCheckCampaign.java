package com.schoolhealth.schoolmedical.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.schoolhealth.schoolmedical.entity.enums.StatusHealthCampaign;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "heal_check_campaign")
public class HealCheckCampaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "campaign_id", nullable = false, unique = true)
    private int campaignId;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "start_examination_date", nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime startExaminationDate;

    @Column(name = "end_examination_date", nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime endExaminationDate;

    @Column(name = "publidation_date", nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate publidationDate;

    @Column(name = "deadline_date", nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate deadlineDate;

    @Column(name = "description", nullable = true, columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate createdAt;

    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive;

    @Enumerated(EnumType.STRING)
    private StatusHealthCampaign statusHealthCampaign;

    @OneToMany(
            mappedBy = "healCheckCampaign",
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.LAZY
    )
    private List<HealthCheckConsentForm> healthCheckConsentForms;

    @OneToMany(
            mappedBy = "healthCheckCampaign",
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.LAZY
    )
    private List<HealthCheckDisease> healthCheckDiseases;

    // Additional fields and methods can be added as needed
}
