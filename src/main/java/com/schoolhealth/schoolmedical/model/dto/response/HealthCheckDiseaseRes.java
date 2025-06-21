package com.schoolhealth.schoolmedical.model.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HealthCheckDiseaseRes {
    private Long healthCheckDiseaseId;
    private String diseaseName;
}
