package com.schoolhealth.schoolmedical.model.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MedicationItemRes {
    private Long medicationId;
    private String medicationName;
    private String unitAndUsage;
    private String medicationSchedule;
}
