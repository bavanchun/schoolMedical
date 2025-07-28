package com.schoolhealth.schoolmedical.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Schema(description = "Dashboard statistics response")
public class DashboardStatisticsResponse {

    @Schema(description = "Total number of pupils in school", example = "480")
    private Long totalPupils;

    @Schema(description = "Total number of pupils who had health checks", example = "520")
    private Long totalHealthChecks;

    @Schema(description = "Total number of pupils who were vaccinated", example = "275")
    private Long totalVaccinations;

    @Schema(description = "Total number of medical events", example = "74")
    private Long totalMedicalEvents;

    @Schema(description = "Prescription statistics for the year")
    private PrescriptionStatsDto prescriptionsLastMonth;

    @Schema(description = "Campaign statistics")
    private List<CampaignStatisticsDto> campaigns;

    @Schema(description = "Vaccination statistics by vaccine type")
    private List<VaccinationStatisticsDto> vaccinations;

    @Schema(description = "Medical events statistics by month")
    private List<EventStatisticsDto> events;
}
