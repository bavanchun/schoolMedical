package com.schoolhealth.schoolmedical.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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
    @NotEmpty(message = "Name disease should not be null or empty")
    @Size(min = 2, max = 100, message = "Name disease must be between 2 and 100 characters")
    private String name;

    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;

    @JsonProperty("injectedVaccination")
    private boolean isInjectedVaccination;

    @Min(value = 0, message = "Dose quantity must be a non-negative integer")
    private int doseQuantity;
}
