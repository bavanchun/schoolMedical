package com.schoolhealth.schoolmedical.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Schema(description = "Request DTO for dashboard statistics")
public class DashboardStatisticsRequest {

    @Schema(description = "School year for statistics", example = "2024")
    private Integer year;
}
