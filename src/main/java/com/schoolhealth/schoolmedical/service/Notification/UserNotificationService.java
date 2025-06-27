package com.schoolhealth.schoolmedical.service.Notification;

import com.schoolhealth.schoolmedical.entity.UserNotification;
import com.schoolhealth.schoolmedical.entity.enums.TypeNotification;
import com.schoolhealth.schoolmedical.model.dto.response.NotificationHealthCampaignRes;

import java.util.List;

public interface UserNotificationService {
    List<UserNotification> saveAllUserNotifications(List<UserNotification> userNotification);
    //NotificationHealthCampaignRes getNotificationHealthCampaignByIdAndPupilAndDisease(Long id, String pupilId, TypeNotification typeNotification);
    Object getNotificationCampaign(Long id, String pupilId, TypeNotification typeNotification);

}