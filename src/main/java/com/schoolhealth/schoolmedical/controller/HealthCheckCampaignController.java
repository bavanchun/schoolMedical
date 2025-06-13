package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.entity.HealCheckCampaign;
import com.schoolhealth.schoolmedical.service.HealthCheckCampaignService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckCampaignController {

    @Autowired
    private HealthCheckCampaignService healthCheckCampaignService;

    @PostMapping("/health-check-campaigns")
    public HealCheckCampaign createHealthCheckCampaign(@RequestBody @Valid HealCheckCampaign campaign) {
        // Logic to create a health check campaign
        return healthCheckCampaignService.saveHealthCheckCampaign(campaign);
    }
}
