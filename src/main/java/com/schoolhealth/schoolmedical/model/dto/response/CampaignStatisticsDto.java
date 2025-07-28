package com.schoolhealth.schoolmedical.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Schema(description = "Campaign statistics data")
public class CampaignStatisticsDto {

    @Schema(description = "Campaign title", example = "Health Check Q1")
    private String title;

    @Schema(description = "Number of pupils participated", example = "120")
    private Long pupilCount;
}
