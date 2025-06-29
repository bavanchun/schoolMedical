package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.entity.*;
import com.schoolhealth.schoolmedical.entity.enums.StatusHealthCampaign;
import com.schoolhealth.schoolmedical.entity.enums.TypeNotification;
import com.schoolhealth.schoolmedical.exception.CampaignAlreadyExistsForYearException;
import com.schoolhealth.schoolmedical.exception.NotFoundException;
import com.schoolhealth.schoolmedical.exception.UpdateNotAllowedException;
import com.schoolhealth.schoolmedical.model.dto.request.HealthCheckCampaginReq;
import com.schoolhealth.schoolmedical.model.dto.response.*;
import com.schoolhealth.schoolmedical.model.mapper.DiseaseMapper;
import com.schoolhealth.schoolmedical.model.mapper.HealthCheckCampaignMapper;
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


    @Override
    public HealthCheckCampaignRes saveHealthCheckCampaign(HealthCheckCampaginReq healthCheckCampaignReq){
        HealthCheckCampaign currentCampaign = healthCheckCampaignRepo.findCurrentCampaign();
        if(currentCampaign != null && currentCampaign.getCreatedAt().getYear() == Year.now().getValue() && currentCampaign.getStatusHealthCampaign() != StatusHealthCampaign.CANCELLED) {
            throw new CampaignAlreadyExistsForYearException("A health check campaign already exists for the year:"  + Year.now().getValue() + ". Please cancel the existing campaign before creating a new one.");
        }
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
        return healthCheckCampaignMapper.toDto(campaign);
    }

    @Override
    public HealthCheckCampaignRes getHealthCheckCampaignDetailsById(Long campaignId) {
        HealthCheckCampaign rs = healthCheckCampaignRepo.findHealthCheckCampaignByCampaignId(campaignId)
                .orElseThrow(() -> new NotFoundException("Health check campaign not found with id: " + campaignId));

        List<ConsentDiseaseRes> diseaseOfCampaign = diseaseMapper.toHealthCheckDiseaseDtoList(rs.getHealthCheckDiseases());
        // Check if the campaign is active
        List<HealthCheckConsentRes> healthCheckConsentResList = new ArrayList<>();
        List<ConsentDiseaseRes> diseaseForPupil = null;

        for (Map.Entry<String, List<HealthCheckCampaignFlatData>> entry : groupedData.entrySet()) {
            String pupilId = entry.getKey();
            List<HealthCheckCampaignFlatData> dataList = entry.getValue();

            // Create PupilRes object
            PupilRes pupilRes = PupilRes.builder()
                    .pupilId(dataList.getFirst().getPupilId())
                    .lastName(dataList.getFirst().getLastName())
                    .firstName(dataList.getFirst().getFirstName())
                    .birthDate(dataList.getFirst().getBirthDate())
                    .gender(dataList.getFirst().getGender())
                    .gradeId(dataList.getFirst().getGradeId())
                    .gradeLevel(dataList.getFirst().getGradeLevel())
                    .gradeName(dataList.getFirst().getGradeName())
                    .build();
            // Collect disease names for this pupil
            diseaseForPupil = dataList.stream()
                    .map(data -> ConsentDiseaseRes.builder()
                            .diseaseId(data.getDiseaseId())
                            .name(data.getDiseaseName())
                            .build())
                    .toList();
            HealthCheckConsentRes healthCheckConsentRes = HealthCheckConsentRes.builder()
                    .consentFormId(dataList.getFirst().getHealthCheckConsentId())
                    .schoolYear(dataList.getFirst().getSchoolYear())
                    .pupilRes(pupilRes)
                    .build();
            healthCheckConsentResList.add(healthCheckConsentRes);
        }
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
                    .consentForms(healthCheckConsentResList)
                    .build();

    }

    @Override
    @Transactional
    public void updateStatusHealthCheckCampaign(Long campaignId, StatusHealthCampaign statusHealthCampaign) {
        HealthCheckCampaign campaign1 = healthCheckCampaignRepo.findById(campaignId).orElseThrow(() -> new NotFoundException("campaign not found to update status"));
        campaign1.setStatusHealthCampaign(statusHealthCampaign);
        HealthCheckCampaign campaign = healthCheckCampaignRepo.save(campaign1);
        if(statusHealthCampaign == StatusHealthCampaign.PUBLISHED){
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
            for( User parent : parents) {
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
                String title = "Chiến dịch kiểm tra sức khỏe hằng năm";
                fcmService.sendNotification(pupilsByParent, campaignId, TypeNotification.HEALTH_CHECK_CAMPAIGN.name(),title );
            }
        }
    }

    @Override
    public LatestHealthCheckCampaignRes getLatestHealthCheckCampaign() {
        return healthCheckCampaignMapper.toLatestDto(healthCheckCampaignRepo.findStatusCampaignPublishedInProgressOrderByCreatedAtDesc());
    }
    public HealthCheckCampaign getLatestHealthCheckCampaignEntity() {
        return healthCheckCampaignRepo.findCurrentCampaign();
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
    public HealthCheckCampaignRes updateHealthCheckCampaignAndDiseases(HealthCheckCampaginReq healthCheckCampaign) {

        return null;
    }
}



