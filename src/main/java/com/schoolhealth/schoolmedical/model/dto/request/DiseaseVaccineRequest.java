package com.schoolhealth.schoolmedical.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiseaseVaccineRequest {

    @NotNull(message = "Disease ID cannot be null")
    @Positive(message = "Disease ID must be a positive integer")
    private int diseaseId;

    @NotNull(message = "Vaccine ID cannot be null")
    @Positive(message = "Vaccine ID must be a positive integer")
    private int vaccineId;
}
