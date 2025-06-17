package com.schoolhealth.schoolmedical.entity;

import com.schoolhealth.schoolmedical.entity.enums.StatusHealthCampaign;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "health_check_campaign")
public class HealthCheckCampaign {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "campaign_id", nullable = false, unique = true)
//    private int campaignId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "campaign_id", nullable = false, unique = true)
    private Long campaignId;

    @NotBlank(message = "Address cannot be null")
    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @NotNull
    @Column(name = "start_examination_date", nullable = false)
    private LocalDateTime startExaminationDate;

    @NotNull
    @Column(name = "end_examination_date", nullable = false)
    private LocalDateTime endExaminationDate;

    @NotNull
    @Column(name = "deadline_date", nullable = false)
    private LocalDate deadlineDate;

    @Column(name = "description", nullable = true, columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDate createdAt;

    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isActive;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_health_campaign", nullable = false)
    private StatusHealthCampaign statusHealthCampaign;

    @OneToMany(
            mappedBy = "healthCheckCampaign",
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
