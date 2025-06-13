package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.entity.HealCheckCampaign;

public interface HealthCheckCampaignService {
    HealCheckCampaign getHealthCheckCampaignById();
    HealCheckCampaign saveHealthCheckCampaign(HealCheckCampaign healCheckCampaign);
}
