package com.schoolhealth.schoolmedical.model.dto.response;

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
public class DiseaseResponse {
    private int diseaseId;
    private String name;
    private String description;
    private boolean isInjectedVaccination;
    private int doseQuantity;
    private boolean isActive;

}
