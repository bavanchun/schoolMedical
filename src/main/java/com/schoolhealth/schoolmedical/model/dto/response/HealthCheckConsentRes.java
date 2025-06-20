package com.schoolhealth.schoolmedical.model.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HealthCheckConsentRes {
    private Long healthCheckConsentId;
    private int schoolYear;
    private PupilRes pupilRes;
}
