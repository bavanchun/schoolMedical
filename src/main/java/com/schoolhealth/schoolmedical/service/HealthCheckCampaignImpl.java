package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.entity.*;
import com.schoolhealth.schoolmedical.entity.enums.Role;
import com.schoolhealth.schoolmedical.entity.enums.TypeNotification;
import com.schoolhealth.schoolmedical.exception.NotFoundException;
import com.schoolhealth.schoolmedical.model.dto.request.HealthCheckCampaginReq;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckCampaignResponse;
import com.schoolhealth.schoolmedical.model.mapper.HealthCheckCampaignMapper;
import com.schoolhealth.schoolmedical.repository.HealthCheckCampaignRepo;
import com.schoolhealth.schoolmedical.service.HealthCheckHistory.HealthCheckHistoryService;
import com.schoolhealth.schoolmedical.service.Notification.FCMService;
import com.schoolhealth.schoolmedical.service.Notification.UserNotificationService;
import com.schoolhealth.schoolmedical.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Service
public class HealthCheckCampaignImpl implements HealthCheckCampaignService {

    @Autowired
    private HealthCheckCampaignRepo healthCheckCampaignRepo;

    @Autowired
    private PupilService pupilService;

    @Autowired
    private HealthCheckConsentService healthCheckConsentService;

    @Autowired
    private DiseaseService diseaseService;

    @Autowired
    private HealthCheckDiseaseService healthCheckDiseaseService;

    @Autowired
    private FCMService fcmService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserNotificationService userNotificationService;

    @Autowired
    private HealthCheckCampaignMapper healthCheckCampaignMapper;

    @Autowired
    private HealthCheckHistoryService healthCheckHistoryService;

    @Override
    public HealthCheckCampaign getHealthCheckCampaignById() {
        return null;
    }

    // This method saves a health check campaign and creates consent forms for all pupils
    @Override
    @Transactional
    public HealthCheckCampaignResponse saveHealthCheckCampaign(HealthCheckCampaginReq healthCheckCampaign) {
        List<Pupil> pupils = pupilService.getAll();

        HealthCheckCampaign campaign = healthCheckCampaignRepo.saveAndFlush(
                healthCheckCampaignMapper.toEntity(healthCheckCampaign)
        );

        List<HealthCheckConsentForm> healthCheckConsentForm = new ArrayList<>();
        for (Pupil pupil : pupils) {
            HealthCheckConsentForm consentForm = new HealthCheckConsentForm();
            consentForm.setPupil(pupil);
//            HealthCheckHistory history = new HealthCheckHistory();
//            history = healthCheckHistoryService.saveHealthCheckHistory(history);
//            consentForm.setHealthCheckHistory(history);
            consentForm.setSchoolYear(Year.now().getValue());
            consentForm.setHealthCheckCampaign(campaign);
            healthCheckConsentForm.add(consentForm);
        }
        healthCheckConsentService.saveAll(healthCheckConsentForm);

        List<HealthCheckDisease> healthCheckDiseases = new ArrayList<>();
        List<Disease> diseases = diseaseService.getAllDiseases();
        List<HealthCheckConsentForm> consentForms = healthCheckConsentService.getAllHealthCheckConsents();
        for( HealthCheckConsentForm consentForm : consentForms) {
            for (Disease disease : diseases) {
                HealthCheckDisease checkDiseases = new HealthCheckDisease();
                checkDiseases.setHealthCheckCampaign(campaign);
                checkDiseases.setDisease(disease);
                checkDiseases.setHealthCheckConsentForm(consentForm);
                healthCheckDiseases.add(checkDiseases);
            }
        }
        healthCheckDiseaseService.saveHealthCheckDisease(healthCheckDiseases);
        List<User> parents = userService.findAllByRole(Role.PARENT);
        List<UserNotification> listNotification = new ArrayList<>();
        for( User parent : parents) {
            UserNotification notification = UserNotification.builder()
                    .message("Chiến dịch kiểm tra sức khỏe mới")
                    .sourceId(campaign.getCampaignId())
                    .typeNotification(TypeNotification.health_check_campaign)
                    .user(parent)
                    .build();
            listNotification.add(notification);
        }
        userNotificationService.saveAllUserNotifications(listNotification);
        List<String> tokens = parents.stream()
                .map(User::getDeviceToken)
                .filter(token -> token != null && !token.isEmpty())
                .toList();

        // Add null/empty check before using getFirst()
        if (!listNotification.isEmpty()) {
            fcmService.sendMulticastNotification(tokens, listNotification.getFirst());
        }

        return healthCheckCampaignMapper.toDto(campaign);
    }

    @Override
    public List<HealthCheckCampaignResponse> getAllHealthCheckCampaigns() {
        return healthCheckCampaignRepo.findAll().stream()
                .map(healthCheckCampaignMapper::toDto)
                .toList();
    }
}

