package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.model.dto.request.CreateMedicalEventRequest;
import com.schoolhealth.schoolmedical.model.dto.response.MedicalEventResponse;
import com.schoolhealth.schoolmedical.service.medicalevent.MedicalEventService;
import com.schoolhealth.schoolmedical.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/api/v1/medical-events")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Medical Event Management", description = "APIs for managing medical events (immutable records)")
@SecurityRequirement(name = "bearerAuth")
public class MedicalEventController {

    private final MedicalEventService medicalEventService;
    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasAnyRole('SCHOOL_NURSE', 'MANAGER', 'ADMIN')")
    @Operation(summary = "Create a new medical event", description = "Medical events are immutable - create new event for corrections")
    public ResponseEntity<MedicalEventResponse> createMedicalEvent(
            @Valid @RequestBody CreateMedicalEventRequest request,
            HttpServletRequest httpRequest) {

        String createdBy = userService.getCurrentUserId(httpRequest);
        MedicalEventResponse response = medicalEventService.createMedicalEvent(request, createdBy);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }



    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SCHOOL_NURSE', 'MANAGER', 'ADMIN', 'PARENT')")
    @Operation(summary = "Get medical event by ID", description = "Access control based on user role")
    public ResponseEntity<MedicalEventResponse> getMedicalEventById(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {

        String userId = userService.getCurrentUserId(httpRequest);
        MedicalEventResponse response = medicalEventService.getMedicalEventById(id, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @Operation(summary = "Get all medical events", description = "Only managers and admins can view all medical events")
    public ResponseEntity<Page<MedicalEventResponse>> getAllMedicalEvents(
            @PageableDefault(size = 20) Pageable pageable,
            @RequestParam(required = false) @Parameter(description = "Search term for filtering") String search) {

        Page<MedicalEventResponse> response = medicalEventService.getAllMedicalEvents(pageable, search);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pupil/{pupilId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'PARENT')")
    @Operation(summary = "Get medical events by pupil ID", description = "Managers/admins can access any pupil, parents only their children")
    public ResponseEntity<List<MedicalEventResponse>> getMedicalEventsByPupilId(
            @PathVariable String pupilId,
            HttpServletRequest httpRequest) {

        String requestingUserId = userService.getCurrentUserId(httpRequest);
        List<MedicalEventResponse> response = medicalEventService.getMedicalEventsByPupilId(pupilId, requestingUserId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/grade/{gradeLevel}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @Operation(summary = "Get medical events by grade level", description = "Only managers and admins can view events by grade")
    public ResponseEntity<List<MedicalEventResponse>> getMedicalEventsByGradeLevel(
            @PathVariable String gradeLevel,
            HttpServletRequest httpRequest) {

        String requestingUserId = userService.getCurrentUserId(httpRequest);
        List<MedicalEventResponse> response = medicalEventService.getMedicalEventsByGradeLevel(gradeLevel, requestingUserId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/parent/my-children")
    @PreAuthorize("hasRole('PARENT')")
    @Operation(summary = "Get medical events for parent's children", description = "Parents can only view their own children's medical events")
    public ResponseEntity<List<MedicalEventResponse>> getMedicalEventsForMyChildren(
            @RequestParam(required = false, defaultValue = "2024") @Parameter(description = "Year filter") int year,
            HttpServletRequest httpRequest) {

        String parentId = userService.getCurrentUserId(httpRequest);
        List<MedicalEventResponse> response = medicalEventService.getMedicalEventsForParentByYear(parentId, year);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @Operation(summary = "Soft delete a medical event", description = "Only managers and admins can delete medical events")
    public ResponseEntity<Void> deleteMedicalEvent(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {

        String deletedBy = userService.getCurrentUserId(httpRequest);
        medicalEventService.deleteMedicalEvent(id, deletedBy);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @Operation(summary = "Get medical event statistics", description = "Only managers and admins can view statistics")
    public ResponseEntity<MedicalEventService.MedicalEventStatistics> getMedicalEventStatistics() {

        MedicalEventService.MedicalEventStatistics statistics = medicalEventService.getMedicalEventStatistics();
        return ResponseEntity.ok(statistics);
    }
}
