package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.entity.HealthCheckCampaign;
import com.schoolhealth.schoolmedical.entity.enums.GradeLevel;
import com.schoolhealth.schoolmedical.entity.enums.StatusHealthCampaign;
import com.schoolhealth.schoolmedical.model.dto.request.HealthCheckCampaginReq;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckCampaginByIdRes;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckCampaignFlatData;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckCampaignRes;
import com.schoolhealth.schoolmedical.model.dto.response.LatestHealthCheckCampaignRes;

import java.util.List;

public interface HealthCheckCampaignService {

    HealthCheckCampaignRes saveHealthCheckCampaign(HealthCheckCampaginReq healthCheckCampaign);
    HealthCheckCampaignRes getHealthCheckCampaignDetailsById(Long campaignId);
    void updateStatusHealthCheckCampaign(Long campaignId, StatusHealthCampaign statusHealthCampaign);
    HealthCheckCampaignRes getLatestHealthCheckCampaign();
    List<HealthCheckCampaignRes> getAllHealthCheckCampaigns();
    HealthCheckCampaignRes getHealthCheckCampaignById(Long campaignId);
    HealthCheckCampaign getHealthCheckCampaignEntityById(Long campaignId);
    HealthCheckCampaignRes updateHealthCheckCampaignAndDiseases(HealthCheckCampaginReq healthCheckCampaign);
    List<HealthCheckCampaginByIdRes> getHealthCheckCampaignsByPupilId(String pupilId);
}
