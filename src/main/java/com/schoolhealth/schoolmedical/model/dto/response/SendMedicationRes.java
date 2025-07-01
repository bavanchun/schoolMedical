package com.schoolhealth.schoolmedical.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.schoolhealth.schoolmedical.entity.enums.StatusSendMedication;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SendMedicationRes {
    private Long sendMedicationId;
    private String diseaseName;
    private String pupilId;
    private String medicationImg;
    private String unitMeasure;
    private String note;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDate;
    private String medicationSchedule;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate requestedDate;
    private StatusSendMedication status;
    private List<MedicationItemRes> medicationItems;
    private List<MedicationLogsRes> medicationLogs;
}
