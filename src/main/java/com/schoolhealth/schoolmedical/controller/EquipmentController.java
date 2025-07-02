package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.model.dto.request.EquipmentRequest;
import com.schoolhealth.schoolmedical.model.dto.response.EquipmentResponse;
import com.schoolhealth.schoolmedical.service.equipment.EquipmentService;
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
@RequestMapping("/api/v1/equipment")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EquipmentController {

    private final EquipmentService equipmentService;

    /**
     * Create new equipment
     */
    @PostMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<EquipmentResponse> createEquipment(@Valid @RequestBody EquipmentRequest request) {
        EquipmentResponse response = equipmentService.createEquipment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update existing equipment
     */
    @PutMapping("/{equipmentId}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<EquipmentResponse> updateEquipment(
            @PathVariable Long equipmentId,
            @Valid @RequestBody EquipmentRequest request) {
        EquipmentResponse response = equipmentService.updateEquipment(equipmentId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Get equipment by ID
     */
    @GetMapping("/{equipmentId}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN') or hasRole('SCHOOL_NURSE')")
    public ResponseEntity<EquipmentResponse> getEquipmentById(@PathVariable Long equipmentId) {
        EquipmentResponse response = equipmentService.getEquipmentById(equipmentId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all equipment (with optional pagination)
     */
    @GetMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN') or hasRole('SCHOOL_NURSE')")
    public ResponseEntity<?> getAllEquipment(
            @RequestParam(required = false, defaultValue = "false") boolean paginated,
            @PageableDefault(size = 20) Pageable pageable) {

        if (paginated) {
            Page<EquipmentResponse> response = equipmentService.getAllEquipmentWithPagination(pageable);
            return ResponseEntity.ok(response);
        } else {
            List<EquipmentResponse> response = equipmentService.getAllEquipment();
            return ResponseEntity.ok(response);
        }
    }

    /**
     * Search equipment by name
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN') or hasRole('SCHOOL_NURSE')")
    public ResponseEntity<List<EquipmentResponse>> searchEquipmentByName(@RequestParam String name) {
        List<EquipmentResponse> response = equipmentService.searchEquipmentByName(name);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete equipment (soft delete)
     */
    @DeleteMapping("/{equipmentId}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long equipmentId) {
        equipmentService.deleteEquipment(equipmentId);
        return ResponseEntity.noContent().build();
    }
}
