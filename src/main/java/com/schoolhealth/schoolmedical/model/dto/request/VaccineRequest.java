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
public class VaccineRequest {
    private String name;
    private String manufacturer;
    private String recommendedAge;
    private String description;
    private int doseNumber;
}
