package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.entity.enums.VaccinationCampaignStatus;
import com.schoolhealth.schoolmedical.model.dto.request.VaccinationCampaignRequest;
import com.schoolhealth.schoolmedical.model.dto.response.VaccinationCampaignResponse;
import com.schoolhealth.schoolmedical.service.vaccinationCampaign.VaccinationCampaignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vaccination-campaigns")
@RequiredArgsConstructor
@Tag(name = "Vaccination Campaign", description = "APIs for managing vaccination campaigns")
@SecurityRequirement(name = "bearerAuth")
public class VaccinationCampaignController {

    private final VaccinationCampaignService vaccinationCampaignService;

    @PostMapping
    @Operation(summary = "Create new vaccination campaign", description = "Create a new vaccination campaign (Manager only)")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<VaccinationCampaignResponse> createCampaign(
            @Valid @RequestBody VaccinationCampaignRequest request) {
        VaccinationCampaignResponse response = vaccinationCampaignService.createCampaign(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{campaignId}")
    @Operation(summary = "Update vaccination campaign", description = "Update campaign information (Manager only, PENDING status only)")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<VaccinationCampaignResponse> updateCampaign(
            @PathVariable Long campaignId,
            @Valid @RequestBody VaccinationCampaignRequest request) {
        VaccinationCampaignResponse response = vaccinationCampaignService.updateCampaign(campaignId, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{campaignId}/publish")
    @Operation(summary = "Publish vaccination campaign", description = "Publish campaign and send notifications to parents (Manager only)")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<String> publishCampaign(@PathVariable Long campaignId) {
        vaccinationCampaignService.publishCampaign(campaignId);
        return ResponseEntity.ok("Campaign published successfully and notifications sent to parents.");
    }

    @PatchMapping("/{campaignId}/status")
    @Operation(summary = "Update campaign status", description = "Update campaign status (Manager for IN_PROGRESS, School Nurse for COMPLETED)")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN') or hasRole('SCHOOL_NURSE')")
    public ResponseEntity<String> updateCampaignStatus(
            @PathVariable Long campaignId,
            @RequestParam VaccinationCampaignStatus status) {
        vaccinationCampaignService.updateStatus(campaignId, status);
        return ResponseEntity.ok("Campaign status updated to " + status);
    }
}
