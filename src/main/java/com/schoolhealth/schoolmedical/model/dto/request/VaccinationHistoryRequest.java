package com.schoolhealth.schoolmedical.model.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.schoolhealth.schoolmedical.entity.enums.VaccinationSource;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VaccinationHistoryRequest {
    @NotNull(message = "Pupil ID is required")
    private String pupilId;

    @NotNull(message = "Vaccine ID is required")
    private Long vaccineId;

    @NotNull(message = "Disease ID is required")
    private Long diseaseId;

    @NotNull(message = "Vaccination date is required")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate vaccinatedAt;

    private String notes;

    private VaccinationSource source = VaccinationSource.PARENT_DECLARATION;
}
