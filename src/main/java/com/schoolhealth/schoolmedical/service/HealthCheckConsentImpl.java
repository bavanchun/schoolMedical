package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.entity.HealthCheckConsentForm;
import com.schoolhealth.schoolmedical.entity.enums.GradeLevel;
import com.schoolhealth.schoolmedical.exception.NotFoundException;
import com.schoolhealth.schoolmedical.model.dto.response.*;
import com.schoolhealth.schoolmedical.model.mapper.DiseaseMapper;
import com.schoolhealth.schoolmedical.model.mapper.HealthCheckConsentMapper;
import com.schoolhealth.schoolmedical.model.mapper.HealthCheckHistoryMapper;
import com.schoolhealth.schoolmedical.repository.HealthCheckConsentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HealthCheckConsentImpl implements HealthCheckConsentService{

    @Autowired
    private HealthCheckConsentRepo healthCheckConsentRepo;

    @Autowired
    private HealthCheckConsentMapper healthCheckConsentMapper;

    @Autowired
    private HealthCheckHistoryMapper healthCheckHistoryMapper;
    @Autowired
    private DiseaseMapper diseaseMapper;

    @Override
    public List<HealthCheckConsentForm> getAllHealthCheckConsents() {
        return healthCheckConsentRepo.findAll();
    }

    @Override
    public HealthCheckConsentForm saveHealthCheckConsent(HealthCheckConsentForm healthCheckConsentForm) {
        return healthCheckConsentRepo.save(healthCheckConsentForm);
    }

    @Override
    public List<HealthCheckConsentForm> saveAll(List<HealthCheckConsentForm> healthCheckConsentForms) {
        return healthCheckConsentRepo.saveAll(healthCheckConsentForms);
    }

    @Override
    public List<HealthCheckConsentRes> getHealthCheckConsentByGradeAndSchoolYear(GradeLevel grade) {
        List<HealthCheckConsentFlatData> rs = healthCheckConsentRepo.findListPupilByGradeAndSchoolYear(grade);
        if(rs.isEmpty()) throw new NotFoundException("No health check consent found for grade: " + grade );
        List<HealthCheckConsentRes> res = new ArrayList<>();
        Map<Long, List<HealthCheckConsentFlatData>> groupedByConsentId = rs.stream()
                .collect(Collectors.groupingBy(HealthCheckConsentFlatData::getHealthCheckConsentId));
        for (Map.Entry<Long, List<HealthCheckConsentFlatData>> entry : groupedByConsentId.entrySet()) {
            Long consentId = entry.getKey();
            List<HealthCheckConsentFlatData> consentDataList = entry.getValue();

            PupilRes pupil = PupilRes.builder()
                    .pupilId(consentDataList.getFirst().getPupilId())
                    .lastName(consentDataList.getFirst().getLastName())
                    .firstName(consentDataList.getFirst().getFirstName())
                    .birthDate(consentDataList.getFirst().getBirthDate())
                    .gender(consentDataList.getFirst().getGender())
                    .gradeName(consentDataList.getFirst().getGradeName())
                    .build();
            List<ConsentDiseaseRes> diseaseRes = consentDataList.stream()
                    .map(data -> ConsentDiseaseRes.builder()
                            .diseaseId(data.getDiseaseId())
                            .name(data.getName())
                            .description(data.getDescription())
                            .build())
                    .toList();

            HealthCheckConsentRes consentRes = HealthCheckConsentRes.builder()
                    .consentFormId(consentId)
                    .schoolYear(consentDataList.getFirst().getSchoolYear())
                    .pupilRes(pupil)
                    .active(consentDataList.getFirst().isActive())
                    .disease(diseaseRes)
                    .build();
            res.add(consentRes);
        }
        return res;
    }

    @Override
    public HealthCheckConsentForm getHealthCheckConsentByPupilIdAndCampaignId(String pupilId, Long campaignId) {
        return healthCheckConsentRepo.findHealthCheckConsentByPupilIdAndCampaignId(pupilId, campaignId);
    }

    @Override
    public void saveHealthCheckConsentForm(HealthCheckConsentForm healthCheckConsentForm) {
        healthCheckConsentRepo.save(healthCheckConsentForm);
    }
    public List<HealthCheckConsentForm> getHealthCheckConsentById(List<Long> consentIds) {
        return healthCheckConsentRepo.findAllById(consentIds);
    }
    @Override
    public List<HealthCheckConsentRes> getHealthCheckConsentByCampaignId(Long campaignId) {
        List<HealthCheckConsentFlatData> rs = healthCheckConsentRepo.findHealthCheckConsentFormByCampaignId(campaignId);
        if(rs.isEmpty()) throw new NotFoundException("No health check consent found for campaignId:" + campaignId);

        List<HealthCheckConsentRes> res = new ArrayList<>();
        Map<Long, List<HealthCheckConsentFlatData>> groupedByConsentId = rs.stream()
                .collect(Collectors.groupingBy(HealthCheckConsentFlatData::getHealthCheckConsentId));
        List<Long> key = groupedByConsentId.keySet().stream().toList();

        List<HealthCheckConsentForm> checkConsent = getHealthCheckConsentById(key);

        Map<Long, List<HealthCheckConsentForm>> checkConsentMap = checkConsent.stream()
                .collect(Collectors.groupingBy(HealthCheckConsentForm::getConsentFormId));

        for (Map.Entry<Long, List<HealthCheckConsentFlatData>> entry : groupedByConsentId.entrySet()) {
            Long consentId = entry.getKey();
            List<HealthCheckConsentForm> form = checkConsentMap.get(consentId);
            HealthCheckHistoryRes healthCheckHistoryRes = null;
            if( form !=null){
                healthCheckHistoryRes = healthCheckHistoryMapper.toHealthCheckHistoryRes(form.getFirst().getHealthCheckHistory());
            }

            List<HealthCheckConsentFlatData> consentDataList = entry.getValue();

            PupilRes pupil = PupilRes.builder()
                    .pupilId(consentDataList.getFirst().getPupilId())
                    .lastName(consentDataList.getFirst().getLastName())
                    .firstName(consentDataList.getFirst().getFirstName())
                    .birthDate(consentDataList.getFirst().getBirthDate())
                    .gender(consentDataList.getFirst().getGender())
                    .gradeName(consentDataList.getFirst().getGradeName())
                    .build();
            List<ConsentDiseaseRes> diseaseRes = consentDataList.stream()
                    .map(data -> ConsentDiseaseRes.builder()
                            .diseaseId(data.getDiseaseId())
                            .name(data.getName())
                            .description(data.getDescription())
                            .note(data.getNote())
                            .build())
                    .toList();

            HealthCheckConsentRes consentRes = HealthCheckConsentRes.builder()
                    .consentFormId(consentId)
                    .schoolYear(consentDataList.getFirst().getSchoolYear())
                    .pupilRes(pupil)
                    .active(consentDataList.getFirst().isActive())
                    .healthCheckHistoryRes(healthCheckHistoryRes)
                    .disease(diseaseRes)
                    .build();
            res.add(consentRes);
        }
        return res;
    }

    @Override
    public HealthCheckConsentRes getHealthCheckConsentById(Long consentId) {
        HealthCheckConsentForm consentForm = healthCheckConsentRepo.findByConsentFormId(consentId);
        return HealthCheckConsentRes.builder()
                .consentFormId(consentForm.getConsentFormId())
                .schoolYear(consentForm.getSchoolYear())
                .active(consentForm.isActive())
                .healthCheckHistoryRes(healthCheckHistoryMapper.toHealthCheckHistoryRes(consentForm.getHealthCheckHistory()))
                .disease(diseaseMapper.toConsentDiseasesDtoList(consentForm.getConsentDiseases()))
                .build();
    }

    @Override
    public List<HealthCheckConsentForm> getHealthCheckConsentByPupilId(String pupilId) {
    List<HealthCheckConsentForm> consentForms = healthCheckConsentRepo.findAllByPupilPupilIdIn(pupilId);
        return consentForms;
    }

}
