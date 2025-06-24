package com.schoolhealth.schoolmedical.model.dto.response;

import com.schoolhealth.schoolmedical.entity.enums.ConsentFormStatus;

import java.time.LocalDateTime;

public class VaccinationConsentFormResponse {
    private Long consentFormId;
    private int doseNumber;
    private LocalDateTime respondedAt;
    private ConsentFormStatus status;
    private String campaignName;
    private String vaccineName;
    private String pupilName;
    private String pupilId;
    private String gradeLevel;
    private LocalDateTime formDeadline;


}
