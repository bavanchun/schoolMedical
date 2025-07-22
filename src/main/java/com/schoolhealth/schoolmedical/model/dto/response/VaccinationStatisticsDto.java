package com.schoolhealth.schoolmedical.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Schema(description = "Vaccination statistics data")
public class VaccinationStatisticsDto {

    @Schema(description = "Vaccine name", example = "Hepatitis A")
    private String vaccine;

    @Schema(description = "Number of pupils vaccinated", example = "60")
    private Long count;
}
