package com.schoolhealth.schoolmedical.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiseaseVaccineInfo {
    private Long diseaseId;
    private String diseaseName;
    private int doseQuantity;
    private List<VaccineInfo> vaccines;
}
