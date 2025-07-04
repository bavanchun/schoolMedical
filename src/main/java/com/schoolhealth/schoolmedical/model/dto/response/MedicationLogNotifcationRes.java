package com.schoolhealth.schoolmedical.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MedicationLogNotifcationRes {
    private String pupilId;
    private String pupilFirstName;
    private String pupilLastName;
    private String senderName;
    private Long sendMedicationId;
    private String diseaseName;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDate;
    private MedicationLogsRes medicationLog;
}
