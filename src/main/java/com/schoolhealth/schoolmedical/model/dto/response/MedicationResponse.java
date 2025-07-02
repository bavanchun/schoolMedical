package com.schoolhealth.schoolmedical.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MedicationResponse {

    private Long medicationId;
    private String name;
    private String description;
    private String dosage;
    private String unit;
    private Boolean isActive;
}
