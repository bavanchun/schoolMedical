package com.schoolhealth.schoolmedical.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Schema(description = "Prescription statistics data")
public class PrescriptionStatsDto {

    @Schema(description = "Total count of pupils with prescriptions", example = "35")
    private Long count;

    @Schema(description = "List of common disease types", example = "[\"Antibiotics\", \"Allergy Meds\", \"Pain Relievers\"]")
    private List<String> commonTypes;
}
