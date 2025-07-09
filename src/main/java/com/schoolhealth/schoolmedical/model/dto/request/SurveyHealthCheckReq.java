package com.schoolhealth.schoolmedical.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SurveyHealthCheckReq {
    @NotNull
    private Long campaignId;
    @NotNull
    private String pupilId;
    private List<Long> diseaseId;
}
