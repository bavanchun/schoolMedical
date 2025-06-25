package com.schoolhealth.schoolmedical.service.Notification;


import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.entity.UserNotification;

import java.util.List;
import java.util.Map;

public interface FCMService {
    void sendNotification(Map<String, List<Pupil>> pupilsMap, Long sourceId, String type, String title);
    void sendMulticastNotification(List<String> tokens, UserNotification notification);
}