package com.schoolhealth.schoolmedical.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HealthCheckConsentRes {
    private Long healthCheckConsentId;
    private int schoolYear;
    private PupilRes pupilRes;
    private List<HealthCheckDiseaseRes> disease;
}
