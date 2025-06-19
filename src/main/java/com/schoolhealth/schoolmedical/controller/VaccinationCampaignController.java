package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.model.dto.request.VaccinationCampaignRequest;
import com.schoolhealth.schoolmedical.model.dto.response.VaccinationCampaignResponse;
import com.schoolhealth.schoolmedical.service.vaccinationCampaign.VaccinationCampaignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vaccination-campaigns")
@RequiredArgsConstructor
public class VaccinationCampaignController {

    private final VaccinationCampaignService campaignService;

    @PostMapping
    public ResponseEntity<VaccinationCampaignResponse> createCampaign(@Valid @RequestBody VaccinationCampaignRequest request) {
        VaccinationCampaignResponse response = campaignService.createCampaign(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VaccinationCampaignResponse> getCampaignById(@PathVariable int id) {
        VaccinationCampaignResponse response = campaignService.getCampaignById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<VaccinationCampaignResponse>> getAllCampaigns() {
        List<VaccinationCampaignResponse> responses = campaignService.getAllCampaigns();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VaccinationCampaignResponse> updateCampaign(@PathVariable int id, @Valid @RequestBody VaccinationCampaignRequest request) {
        VaccinationCampaignResponse response = campaignService.updateCampaign(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampaign(@PathVariable int id) {
        campaignService.deleteCampaign(id);
        return ResponseEntity.noContent().build();
    }
}
