package com.schoolhealth.schoolmedical.model.dto.request;

import com.schoolhealth.schoolmedical.entity.MedicationItem;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SendMedicationReq {
    private String pupilId;
    private String diseaseName;
    private String medicationImg;
    private String note;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<MedicationItemReq> medicationItems;
}
