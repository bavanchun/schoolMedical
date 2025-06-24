package com.schoolhealth.schoolmedical.model.dto.request;

import java.time.LocalDateTime;
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

    @Positive(message = "Dose number must be positive")
    private int doseNumber;

    @NotNull(message = "Vaccination date is required")
    private LocalDateTime vaccinatedAt;

    private String notes;

    private VaccinationSource source = VaccinationSource.PARENT_DECLARATION;
}
