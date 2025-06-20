package com.schoolhealth.schoolmedical.model.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiseaseVaccineResponse {
    private int diseaseId;
    private int vaccineId;
    private boolean isActive;
}
