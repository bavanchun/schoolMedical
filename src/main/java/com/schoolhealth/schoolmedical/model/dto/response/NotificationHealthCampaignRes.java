package com.schoolhealth.schoolmedical.model.dto.response;

import lombok.*;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NotificationHealthCampaignRes {
    private HealthCheckCampaignRes healthCheckCampaign;
    private PupilRes pupil;
    private Page<DiseaseHealthCheckRes> disease;
}
