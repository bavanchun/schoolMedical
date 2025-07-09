package com.schoolhealth.schoolmedical.model.dto.response;

import com.schoolhealth.schoolmedical.entity.enums.GradeLevel;
import com.schoolhealth.schoolmedical.entity.enums.StatusHealthCampaign;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HealthCheckCampaignFlatData {
    private Long campaignId;
    private String title;
    private String address;
    private String description;
    private LocalDate deadlineDate;
    private LocalDateTime startExaminationDate;
    private LocalDateTime endExaminationDate;
    private LocalDate createdAt;
    private StatusHealthCampaign statusHealthCampaign;
    private Long healthCheckConsentId;
    private int schoolYear;
    private String pupilId;
    private String lastName;
    private String firstName;
    private LocalDate birthDate;
    private char gender;
    private String avatar;
    private Long gradeId;
    private GradeLevel gradeLevel;
    private String gradeName;
    private Long diseaseId;
    private String diseaseName;
}
