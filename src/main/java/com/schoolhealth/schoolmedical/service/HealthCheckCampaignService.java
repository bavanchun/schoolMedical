package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.entity.HealthCheckCampaign;
import com.schoolhealth.schoolmedical.model.dto.request.HealthCheckCampaginReq;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckCampaignResponse;

public interface HealthCheckCampaignService {
    HealthCheckCampaign getHealthCheckCampaignById();
    HealthCheckCampaignResponse saveHealthCheckCampaign(HealthCheckCampaginReq healthCheckCampaign);
}
