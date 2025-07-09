package com.schoolhealth.schoolmedical.model.dto.response;

import com.schoolhealth.schoolmedical.entity.enums.ConsentFormStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class VaccinationConsentFormResponse {
    private Long consentFormId;
    private LocalDateTime respondedAt;
    private ConsentFormStatus status;
    private LocalDateTime formDeadline;
    private Long campaignId;
    private String campaignName;
    private Long diseaseId;
    private String diseaseName;
    private Integer doseNumber;        // Total doses required for this disease
    private Integer currDoseNumber;
    private Long vaccineId;
    private String vaccineName;
    private String pupilId;
    private String pupilName;
    private String gradeLevel;


}
