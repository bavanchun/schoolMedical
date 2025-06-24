package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.entity.enums.GradeLevel;
import com.schoolhealth.schoolmedical.service.HealthCheckConsentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Year;

@RestController
@RequestMapping("/api/v1/management/health-check/annual")
public class HealthCheckAnnualController {
    @Autowired
    private HealthCheckConsentService healthCheckConsentService;
    @GetMapping("/student/{grade}")
    public ResponseEntity<?> getHealthCheckConsentByGrade(@PathVariable String grade) {
        GradeLevel gradeLevel = GradeLevel.fromValue(grade);
        return ResponseEntity.ok(healthCheckConsentService.getHealthCheckConsentByGradeAndSchoolYear(gradeLevel, Year.now().getValue()));
    }
//    @PostMapping("/result/{consentId}")
//    public ResponseEntity<?> saveHealthCheckHistory(@RequestBody  healthCheckConsentForm) {
//        return ResponseEntity.ok(healthCheckConsentService.saveHealthCheckConsent(healthCheckConsentForm));
//    }
}
