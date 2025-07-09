package com.schoolhealth.schoolmedical.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiseaseWithVaccinesWrapper {
    @JsonProperty("GetVaccineByDisease")
    private List<DiseaseVaccineInfo> getVaccineByDisease;
}
