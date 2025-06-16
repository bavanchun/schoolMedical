package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.entity.HealthCheckCampaign;
import com.schoolhealth.schoolmedical.model.dto.request.HealthCheckCampaginReq;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckCampaignResponse;
import com.schoolhealth.schoolmedical.service.HealthCheckCampaignService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HealthCheckCampaignController {

    @Autowired
    private HealthCheckCampaignService healthCheckCampaignService;

    @PostMapping("/health-check-campaigns")
    public HealthCheckCampaignResponse createHealthCheckCampaign(@RequestBody @Valid HealthCheckCampaginReq campaign) {
        // Logic to create a health check campaign
        return healthCheckCampaignService.saveHealthCheckCampaign(campaign);
    }
}
