package com.schoolhealth.schoolmedical.service.Notification;

import com.schoolhealth.schoolmedical.entity.HealthCheckCampaign;
import com.schoolhealth.schoolmedical.entity.HealthCheckDisease;
import com.schoolhealth.schoolmedical.entity.SendMedication;
import com.schoolhealth.schoolmedical.entity.UserNotification;
import com.schoolhealth.schoolmedical.entity.enums.TypeNotification;
import com.schoolhealth.schoolmedical.model.dto.response.*;
import com.schoolhealth.schoolmedical.model.mapper.*;
import com.schoolhealth.schoolmedical.repository.NotificationRepo;
import com.schoolhealth.schoolmedical.service.DiseaseService;
import com.schoolhealth.schoolmedical.service.HealthCheckCampaignService;
import com.schoolhealth.schoolmedical.service.HealthCheckDiseaseService;
import com.schoolhealth.schoolmedical.service.pupil.PupilService;
import com.schoolhealth.schoolmedical.service.sendMedication.SendMedicalService;
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
    private HealthCheckDiseaseService healthCheckDiseaseService;
    @Autowired
    private PupilService pupilService;
    @Autowired
    private NotificationRepo notificationRepo;

    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private HealthCheckCampaignMapper healthCheckCampaignMapper;

    @Autowired
    private DiseaseMapper diseaseMapper;

    @Autowired
    @Lazy
    private SendMedicalService sendMedicalService;
    @Autowired
    private SendMedicationMapper sendMedicationMapper;

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
            case HEALTH_CHECK_CAMPAIGN:
                return getHealthCheckCampaignResponse(sourceId, pupilId);
            case SEND_MEDICAL:
                return getSendMedicationResponse(sourceId);
            default:
                throw new IllegalArgumentException("Invalid type of notification");
        }
    }

    @Override
    public List<NotificationRes> getAllNotificationsByParentId(String parentId) {
        List<UserNotification> userNotification = notificationRepo.findAllByUserId(parentId);
        return notificationMapper.toDto(userNotification);
    }

    public NotificationHealthCampaignRes getHealthCheckCampaignResponse(Long id, String pupilId) {
        HealthCheckCampaign healthCheckCampaign = healthCheckCampaignService.getHealthCheckCampaignEntityById(id);
        return NotificationHealthCampaignRes.builder()
                .healthCheckCampaign(healthCheckCampaignMapper.toDto(healthCheckCampaign))
                .pupil(pupilService.getPupilById(pupilId))
                .build();
    }
    public MedicationLogNotifcationRes getSendMedicationResponse(Long medicationLogId) {
        SendMedication sendMedication = sendMedicalService.findByMedicationLogId(medicationLogId);
        return MedicationLogNotifcationRes.builder()
                .pupilId(sendMedication.getPupil().getPupilId())
                .pupilFirstName(sendMedication.getPupil().getFirstName())
                .pupilLastName(sendMedication.getPupil().getLastName())
                .sendMedicationId(sendMedication.getSendMedicationId())
                .senderName(sendMedication.getSenderName())
                .diseaseName(sendMedication.getDiseaseName())
                .startDate(sendMedication.getStartDate())
                .endDate(sendMedication.getEndDate())
                .medicationLog(sendMedicationMapper.toMedicationLogsDto(sendMedication.getMedicationLogs().getFirst()))
                .build();
    }
}

