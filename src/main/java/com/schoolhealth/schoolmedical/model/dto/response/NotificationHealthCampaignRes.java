package com.schoolhealth.schoolmedical.model.dto.response;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NotificationHealthCampaignRes {
    private HealthCheckCampaignRes healthCheckCampaign;
    private PupilRes pupil;
}
