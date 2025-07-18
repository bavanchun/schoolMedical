package com.schoolhealth.schoolmedical.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class SendMedicationSimpleRes {
    private String pupilId;
    private String pupilFirstName;
    private String pupilLastName;
    private String gradeName;
    private Long sendMedicationId;
    private String diseaseName;
    private String medicationName;
    private String unitAndUsage;
}
