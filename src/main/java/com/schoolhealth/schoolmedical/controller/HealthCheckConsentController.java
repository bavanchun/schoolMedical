package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.entity.Grade;
import com.schoolhealth.schoolmedical.entity.enums.GradeLevel;
import com.schoolhealth.schoolmedical.service.HealthCheckConsentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Year;

@RestController
@RequestMapping("/api/v1/management/health-check-consent")
public class HealthCheckConsentController {
    @Autowired
    private HealthCheckConsentService healthCheckConsentService;
    @GetMapping("/grade/{grade}")
    public ResponseEntity<?> getHealthCheckConsentByGrade(@PathVariable String grade) {
        GradeLevel gradeLevel = GradeLevel.fromValue(grade);
        return ResponseEntity.ok(healthCheckConsentService.getHealthCheckConsentByGradeAndSchoolYear(gradeLevel, Year.now().getValue()));
    }
}
