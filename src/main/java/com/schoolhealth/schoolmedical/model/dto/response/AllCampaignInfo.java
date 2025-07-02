package com.schoolhealth.schoolmedical.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllCampaignInfo {
    private Long campaignId;
    private Long diseaseId;
    private String diseaseName;
    private Long vaccineId;
    private String vaccineName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate formDeadline;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private String notes;
    private String status;
}
