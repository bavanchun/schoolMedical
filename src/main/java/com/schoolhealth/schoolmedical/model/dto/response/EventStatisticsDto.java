package com.schoolhealth.schoolmedical.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Schema(description = "Event statistics data")
public class EventStatisticsDto {

    @Schema(description = "Date or month", example = "Jan")
    private String date;

    @Schema(description = "Number of medical events", example = "10")
    private Long eventCount;
}
