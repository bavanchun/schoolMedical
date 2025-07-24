    package com.schoolhealth.schoolmedical.controller;

    import com.schoolhealth.schoolmedical.model.dto.request.HealthCheckCampaignReq;
    import com.schoolhealth.schoolmedical.model.dto.request.UpdateStatusHealthCampaignReq;
    import com.schoolhealth.schoolmedical.service.healthcheckcampaign.HealthCheckCampaignService;
    import jakarta.validation.Valid;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/api/v1/management/health-check-campaigns")
    public class HealthCheckCampaignController {

        @Autowired
        private HealthCheckCampaignService healthCheckCampaignService;

        @PostMapping()
        @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
        public ResponseEntity<?> createHealthCheckCampaign(@RequestBody @Valid HealthCheckCampaignReq campaign) {
            // Logic to create a health check campaign
            return ResponseEntity.status(HttpStatus.CREATED).body(healthCheckCampaignService.saveHealthCheckCampaign(campaign));
        }
        @GetMapping("/{campaignId}")
        @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'SCHOOL_NURSE', 'PARENT')")
        public ResponseEntity<?> getHealthCheckCampaigns(@PathVariable Long campaignId) {
            // Logic to get all health check campaigns
            System.out.println("System default timezone: " + java.time.ZoneId.systemDefault());
            return ResponseEntity.ok(healthCheckCampaignService.getHealthCheckCampaignDetailsById(campaignId));
        }
        @PatchMapping("/status/{campaignId}")
        @PreAuthorize("hasAnyRole('MANAGER')")
        public ResponseEntity<?> updateStatusHealthCheckCampaign(@RequestBody @Valid UpdateStatusHealthCampaignReq status, @PathVariable Long campaignId) {
            healthCheckCampaignService.updateStatusHealthCheckCampaign(campaignId, status.getStatusHealthCampaign());
            return ResponseEntity.ok().build();
        }
        @GetMapping("/latest")
        public ResponseEntity<?> getLatestHealthCheckCampaign() {
            return ResponseEntity.ok(healthCheckCampaignService.getLatestHealthCheckCampaign());
        }
        @GetMapping("/allHealthCheckCampaigns")
        @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'SCHOOL_NURSE', 'PARENT')")
        public ResponseEntity<?> getAllHealthCheckCampaigns() {
            return ResponseEntity.ok(healthCheckCampaignService.getAllHealthCheckCampaigns());
        }
        @GetMapping("/allHealthCheckCampaignsByPupilId/{pupilId}")
        public ResponseEntity<?> getAllHealthCheckCampaignsByPupilId(@PathVariable String pupilId) {
            return ResponseEntity.ok(healthCheckCampaignService.getHealthCheckCampaignsByPupilId(pupilId));
        }
        @PutMapping("/{campaignId}")
        public ResponseEntity<?> updateHealthCheckCampaign(@PathVariable Long campaignId, @RequestBody HealthCheckCampaignReq healthCheckCampaign) {
            return ResponseEntity.ok(healthCheckCampaignService.updateHealthCheckCampaignAndDiseases(campaignId,healthCheckCampaign));
        }
    }
