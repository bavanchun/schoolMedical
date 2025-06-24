package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.model.dto.request.VaccinationHistoryRequest;
import com.schoolhealth.schoolmedical.model.dto.response.VaccinationHistoryResponse;
import com.schoolhealth.schoolmedical.service.vaccinationHistory.VaccinationHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vaccination-history")
@RequiredArgsConstructor
@Tag(name = "Vaccination History", description = "APIs for managing vaccination history")
@SecurityRequirement(name = "bearerAuth")
public class VaccinationHistoryController {

    private final VaccinationHistoryService vaccinationHistoryService;

    @GetMapping("/pupil/{pupilId}")
    @Operation(summary = "Get pupil vaccination history", description = "Get vaccination history for a specific pupil")
    @PreAuthorize("hasRole('PARENT') or hasRole('SCHOOL_NURSE') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<List<VaccinationHistoryResponse>> getPupilVaccinationHistory(
            @PathVariable String pupilId) {
        List<VaccinationHistoryResponse> history = vaccinationHistoryService.getPupilVaccinationHistory(pupilId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/{historyId}")
    @Operation(summary = "Get vaccination history by ID", description = "Get specific vaccination history record")
    @PreAuthorize("hasRole('PARENT') or hasRole('SCHOOL_NURSE') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<VaccinationHistoryResponse> getHistoryById(@PathVariable Long historyId) {
        VaccinationHistoryResponse history = vaccinationHistoryService.getHistoryById(historyId);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/declare")
    @Operation(summary = "Parent declare vaccination", description = "Parent declares vaccination done outside school")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<VaccinationHistoryResponse> createParentDeclaration(
            @Valid @RequestBody VaccinationHistoryRequest request,
            Authentication authentication) {
        String parentUserId = authentication.getName();
        VaccinationHistoryResponse response = vaccinationHistoryService.createParentDeclaration(request, parentUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{historyId}/confirm")
    @Operation(summary = "Confirm parent declaration", description = "Nurse confirms or rejects parent declaration")
    @PreAuthorize("hasRole('SCHOOL_NURSE') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<VaccinationHistoryResponse> confirmParentDeclaration(
            @PathVariable Long historyId,
            @RequestParam boolean approved) {
        VaccinationHistoryResponse response = vaccinationHistoryService.confirmParentDeclaration(historyId, approved);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{historyId}")
    @Operation(summary = "Update vaccination history", description = "Update vaccination history record")
    @PreAuthorize("hasRole('SCHOOL_NURSE') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<VaccinationHistoryResponse> updateHistory(
            @PathVariable Long historyId,
            @Valid @RequestBody VaccinationHistoryRequest request) {
        VaccinationHistoryResponse response = vaccinationHistoryService.updateHistory(historyId, request);
        return ResponseEntity.ok(response);
    }
}
