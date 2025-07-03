package com.schoolhealth.schoolmedical.model.dto.request;

import com.schoolhealth.schoolmedical.entity.enums.StatusMedLogs;
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
    @NotNull
    private StatusMedLogs status;
}
