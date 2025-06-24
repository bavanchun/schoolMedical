package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.model.dto.request.VaccinationConsentFormRequest;
import com.schoolhealth.schoolmedical.model.dto.response.VaccinationConsentFormResponse;
import com.schoolhealth.schoolmedical.service.vaccinationConsentForm.VaccinationConsentFormService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/consent-forms")
@RequiredArgsConstructor
@Tag(name = "Vaccination Consent Form", description = "APIs for managing vaccination consent forms")
@SecurityRequirement(name = "bearerAuth")
public class VaccinationConsentFormController {

    private final VaccinationConsentFormService consentFormService;

    @GetMapping("/my-forms")
    @Operation(summary = "Get my consent forms", description = "Parent gets list of consent forms for their children")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<List<VaccinationConsentFormResponse>> getMyConsentForms(Authentication authentication) {
        String parentUserId = authentication.getName();
        List<VaccinationConsentFormResponse> forms = consentFormService.getMyConsentForms(parentUserId);
        return ResponseEntity.ok(forms);
    }

    @PatchMapping("/{formId}/respond")
    @Operation(summary = "Parent respond to consent form", description = "Parent responds with APPROVED or REJECTED")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<VaccinationConsentFormResponse> parentRespond(
            @PathVariable Long formId,
            @Valid @RequestBody VaccinationConsentFormRequest request,
            Authentication authentication) {
        String parentUserId = authentication.getName();
        VaccinationConsentFormResponse response = consentFormService.parentRespond(formId, parentUserId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/campaign/{campaignId}")
    @Operation(summary = "Get consent forms by campaign", description = "Get list of consent forms for a campaign (for nurse)")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN') or hasRole('SCHOOL_NURSE')")
    public ResponseEntity<List<VaccinationConsentFormResponse>> getConsentFormsByCampaign(
            @PathVariable Long campaignId,
            @RequestParam(required = false) String status) {
        List<VaccinationConsentFormResponse> forms;
        if (status != null) {
            forms = consentFormService.getConsentFormsByCampaignAndStatus(campaignId, status);
        } else {
            forms = consentFormService.getApprovedPupilsByCampaign(campaignId);
        }
        return ResponseEntity.ok(forms);
    }

    @PatchMapping("/{formId}/status")
    @Operation(summary = "Nurse update consent form status", description = "Nurse updates status to INJECTED or NO_SHOW")
    @PreAuthorize("hasRole('SCHOOL_NURSE')")
    public ResponseEntity<VaccinationConsentFormResponse> nurseUpdateStatus(
            @PathVariable Long formId,
            @Valid @RequestBody VaccinationConsentFormRequest request) {
        VaccinationConsentFormResponse response = consentFormService.nurseUpdateStatus(formId, request);
        return ResponseEntity.ok(response);
    }
}
