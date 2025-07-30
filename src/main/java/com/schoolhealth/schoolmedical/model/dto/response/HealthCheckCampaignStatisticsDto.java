package com.schoolhealth.schoolmedical.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Schema(description = "Health check campaign detailed statistics")
public class HealthCheckCampaignStatisticsDto {

    @Schema(description = "Campaign title", example = "Annual Health Check Campaign 2025")
    private String campaignTitle;

    @Schema(description = "Number of pupils who agreed to health check", example = "45")
    private Long agreedCount;

    @Schema(description = "Number of pupils who disagreed to health check", example = "5")
    private Long disagreedCount;

    @Schema(description = "Number of pupils who were examined", example = "40")
    private Long examinedCount;

    @Schema(description = "Number of pupils who were absent during examination", example = "5")
    private Long absentCount;

    @Schema(description = "Total pupils in campaign", example = "50")
    private Long totalPupils;
}
