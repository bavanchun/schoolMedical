package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.entity.HealthCheckHistory;
import com.schoolhealth.schoolmedical.entity.UserNotification;
import com.schoolhealth.schoolmedical.entity.enums.GradeLevel;
import com.schoolhealth.schoolmedical.model.dto.request.HealthCheckHistoryReq;
import com.schoolhealth.schoolmedical.model.dto.request.NotificationReq;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckHistoryRes;
import com.schoolhealth.schoolmedical.service.HealthCheckConsentService;
import com.schoolhealth.schoolmedical.service.HealthCheckHistory.HealthCheckHistoryService;
import com.schoolhealth.schoolmedical.service.Notification.UserNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Year;

@RestController
@RequestMapping("/api/v1/management/health-check/annual")
public class HealthCheckAnnualController {
    @Autowired
    private HealthCheckConsentService healthCheckConsentService;

    @Autowired
    private HealthCheckHistoryService healthCheckHistoryService;

    @Autowired
    private UserNotificationService userNotificationService;
    @GetMapping("/student/{grade}")
    public ResponseEntity<?> getHealthCheckConsentByGrade(@PathVariable String grade) {
        GradeLevel gradeLevel = GradeLevel.fromValue(grade);
        return ResponseEntity.ok(healthCheckConsentService.getHealthCheckConsentByGradeAndSchoolYear(gradeLevel, Year.now().getValue()));
    }
    @PostMapping("/result/{consentId}")
    public ResponseEntity<?> saveHealthCheckHistory(@RequestBody HealthCheckHistoryReq healthCheckHistoryReq, @PathVariable Long consentId) {
        return ResponseEntity.ok(healthCheckHistoryService.saveHealthCheckHistory(healthCheckHistoryReq, consentId));
    }
    @GetMapping("/result")
    public ResponseEntity<?> getHealthCheckHistoryByPupilIdAndSchoolYear(@RequestParam String pupilId, @RequestParam int schoolYear) {
        HealthCheckHistoryRes healthCheckHistory = healthCheckHistoryService.getHealthCheckHistoryByPupilIdAndSchoolYear(pupilId, schoolYear);
        return ResponseEntity.ok(healthCheckHistory);
    }
    @GetMapping("/notification")
    public ResponseEntity<?> getHealthCheckNotificationByPupilIdAndSchoolYear(@RequestBody NotificationReq notificationReq) {
        return ResponseEntity.ok(userNotificationService.getNotificationHealthCampaignByIdAndPupilAndDisease(notificationReq.getSourceId(), notificationReq.getPupilId(), notificationReq.getTypeNotification()));
    }
}
