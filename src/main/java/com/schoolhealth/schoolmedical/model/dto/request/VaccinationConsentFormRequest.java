package com.schoolhealth.schoolmedical.model.dto.request;

import com.schoolhealth.schoolmedical.entity.enums.ConsentFormStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VaccinationConsentFormRequest {
    @NotNull(message = "Status is required")
    private ConsentFormStatus status;

    private String notes;
}
