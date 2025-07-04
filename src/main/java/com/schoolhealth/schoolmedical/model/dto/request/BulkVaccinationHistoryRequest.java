package com.schoolhealth.schoolmedical.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkVaccinationHistoryRequest {

    @NotNull(message = "Pupil ID is required")
    private String pupilId;

    @NotEmpty(message = "Vaccination history list cannot be empty")
    @Valid
    private List<VaccinationHistoryItem> vaccinationHistories;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class VaccinationHistoryItem {
        @NotNull(message = "Vaccine ID is required")
        private Long vaccineId;

        @NotNull(message = "Disease ID is required")
        private Long diseaseId;

        @NotEmpty(message = "At least one dose is required")
        @Valid
        private List<DoseInfo> doses;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DoseInfo {
        @NotNull(message = "Vaccination date is required")
        @JsonFormat(pattern = "dd-MM-yyyy")
        private java.time.LocalDate vaccinatedAt;

        private String notes;

        private Integer doseNumber; // Mũi thứ mấy (1, 2, 3...)
    }
}
