package com.schoolhealth.schoolmedical.model.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SendMedicationPageReq {
    private int skip;
    private int limit;
}
