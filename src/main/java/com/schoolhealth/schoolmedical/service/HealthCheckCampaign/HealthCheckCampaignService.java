package com.schoolhealth.schoolmedical.service.HealthCheckCampaign;

import com.schoolhealth.schoolmedical.entity.HealthCheckCampaign;
import com.schoolhealth.schoolmedical.entity.enums.StatusHealthCampaign;
import com.schoolhealth.schoolmedical.model.dto.request.HealthCheckCampaignReq;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckCampaginByIdRes;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckCampaignRes;

import java.util.List;

public interface HealthCheckCampaignService {

    HealthCheckCampaignRes saveHealthCheckCampaign(HealthCheckCampaignReq healthCheckCampaign);
    HealthCheckCampaignRes getHealthCheckCampaignDetailsById(Long campaignId);
    void updateStatusHealthCheckCampaign(Long campaignId, StatusHealthCampaign statusHealthCampaign);
    HealthCheckCampaignRes getLatestHealthCheckCampaign();
    List<HealthCheckCampaignRes> getAllHealthCheckCampaigns();
    HealthCheckCampaignRes getHealthCheckCampaignById(Long campaignId);
    HealthCheckCampaign getHealthCheckCampaignEntityById(Long campaignId);
    HealthCheckCampaignRes updateHealthCheckCampaignAndDiseases(Long campaignId ,HealthCheckCampaignReq healthCheckCampaign);
    List<HealthCheckCampaginByIdRes> getHealthCheckCampaignsByPupilId(String pupilId);
}
