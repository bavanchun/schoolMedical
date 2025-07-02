package com.schoolhealth.schoolmedical.model.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MedicationItemReq {
    private String medicationName;
    private String unitAndUsage;
    private String medicationSchedule;
}
