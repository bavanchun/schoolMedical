package com.schoolhealth.schoolmedical.model.dto.request;

import com.schoolhealth.schoolmedical.entity.enums.ConsentFormStatus;
import jakarta.validation.constraints.NotNull;

public class VaccinationConsentFormRequest {
    @NotNull(message = "Status is required")
    private ConsentFormStatus status;

    private String notes;
}
