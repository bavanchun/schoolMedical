package com.schoolhealth.schoolmedical.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.schoolhealth.schoolmedical.entity.enums.VaccinationCampaignStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VaccinationCampaignRequest {
    @NotBlank(message = "Title is required")
    private String titleCampaign;

    @NotNull(message = "Vaccine ID is required")
    private Integer vaccineId;

    @NotNull(message = "Disease ID is required")
    private Integer diseaseId;

    @NotNull(message = "Dose number is required")
    @Min(value = 1, message = "Dose number must be >= 1")
    private Integer doseNumber;

    @NotNull(message = "Start date is required")
    @FutureOrPresent
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDate;

    @NotNull(message = "Form deadline is required")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate formDeadline;

    private String notes;
    private VaccinationCampaignStatus status;
}
