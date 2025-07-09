package com.schoolhealth.schoolmedical.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class LatestHealthCheckCampaignRes {
    private Long campaignId;
    private String title;
    private String description;
    private String address;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime startExaminationDate;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime endExaminationDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate deadlineDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate createdAt;
    private StatusHealthCampaign statusHealthCampaign;
}
