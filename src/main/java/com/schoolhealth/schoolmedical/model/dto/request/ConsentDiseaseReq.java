package com.schoolhealth.schoolmedical.model.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ConsentDiseaseReq {
    private Long diseaseId;
    private String note;
}
