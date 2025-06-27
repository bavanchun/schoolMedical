package com.schoolhealth.schoolmedical.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private Long diseaseId;
    private String name;
    private String description;

    // Đảm bảo trường này luôn được JSON hóa là "injectedVaccination"
    @JsonProperty("injectedVaccination")
    private Boolean isInjectedVaccination;

    private int doseQuantity;

    // Đảm bảo trường này luôn được JSON hóa là "active"
    @JsonProperty("active")
    private boolean isActive;
}
