package com.schoolhealth.schoolmedical.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HealthCheckConsentRes {
    private Long consentFormId;
    private int schoolYear;
    private PupilRes pupilRes;
    private List<ConsentDiseaseRes> disease;
}
