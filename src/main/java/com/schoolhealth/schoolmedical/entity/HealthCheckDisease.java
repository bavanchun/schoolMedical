package com.schoolhealth.schoolmedical.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "health_check_disease")
public class HealthCheckDisease {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "health_check_disease_id")
    private Long healthCheckDiseaseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disease_id")
    private Disease disease;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "health_check_campaign_id")
    private HealthCheckCampaign healthCheckCampaign;

}
