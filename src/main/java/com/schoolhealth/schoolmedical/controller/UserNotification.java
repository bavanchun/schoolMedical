package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.model.dto.request.NotificationReq;
import com.schoolhealth.schoolmedical.service.Notification.UserNotificationService;
import com.schoolhealth.schoolmedical.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/notification/")
public class UserNotification {
    @Autowired
    private UserNotificationService userNotificationService;
    @GetMapping("health-check")
    public ResponseEntity<?> getHealthCheckNotificationByPupilIdAndSchoolYear(@RequestBody NotificationReq notificationReq) {
        return ResponseEntity.ok(userNotificationService.getNotificationCampaign(notificationReq.getSourceId(), notificationReq.getPupilId(), notificationReq.getTypeNotification()));
    }

}
