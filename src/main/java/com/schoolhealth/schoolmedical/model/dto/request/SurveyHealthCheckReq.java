package com.schoolhealth.schoolmedical.model.dto.request;

import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SurveyHealthCheckReq {
    private Long campaignId;
    private String pupilId;
    Set<Long> diseaseId;
}
