package com.schoolhealth.schoolmedical.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
public class VaccineRequest {
    @NotBlank(message = "Name vaccine should not be blank")
    @Size(max = 255, message = "Name vaccine should not exceed 255 characters")
    private String name;

    @NotBlank(message = "Manufacturer should not be blank")
    @Size(max = 255, message = "Manufacturer should not exceed 255 characters")
    private String manufacturer;

    @NotBlank(message = "Recommended age should not be blank")
    @Size(max = 100, message = "Recommended age should not exceed 100 characters")
    private String recommendedAge;

    @NotBlank(message = "Description should not be blank")
    private String description;

    @Min(value = 1, message = "Dose number should be at least 1")
    private int doseNumber;
}
