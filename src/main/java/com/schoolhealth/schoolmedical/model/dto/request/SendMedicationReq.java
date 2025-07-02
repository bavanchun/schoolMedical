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
    private LocalDate startDate;
    private LocalDate endDate;
    private String prescriptionImage;
    private String note;
    private List<MedicationItemReq> medicationItems;
}
