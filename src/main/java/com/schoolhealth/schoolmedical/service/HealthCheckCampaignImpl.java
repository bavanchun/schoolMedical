package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.entity.*;
import com.schoolhealth.schoolmedical.entity.enums.StatusHealthCampaign;
import com.schoolhealth.schoolmedical.entity.enums.TypeNotification;
import com.schoolhealth.schoolmedical.exception.NotFoundException;
import com.schoolhealth.schoolmedical.exception.UpdateNotAllowedException;
import com.schoolhealth.schoolmedical.model.dto.request.HealthCheckCampaignReq;
import com.schoolhealth.schoolmedical.model.dto.response.*;
import com.schoolhealth.schoolmedical.model.mapper.DiseaseMapper;
import com.schoolhealth.schoolmedical.model.mapper.HealthCheckCampaignMapper;
import com.schoolhealth.schoolmedical.model.mapper.HealthCheckConsentMapper;
import com.schoolhealth.schoolmedical.repository.HealthCheckCampaignRepo;
import com.schoolhealth.schoolmedical.service.HealthCheckHistory.HealthCheckHistoryService;
import com.schoolhealth.schoolmedical.service.Notification.FCMService;
import com.schoolhealth.schoolmedical.service.Notification.UserNotificationService;
import com.schoolhealth.schoolmedical.service.pupil.PupilService;
import com.schoolhealth.schoolmedical.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Autowired
    private DiseaseMapper diseaseMapper;
    @Autowired
    private HealthCheckConsentMapper healthCheckConsentMapper;


    @Override
    public HealthCheckCampaignRes saveHealthCheckCampaign(HealthCheckCampaignReq healthCheckCampaignReq){
        //HealthCheckCampaign currentCampaign = healthCheckCampaignRepo.findCurrentCampaign();
//        if(currentCampaign != null && currentCampaign.getCreatedAt().getYear() == Year.now().getValue() && currentCampaign.getStatusHealthCampaign() != StatusHealthCampaign.CANCELLED) {
//            throw new CampaignAlreadyExistsForYearException("A health check campaign already exists for the year:"  + Year.now().getValue() + ". Please cancel the existing campaign before creating a new one.");
//        }
        HealthCheckCampaign campaign = healthCheckCampaignRepo.save(healthCheckCampaignMapper.toEntity(healthCheckCampaignReq));
        List<Disease> diseases = diseaseService.getAllDiseasesById(healthCheckCampaignReq.getDiseaseIds());
        List<HealthCheckDisease> healthCheckDiseases = new ArrayList<>();
        for( Disease disease : diseases) {
            HealthCheckDisease healthCheckDisease = HealthCheckDisease.builder()
                    .healthCheckCampaign(campaign)
                    .disease(disease)
                    .build();
            healthCheckDiseases.add(healthCheckDisease);
        }
        healthCheckDiseaseService.saveHealthCheckDisease(healthCheckDiseases);
        return HealthCheckCampaignRes.builder()
                .campaignId(campaign.getCampaignId())
                .title(campaign.getTitle())
                .address(campaign.getAddress())
                .description(campaign.getDescription())
                .deadlineDate(campaign.getDeadlineDate())
                .startExaminationDate(campaign.getStartExaminationDate())
                .endExaminationDate(campaign.getEndExaminationDate())
                .createdAt(campaign.getCreatedAt())
                .statusHealthCampaign(campaign.getStatusHealthCampaign())
                .diseases(diseaseMapper.toHealthCheckDiseaseDtoList(healthCheckDiseases))
                .build();
    }

    @Override
    public HealthCheckCampaignRes getHealthCheckCampaignDetailsById(Long campaignId) {
        HealthCheckCampaign rs = healthCheckCampaignRepo.findHealthCheckCampaignByCampaignId(campaignId)
                .orElseThrow(() -> new NotFoundException("Health check campaign not found with id: " + campaignId));
        List<HealthCheckConsentRes> healthCheckCosnentList = null;
        if(rs.getStatusHealthCampaign() != StatusHealthCampaign.CANCELLED || rs.getStatusHealthCampaign() != StatusHealthCampaign.PENDING) {
                 healthCheckCosnentList = healthCheckConsentService.getHealthCheckConsentByCampaignId(campaignId);
        }
        List<ConsentDiseaseRes> diseaseOfCampaign = diseaseMapper.toHealthCheckDiseaseDtoList(rs.getHealthCheckDiseases());

        // Create HealthCheckCampaignRes object
        return  HealthCheckCampaignRes.builder()
                    .campaignId(rs.getCampaignId())
                    .title(rs.getTitle())
                    .address(rs.getAddress())
                    .description(rs.getDescription())
                    .deadlineDate(rs.getDeadlineDate())
                    .startExaminationDate(rs.getStartExaminationDate())
                    .endExaminationDate(rs.getEndExaminationDate())
                    .createdAt(rs.getCreatedAt())
                    .statusHealthCampaign(rs.getStatusHealthCampaign())
                    .diseases(diseaseOfCampaign)
                    .consentForms(healthCheckCosnentList)
                    .build();

    }

    @Override
    @Transactional
    public void updateStatusHealthCheckCampaign(Long campaignId, StatusHealthCampaign statusHealthCampaign) {
        HealthCheckCampaign campaign1 = healthCheckCampaignRepo.findById(campaignId).orElseThrow(() -> new NotFoundException("campaign not found to update status"));

        if (statusHealthCampaign == StatusHealthCampaign.PUBLISHED) {
            Optional<HealthCheckCampaign> currentCampaign = healthCheckCampaignRepo.findCurrentCampaignByStatus(Year.now().getValue(), StatusHealthCampaign.PUBLISHED);
            if (currentCampaign.isPresent()) {
                throw new UpdateNotAllowedException("a health campaign in system already published");
            }
            campaign1.setStatusHealthCampaign(statusHealthCampaign);
            HealthCheckCampaign campaign = healthCheckCampaignRepo.save(campaign1);

            List<Pupil> pupils = pupilService.getAll();

            List<HealthCheckConsentForm> healthCheckConsentForm = new ArrayList<>();
            for (Pupil pupil : pupils) {
                HealthCheckConsentForm consentForm = new HealthCheckConsentForm();
                consentForm.setPupil(pupil);
                consentForm.setSchoolYear(Year.now().getValue());
                consentForm.setHealthCheckCampaign(campaign);
                healthCheckConsentForm.add(consentForm);
            }
            healthCheckConsentService.saveAll(healthCheckConsentForm);

//            List<User> parents = userService.findAllByRole(Role.PARENT);
            List<User> parents = userService.findAllWithPupilByParent();
            //save notification for parents
            List<UserNotification> listNotification = new ArrayList<>();
            for (User parent : parents) {
                UserNotification notification = UserNotification.builder()
                        .message("Chiến dịch kiểm tra sức khỏe đã được công bố")
                        .sourceId(campaign.getCampaignId())
                        .typeNotification(TypeNotification.HEALTH_CHECK_CAMPAIGN)
                        .user(parent)
                        .build();
                listNotification.add(notification);
            }
            userNotificationService.saveAllUserNotifications(listNotification);
            //notification to parents
            Map<String, List<Pupil>> pupilsByParent = parents.stream()
                    .filter(parent -> parent.getDeviceToken() != null && !parent.getDeviceToken().isEmpty())
                    .collect(Collectors.groupingBy(User::getDeviceToken, Collectors.mapping(User::getPupils, Collectors.flatMapping(List::stream, Collectors.toList()))));

            List<String> tokens = parents.stream()
                    .map(User::getDeviceToken)
                    .filter(token -> token != null && !token.isEmpty())
                    .toList();

            if (!pupilsByParent.isEmpty()) {
                String title = "Annual health check campaign";
                fcmService.sendNotification(pupilsByParent, campaignId, TypeNotification.HEALTH_CHECK_CAMPAIGN.name(), title);
            }
        } else if (statusHealthCampaign == StatusHealthCampaign.IN_PROGRESS) {
            Optional<HealthCheckCampaign> currentCampaign = healthCheckCampaignRepo.findCurrentCampaignByStatus(Year.now().getValue(), StatusHealthCampaign.IN_PROGRESS);
            if (currentCampaign.isPresent()) {
                throw new UpdateNotAllowedException("a health campaign in system already in progress");
            }
            campaign1.setStatusHealthCampaign(statusHealthCampaign);
            healthCheckCampaignRepo.save(campaign1);
        } else if (statusHealthCampaign == StatusHealthCampaign.CANCELLED) {
            StatusHealthCampaign originalStatus = campaign1.getStatusHealthCampaign();
            if (originalStatus != StatusHealthCampaign.PENDING && originalStatus != StatusHealthCampaign.PUBLISHED) {
                throw new UpdateNotAllowedException("Campaign can only be cancelled if its status is PENDING or PUBLISHED.");
            }

            campaign1.setStatusHealthCampaign(StatusHealthCampaign.CANCELLED);
            HealthCheckCampaign campaign = healthCheckCampaignRepo.save(campaign1);

            if (originalStatus == StatusHealthCampaign.PUBLISHED) {
                List<User> parents = userService.findAllWithPupilByParent();
                //save notification for parents
                List<UserNotification> listNotification = new ArrayList<>();
                for (User parent : parents) {
                    UserNotification notification = UserNotification.builder()
                            .message("The health check campaign has been cancelled")
                            .sourceId(campaign.getCampaignId())
                            .typeNotification(TypeNotification.HEALTH_CHECK_CAMPAIGN)
                            .user(parent)
                            .build();
                    listNotification.add(notification);
                }
                userNotificationService.saveAllUserNotifications(listNotification);
            }
        }else{
            campaign1.setStatusHealthCampaign(statusHealthCampaign);
            healthCheckCampaignRepo.save(campaign1);
        }
    }

    @Override
    public HealthCheckCampaignRes getLatestHealthCheckCampaign() {
        return healthCheckCampaignMapper.toDto(healthCheckCampaignRepo.findStatusCampaignPublishedInProgressOrderByCreatedAtDesc());
    }


    @Override
    public List<HealthCheckCampaignRes> getAllHealthCheckCampaigns() {
        return healthCheckCampaignMapper.toDto(healthCheckCampaignRepo.findAllByActiveTrue());
    }

    @Override
    public HealthCheckCampaignRes getHealthCheckCampaignById(Long campaignId) {
         HealthCheckCampaign h = healthCheckCampaignRepo.findById(campaignId)
                .orElseThrow(() -> new NotFoundException("Health check campaign not found with id: " + campaignId));
         return healthCheckCampaignMapper.toDto(h);
    }

    @Override
    public HealthCheckCampaign getHealthCheckCampaignEntityById(Long campaignId) {
        return healthCheckCampaignRepo.findById(campaignId)
                .orElseThrow(() -> new NotFoundException("Health check campaign not found with id: " + campaignId));
    }

    @Override
    public HealthCheckCampaignRes updateHealthCheckCampaignAndDiseases(Long campaignId, HealthCheckCampaignReq healthCheckCampaign) {
        HealthCheckCampaign existingCampaign = healthCheckCampaignRepo.findById(campaignId)
                .orElseThrow(() -> new NotFoundException("Health check campaign not found with id: " + campaignId));
        if (existingCampaign.getStatusHealthCampaign() != StatusHealthCampaign.PENDING) {
            throw new UpdateNotAllowedException("Health check campaign can only be updated if its status is PENDING.");
        }
        healthCheckCampaignMapper.updateEntityFromDto(healthCheckCampaign, existingCampaign);
        existingCampaign.getHealthCheckDiseases().clear();
        List<Disease> diseases = diseaseService.getAllDiseasesById(healthCheckCampaign.getDiseaseIds());
        List<HealthCheckDisease> healthCheckDiseases = new ArrayList<>();
        for (Disease disease : diseases) {
            HealthCheckDisease healthCheckDisease = HealthCheckDisease.builder()
                    .healthCheckCampaign(existingCampaign)
                    .disease(disease)
                    .build();
            healthCheckDiseases.add(healthCheckDisease);
        }
        existingCampaign.getHealthCheckDiseases().addAll(healthCheckDiseases);
        return healthCheckCampaignMapper.toDto(healthCheckCampaignRepo.save(existingCampaign));
    }

    @Override
    public List<HealthCheckCampaginByIdRes> getHealthCheckCampaignsByPupilId(String pupilId) {
//        List<HealthCheckConsentForm> consentForms = healthCheckConsentService.getHealthCheckConsentByPupilId(pupilId);
//        List<HealthCheckCampaign> campaignIds = consentForms.stream()
//                .map(HealthCheckConsentForm::getHealthCheckCampaign)
//                .toList();
        List<HealthCheckConsentForm> consentForms = healthCheckConsentService.getHealthCheckConsentByPupilId(pupilId);
        List<HealthCheckCampaginByIdRes> healthCheckCampaignRes = new ArrayList<>();
        for( HealthCheckConsentForm consentForm : consentForms) {
            HealthCheckCampaign campaign = consentForm.getHealthCheckCampaign();
            HealthCheckCampaginByIdRes campaignRes = HealthCheckCampaginByIdRes.builder()
                    .campaignId(campaign.getCampaignId())
                    .title(campaign.getTitle())
                    .address(campaign.getAddress())
                    .description(campaign.getDescription())
                    .deadlineDate(campaign.getDeadlineDate())
                    .startExaminationDate(campaign.getStartExaminationDate())
                    .endExaminationDate(campaign.getEndExaminationDate())
                    .createdAt(campaign.getCreatedAt())
                    .statusHealthCampaign(campaign.getStatusHealthCampaign())
                    .diseases(diseaseMapper.toHealthCheckDiseaseDtoList(campaign.getHealthCheckDiseases()))
                    .consentForms(healthCheckConsentMapper.toConsentDtoByPupil(consentForm))
                    .build();
            healthCheckCampaignRes.add(campaignRes);
        }
        return healthCheckCampaignRes;
    }
}



