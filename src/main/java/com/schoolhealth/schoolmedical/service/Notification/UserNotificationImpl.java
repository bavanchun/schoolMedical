package com.schoolhealth.schoolmedical.service.Notification;

import com.schoolhealth.schoolmedical.entity.UserNotification;
import com.schoolhealth.schoolmedical.entity.enums.TypeNotification;
import com.schoolhealth.schoolmedical.model.dto.response.DiseaseHealthCheckRes;
import com.schoolhealth.schoolmedical.model.dto.response.DiseaseResponse;
import com.schoolhealth.schoolmedical.model.dto.response.NotificationHealthCampaignRes;
import com.schoolhealth.schoolmedical.model.mapper.DiseaseMapper;
import com.schoolhealth.schoolmedical.model.mapper.HealthCheckCampaignMapper;
import com.schoolhealth.schoolmedical.model.mapper.PupilMapper;
import com.schoolhealth.schoolmedical.repository.NotificationRepo;
import com.schoolhealth.schoolmedical.service.DiseaseService;
import com.schoolhealth.schoolmedical.service.HealthCheckCampaignService;
import com.schoolhealth.schoolmedical.service.pupil.PupilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserNotificationImpl implements UserNotificationService {
    @Autowired
    @Lazy
    private HealthCheckCampaignService healthCheckCampaignService;
    @Autowired
    private DiseaseService diseaseService;
    @Autowired
    private PupilService pupilService;
    @Autowired
    private NotificationRepo notificationRepo;
    @Autowired
    private HealthCheckCampaignMapper healthCheckCampaignMapper;
    @Autowired
    private PupilMapper pupilMapper;
    @Autowired
    private DiseaseMapper diseaseMapper;
    @Override
    public List<UserNotification> saveAllUserNotifications(List<UserNotification> userNotification) {
        return notificationRepo.saveAll(userNotification);
    }
//    @Override
//    public NotificationHealthCampaignRes getNotificationHealthCampaignByIdAndPupilAndDisease(Long id, String pupilId, TypeNotification typeNotification) {
//        if (typeNotification == TypeNotification.health_check_campaign) {
//            return NotificationHealthCampaignRes.builder()
//                    .healthCheckCampaign(healthCheckCampaignService.getHealthCheckCampaignById(id))
//                    .pupil(pupilService.getPupilById(pupilId))
//                    .disease(diseaseService.getAllDiseasesByisInjectedVaccinationFalse(1, 10))
//                    .build();
//        }
//        throw new IllegalArgumentException("Invalid type of notification");
//    }
    @Override
    public Object getNotificationCampaign(Long sourceId, String pupilId, TypeNotification typeNotification) {
        switch (typeNotification) {
            case HEALTH_CHECK_CAMPAGIN:
                return getHealthCheckCampaignResponse(sourceId, pupilId);
            default:
                throw new IllegalArgumentException("Invalid type of notification");
        }
    }
    public NotificationHealthCampaignRes getHealthCheckCampaignResponse(Long id, String pupilId) {
        return NotificationHealthCampaignRes.builder()
                .healthCheckCampaign(healthCheckCampaignService.getHealthCheckCampaignById(id))
                .pupil(pupilService.getPupilById(pupilId))
                .disease(diseaseService.getAllDiseasesByisInjectedVaccinationFalse(1, 10))
                .build();
    }
}

