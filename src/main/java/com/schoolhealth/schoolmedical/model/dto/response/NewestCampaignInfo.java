package com.schoolhealth.schoolmedical.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewestCampaignInfo {
    private Long campaignId;
    private CampaignDiseaseInfo disease;
    private CampaignVaccineInfo vaccine;
    private String notes;
    private LocalDate consentFormDeadline;
    private LocalDate startDate;
    private LocalDate endDate;
    private String campaignStatus;
    private String status;
}
