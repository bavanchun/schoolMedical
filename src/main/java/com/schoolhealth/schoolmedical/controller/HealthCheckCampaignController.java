    package com.schoolhealth.schoolmedical.controller;

    import com.schoolhealth.schoolmedical.entity.HealthCheckCampaign;
    import com.schoolhealth.schoolmedical.model.dto.request.HealthCheckCampaginReq;
    import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckCampaignResponse;
    import com.schoolhealth.schoolmedical.service.HealthCheckCampaignService;
    import jakarta.validation.Valid;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/api/v1/management")
    public class HealthCheckCampaignController {

        @Autowired
        private HealthCheckCampaignService healthCheckCampaignService;

        @PostMapping()
        public ResponseEntity<?> createHealthCheckCampaign(@RequestBody @Valid HealthCheckCampaginReq campaign) {
            // Logic to create a health check campaign
            return ResponseEntity.ok(healthCheckCampaignService.saveHealthCheckCampaign(campaign));
        }
        @GetMapping
        public ResponseEntity<?> getAllHealthCheckCampaigns() {
            // Logic to get all health check campaigns
            return ResponseEntity.ok(healthCheckCampaignService.getAllHealthCheckCampaigns());
        }

    }
