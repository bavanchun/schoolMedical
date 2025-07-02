package com.schoolhealth.schoolmedical.model.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HealthCheckConsentSimpleRes {
    private int schoolYear;
    private List<ConsentDiseaseRes> disease;
}
