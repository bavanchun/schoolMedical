package com.schoolhealth.schoolmedical.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    private List<DiseaseResponse> diseases;
    private List<HealthCheckConsentRes> consentForms;
}

