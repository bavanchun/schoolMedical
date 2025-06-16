package com.schoolhealth.schoolmedical.service.Notification;

import com.schoolhealth.schoolmedical.entity.UserNotification;
import com.schoolhealth.schoolmedical.repository.NotificationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserNotificationImpl implements UserNotificationService {
    @Autowired
    private NotificationRepo notificationRepo;
    @Override
    public List<UserNotification> saveAllUserNotifications(List<UserNotification> userNotification) {
        return notificationRepo.saveAll(userNotification);
    }
}

