package com.schoolhealth.schoolmedical.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.schoolhealth.schoolmedical.entity.enums.StatusSendMedication;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendMedicationRes {
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
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime requestedDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate confirmedDate;
    private String prescriptionImage;
    private String note;
    private String medicationSchedule;
    private StatusSendMedication status;
    private List<MedicationItemRes> medicationItems;
    private List<MedicationLogsRes> medicationLogs;
}
