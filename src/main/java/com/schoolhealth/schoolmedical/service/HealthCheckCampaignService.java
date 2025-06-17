package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.entity.HealthCheckCampaign;
import com.schoolhealth.schoolmedical.model.dto.request.HealthCheckCampaginReq;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckCampaignResponse;

import java.util.List;

public interface HealthCheckCampaignService {
    HealthCheckCampaign getHealthCheckCampaignById();
    HealthCheckCampaignResponse saveHealthCheckCampaign(HealthCheckCampaginReq healthCheckCampaign);
    List<HealthCheckCampaignResponse> getAllHealthCheckCampaigns();
}
