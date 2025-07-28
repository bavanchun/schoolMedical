package com.schoolhealth.schoolmedical.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Schema(description = "Vaccination campaign detailed statistics")
public class VaccinationCampaignStatisticsDto {

    @Schema(description = "Campaign title", example = "COVID-19 Vaccination Campaign 2025")
    private String campaignTitle;

    @Schema(description = "Disease name", example = "COVID-19")
    private String diseaseName;

    @Schema(description = "Vaccine name", example = "Pfizer-BioNTech COVID-19")
    private String vaccineName;

    @Schema(description = "Number of pupils who agreed to vaccination", example = "45")
    private Long agreedCount;

    @Schema(description = "Number of pupils who disagreed to vaccination", example = "5")
    private Long disagreedCount;

    @Schema(description = "Number of pupils who were vaccinated", example = "40")
    private Long vaccinatedCount;

    @Schema(description = "Number of pupils who were absent during vaccination", example = "5")
    private Long absentCount;

    @Schema(description = "Total pupils in campaign", example = "50")
    private Long totalPupils;
}
