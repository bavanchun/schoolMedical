package com.schoolhealth.schoolmedical.model.dto.request;

import com.schoolhealth.schoolmedical.entity.enums.StatusMedLogs;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MedicationLogReq {
    @NotNull
    private Long sendMedicationId;

    private StatusMedLogs status;
    @NotBlank
    private String note;
}
