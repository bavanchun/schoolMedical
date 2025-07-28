package com.schoolhealth.schoolmedical.service.Notification;

import com.schoolhealth.schoolmedical.entity.UserNotification;
import com.schoolhealth.schoolmedical.entity.enums.Role;
import com.schoolhealth.schoolmedical.entity.enums.TypeNotification;
import com.schoolhealth.schoolmedical.model.dto.response.NotificationHealthCampaignRes;
import com.schoolhealth.schoolmedical.model.dto.response.NotificationRes;

import java.util.List;

public interface UserNotificationService {
    List<UserNotification> saveAllUserNotifications(List<UserNotification> userNotification);
    //NotificationHealthCampaignRes getNotificationHealthCampaignByIdAndPupilAndDisease(Long id, String pupilId, TypeNotification typeNotification);
    Object getNotificationCampaign(Long id, String pupilId, TypeNotification typeNotification);
    List<NotificationRes> getAllNotificationsByParentId(String parentId);
    void addToNotification(String message, Long sourceId, TypeNotification typeNotification, Role role);
}