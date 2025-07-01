package com.schoolhealth.schoolmedical.model.dto.request;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SendMedicationReq {
    private String pupilId;
    private String diseaseName;
    private String medicationImg;
    private String unitMeasure;
    private String note;
    private LocalDate startDate;
    private LocalDate endDate;
    private String medicationSchedule;
}
