package com.schoolhealth.schoolmedical.model.dto.request;


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
public class DiseaseRequest {
    private String name;
    private String description;
    private boolean isInjectedVaccination;
    private int doseQuantity;

}
