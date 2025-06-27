package com.schoolhealth.schoolmedical.service.Notification;

import com.schoolhealth.schoolmedical.entity.UserNotification;
import com.schoolhealth.schoolmedical.model.dto.response.NotificationHealthCampaignRes;
import com.schoolhealth.schoolmedical.repository.NotificationRepo;
import com.schoolhealth.schoolmedical.service.DiseaseService;
import com.schoolhealth.schoolmedical.service.HealthCheckCampaignService;
import com.schoolhealth.schoolmedical.service.pupil.PupilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserNotificationImpl implements UserNotificationService {
//    @Autowired
//    private HealthCheckCampaignService healthCheckCampaignService;
    @Autowired
    private DiseaseService diseaseService;
    @Autowired
    private PupilService pupilService;
    @Autowired
    private NotificationRepo notificationRepo;
    @Override
    public List<UserNotification> saveAllUserNotifications(List<UserNotification> userNotification) {
        return notificationRepo.saveAll(userNotification);
    }

    @Override
    public NotificationHealthCampaignRes getNotificationHealthCampaignByIdAndPupilAndDisease(Long id, String pupilId) {

        return null;
    }


}

