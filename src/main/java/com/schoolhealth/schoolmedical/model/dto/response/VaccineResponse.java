package com.schoolhealth.schoolmedical.model.dto.response;

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
public class VaccineResponse {
    private int vaccineId;
    private String name;
    private String manufacturer;
    private String recommendedAge;
    private String description;
    private int doseNumber;

    // Trường này sẽ được JSON hóa là "active"
    @JsonProperty("active")
    private boolean isActive;
}
