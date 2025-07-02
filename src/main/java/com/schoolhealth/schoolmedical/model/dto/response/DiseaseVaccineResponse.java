package com.schoolhealth.schoolmedical.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiseaseVaccineResponse {
    private boolean success;
    private String message;
    private Long diseaseId;
    private String diseaseName;
    private Long vaccineId;
    private String vaccineName;

    // Additional fields if needed
}
