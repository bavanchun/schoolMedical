package com.schoolhealth.schoolmedical.model.dto.response;

import com.schoolhealth.schoolmedical.entity.enums.ConsentFormStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class VaccinationConsentFormResponse {
    private Long consentFormId;
    private LocalDateTime respondedAt;
    private ConsentFormStatus status;
    private String campaignName;
    private String vaccineName;
    private String pupilName;
    private String pupilId;
    private String gradeLevel;
    private LocalDateTime formDeadline;


}
