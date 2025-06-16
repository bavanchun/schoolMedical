package com.schoolhealth.schoolmedical.service.Notification;


import com.schoolhealth.schoolmedical.entity.UserNotification;

import java.util.List;

public interface FCMService {
    String sendNotification(UserNotification notification, String token);
    void sendMulticastNotification(List<String> tokens, UserNotification notification);
}