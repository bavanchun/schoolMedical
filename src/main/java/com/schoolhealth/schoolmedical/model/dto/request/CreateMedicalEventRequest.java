package com.schoolhealth.schoolmedical.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateMedicalEventRequest {

    @NotBlank(message = "Pupil ID is required")
    private String pupilId;

    @NotBlank(message = "Injury description is required")
    @Size(max = 2000, message = "Injury description must not exceed 2000 characters")
    private String injuryDescription;

    @NotBlank(message = "Treatment description is required")
    @Size(max = 2000, message = "Treatment description must not exceed 2000 characters")
    private String treatmentDescription;

    @Size(max = 5000, message = "Detailed information must not exceed 5000 characters")
    private String detailedInformation;

    // Optional equipment IDs used in treatment
    private List<Long> equipmentIds;

    // Optional medication IDs used in treatment
    private List<Long> medicationIds;
}
