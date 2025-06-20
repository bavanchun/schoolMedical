package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.entity.HealthCheckCampaign;
import com.schoolhealth.schoolmedical.model.dto.request.HealthCheckCampaginReq;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckCampaignFlatData;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckCampaignRes;

import java.util.List;

public interface HealthCheckCampaignService {

    HealthCheckCampaignRes saveHealthCheckCampaign(HealthCheckCampaginReq healthCheckCampaign);
    List<HealthCheckCampaignFlatData> getHealthCheckCampaignDetails(Long campaignId);

}
