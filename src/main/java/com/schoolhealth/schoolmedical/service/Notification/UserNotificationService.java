package com.schoolhealth.schoolmedical.service.Notification;

import com.schoolhealth.schoolmedical.entity.UserNotification;

import java.util.List;

public interface UserNotificationService {
    List<UserNotification> saveAllUserNotifications(List<UserNotification> userNotification);
}