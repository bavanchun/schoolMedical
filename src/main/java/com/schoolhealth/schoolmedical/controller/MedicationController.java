package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.model.dto.request.MedicationRequest;
import com.schoolhealth.schoolmedical.model.dto.response.MedicationResponse;
import com.schoolhealth.schoolmedical.service.medication.MedicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medication")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MedicationController {

    private final MedicationService medicationService;

    /**
     * Create new medication
     */
    @PostMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<MedicationResponse> createMedication(@Valid @RequestBody MedicationRequest request) {
        MedicationResponse response = medicationService.createMedication(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update existing medication
     */
    @PutMapping("/{medicationId}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<MedicationResponse> updateMedication(
            @PathVariable Long medicationId,
            @Valid @RequestBody MedicationRequest request) {
        MedicationResponse response = medicationService.updateMedication(medicationId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get medication by ID
     */
    @GetMapping("/{medicationId}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN') or hasRole('SCHOOL_NURSE')")
    public ResponseEntity<MedicationResponse> getMedicationById(@PathVariable Long medicationId) {
        MedicationResponse response = medicationService.getMedicationById(medicationId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all medication (with optional pagination)
     */
    @GetMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN') or hasRole('SCHOOL_NURSE')")
    public ResponseEntity<?> getAllMedication(
            @RequestParam(required = false, defaultValue = "false") boolean paginated,
            @PageableDefault(size = 20) Pageable pageable) {

        if (paginated) {
            Page<MedicationResponse> response = medicationService.getAllMedicationWithPagination(pageable);
            return ResponseEntity.ok(response);
        } else {
            List<MedicationResponse> response = medicationService.getAllMedication();
            return ResponseEntity.ok(response);
        }
    }

    /**
     * Search medication by name
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN') or hasRole('SCHOOL_NURSE')")
    public ResponseEntity<List<MedicationResponse>> searchMedicationByName(@RequestParam String name) {
        List<MedicationResponse> response = medicationService.searchMedicationByName(name);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete medication (soft delete)
     */
    @DeleteMapping("/{medicationId}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMedication(@PathVariable Long medicationId) {
        medicationService.deleteMedication(medicationId);
        return ResponseEntity.noContent().build();
    }
}
