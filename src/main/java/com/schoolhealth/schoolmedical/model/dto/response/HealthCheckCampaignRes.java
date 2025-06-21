package com.schoolhealth.schoolmedical.model.dto.response;

import com.schoolhealth.schoolmedical.entity.HealthCheckConsentForm;
import com.schoolhealth.schoolmedical.entity.enums.StatusHealthCampaign;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HealthCheckCampaignRes {
    private Long campaignId;
    private String address;
    private String title;
    private String description;
    private LocalDate deadlineDate;
    private LocalDateTime startExaminationDate;
    private LocalDateTime endExaminationDate;
    private LocalDate createdAt;
    private StatusHealthCampaign statusHealthCampaign;
    private List<HealthCheckConsentRes> consentForms;
    private List<String> diseaseNames;
}

