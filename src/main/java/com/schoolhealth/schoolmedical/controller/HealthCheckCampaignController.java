    package com.schoolhealth.schoolmedical.controller;

    import com.schoolhealth.schoolmedical.model.dto.request.HealthCheckCampaginReq;
    import com.schoolhealth.schoolmedical.model.dto.request.UpdateStatusHealthCampaignReq;
    import com.schoolhealth.schoolmedical.service.HealthCheckCampaignService;
    import jakarta.validation.Valid;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/api/v1/management")
    public class HealthCheckCampaignController {

        @Autowired
        private HealthCheckCampaignService healthCheckCampaignService;

        @PostMapping()
        public ResponseEntity<?> createHealthCheckCampaign(@RequestBody @Valid HealthCheckCampaginReq campaign) {
            System.out.println(campaign);
            System.out.println("sasdfsf");
            // Logic to create a health check campaign
            return ResponseEntity.ok(healthCheckCampaignService.saveHealthCheckCampaign(campaign));
        }
        @GetMapping("/{campaignId}")
        public ResponseEntity<?> getHealthCheckCampaigns(@PathVariable Long campaignId) {
            // Logic to get all health check campaigns
            return ResponseEntity.ok(healthCheckCampaignService.getHealthCheckCampaignDetailsById(campaignId));
        }
        @PatchMapping("/{campaignId}/status")
        public ResponseEntity<?> updateStatusHealthCheckCampaign(@RequestBody @Valid UpdateStatusHealthCampaignReq status, @PathVariable Long campaignId) {
            healthCheckCampaignService.updateStatusHealthCheckCampaign(campaignId, status.getStatusHealthCampaign());
            return ResponseEntity.ok().build();
        }


    }
