package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.entity.enums.GradeLevel;
import com.schoolhealth.schoolmedical.model.dto.request.HealthCheckHistoryReq;
import com.schoolhealth.schoolmedical.model.dto.request.SurveyHealthCheckReq;
import com.schoolhealth.schoolmedical.model.dto.request.UpdateHealthCheckHistoryReq;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckHistoryRes;
import com.schoolhealth.schoolmedical.service.HealthCheckConsentService;
import com.schoolhealth.schoolmedical.service.HealthCheckDiseaseService;
import com.schoolhealth.schoolmedical.service.HealthCheckHistory.HealthCheckHistoryService;
import com.schoolhealth.schoolmedical.service.consentDisease.ConsentDiseaseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.List;

@RestController
@RequestMapping("/api/v1/management/health-check/annual")
public class HealthCheckAnnualController {
    @Autowired
    private HealthCheckConsentService healthCheckConsentService;

    @Autowired
    private HealthCheckHistoryService healthCheckHistoryService;
    @Autowired
    private ConsentDiseaseService consentDiseaseService;

    @GetMapping("/student/{grade}")
    @PreAuthorize("hasAnyRole('SCHOOL_NURSE', 'MANAGER','ADMIN')")
    public ResponseEntity<?> getHealthCheckConsentByGrade(@PathVariable String grade) {
        GradeLevel gradeLevel = GradeLevel.fromValue(grade);
        return ResponseEntity.ok(healthCheckConsentService.getHealthCheckConsentByGradeAndSchoolYear(gradeLevel));
    }
    @PostMapping("/result/{consentId}")
    @PreAuthorize("hasAnyRole('SCHOOL_NURSE', 'MANAGER','ADMIN')")
    public ResponseEntity<?> saveHealthCheckHistory(@RequestBody @Valid HealthCheckHistoryReq healthCheckHistoryReq, @PathVariable Long consentId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(healthCheckHistoryService.saveHealthCheckHistory(healthCheckHistoryReq, consentId));
    }
    @GetMapping("/result")
    @PreAuthorize("hasAnyRole('PARENT','SCHOOL_NURSE', 'MANAGER','ADMIN')")
    public ResponseEntity<?> getHealthCheckHistoryByPupilIdAndSchoolYear(@RequestParam String pupilId, @RequestParam int schoolYear) {
        List<HealthCheckHistoryRes> healthCheckHistory = healthCheckHistoryService.getHealthCheckHistoryByPupilIdAndSchoolYear(pupilId, schoolYear);
        return ResponseEntity.ok(healthCheckHistory);
    }
    @PatchMapping("/disease")
    @PreAuthorize("hasAnyRole('PARENT','ADMIN')")
    public ResponseEntity<?> updateConsentDisease(@RequestBody @Valid SurveyHealthCheckReq survey) {
        consentDiseaseService.updateConsentDisease(survey);
        return ResponseEntity.ok().body("Health check disease status updated successfully");
    }
    @GetMapping("/result/{consentId}")
    @PreAuthorize("hasAnyRole('SCHOOL_NURSE', 'MANAGER','ADMIN')")
    public ResponseEntity<?> getHealthCheckHistoryByConsentId(@PathVariable Long consentId) {
        return ResponseEntity.ok(healthCheckConsentService.getHealthCheckConsentById(consentId));
    }
    @PutMapping("/result/{consentId}")
    @PreAuthorize("hasAnyRole('SCHOOL_NURSE', 'MANAGER','ADMIN')")
    public ResponseEntity<?> updateHealthCheckHistory(@RequestBody UpdateHealthCheckHistoryReq healthCheckHistoryReq, @PathVariable Long consentId) {
        return ResponseEntity.ok(healthCheckHistoryService.updateHealthCheckHistory(healthCheckHistoryReq, consentId));
    }
}
