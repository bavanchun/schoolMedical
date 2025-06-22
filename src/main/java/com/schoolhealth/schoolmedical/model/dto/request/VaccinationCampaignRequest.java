package com.schoolhealth.schoolmedical.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.schoolhealth.schoolmedical.entity.enums.VaccinationCampaignStatus;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
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
    @NotBlank(message = "Campaign title is required")
    private String titleCampaign;

    @NotNull(message = "Disease ID is required")
    private Integer diseaseId;

    @NotNull(message = "Vaccine ID is required")
    private Integer vaccineId;

    @NotNull(message = "Dose number is required")
    @Min(value = 1, message = "Dose number must be >= 1")
    private Integer doseNumber;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be in the present or future")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDate;

    @NotNull(message = "Form deadline is required")
    @FutureOrPresent(message = "Form deadline must be in the present or future")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate formDeadline;

    private String notes;
//    private VaccinationCampaignStatus status;

    @JsonIgnore
    @AssertTrue(message = "Date sequence is invalid. Required: Start Date < Form Deadline < End Date.")
    private boolean isDateSequenceValid() {
        // check if startDate, formDeadline, and endDate are not null
        if (startDate == null || formDeadline == null || endDate == null) {
            return true;
        }
        // check if startDate is before formDeadline and formDeadline is before endDate
//        return startDate.isBefore(formDeadline) && formDeadline.isBefore(endDate);
        return formDeadline.isBefore(startDate) && startDate.isBefore(endDate);
    }
}
