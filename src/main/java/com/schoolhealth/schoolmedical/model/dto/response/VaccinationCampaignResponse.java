package com.schoolhealth.schoolmedical.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.schoolhealth.schoolmedical.entity.enums.VaccinationCampaignStatus;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VaccinationCampaignResponse {
    private int campaignId;
    private String titleCampaign;
    private String vaccineName;
    private String diseaseName;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate formDeadline;
    private String notes;
//    private VaccinationCampaignStatus status;
    private String status;
    private boolean isActive;
}
