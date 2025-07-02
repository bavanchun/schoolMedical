package com.schoolhealth.schoolmedical.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MedicationRequest {

    @NotBlank(message = "Medication name is required")
    @Size(max = 100, message = "Medication name must not exceed 100 characters")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @Size(max = 50, message = "Dosage must not exceed 50 characters")
    private String dosage;

    @Size(max = 20, message = "Unit must not exceed 20 characters")
    private String unit;
}
