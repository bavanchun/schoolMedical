package com.schoolhealth.schoolmedical.model.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampaignDiseaseInfo {
    private Long disease_id;
    private String name;
    private String description;
    private Boolean isInjectedVaccine;
    private int doseQuantity;
}
