package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.model.dto.request.NotificationReq;
import com.schoolhealth.schoolmedical.service.Notification.UserNotificationService;
import com.schoolhealth.schoolmedical.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notification")
public class UserNotification {
    @Autowired
    private UserNotificationService userNotificationService;
    @Autowired
    private UserService userService; // Assuming UserService is defined elsewhere
    @GetMapping("/health-check")
    public ResponseEntity<?> getHealthCheckNotificationByPupilIdAndSchoolYear(@RequestBody @Valid NotificationReq notificationReq) {
        return ResponseEntity.ok(userNotificationService.getNotificationCampaign(notificationReq.getSourceId(), notificationReq.getPupilId(), notificationReq.getTypeNotification()));
    }
    @GetMapping()
    public ResponseEntity<?> getAllUserNotifications(HttpServletRequest request) {
        String parentId = userService.getCurrentUserId(request); // Assuming this method retrieves the current user's ID
        return ResponseEntity.ok(userNotificationService.getAllNotificationsByParentId(parentId));
}

}
