package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.entity.HealthCheckConsentForm;
import com.schoolhealth.schoolmedical.entity.enums.GradeLevel;
import com.schoolhealth.schoolmedical.exception.NotFoundException;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckConsentFlatData;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckConsentRes;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckDiseaseRes;
import com.schoolhealth.schoolmedical.model.dto.response.PupilRes;
import com.schoolhealth.schoolmedical.model.mapper.HealthCheckConsentMapper;
import com.schoolhealth.schoolmedical.repository.HealthCheckConsentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HealthCheckConsentImpl implements HealthCheckConsentService{

    @Autowired
    private HealthCheckConsentRepo healthCheckConsentRepo;

    @Autowired
    private HealthCheckConsentMapper healthCheckConsentMapper;

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
    public List<HealthCheckConsentRes> getHealthCheckConsentByGradeAndSchoolYear(GradeLevel grade, int schoolYear) {
        List<HealthCheckConsentFlatData> rs = healthCheckConsentRepo.findListPupilByGradeAndSchoolYear(grade, schoolYear);
        if(rs.isEmpty()) throw new NotFoundException("No health check consent found for grade: " + grade + " and school year: " + schoolYear);
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
            List<HealthCheckDiseaseRes> diseaseRes = consentDataList.stream()
                    .map(data -> HealthCheckDiseaseRes.builder()
                            .healthCheckDiseaseId(data.getHealthCheckDiseaseId())
                            .diseaseName(data.getDiseaseName())
                            .build())
                    .toList();

            HealthCheckConsentRes consentRes = HealthCheckConsentRes.builder()
                    .consentFormId(consentId)
                    .schoolYear(consentDataList.getFirst().getSchoolYear())
                    .pupilRes(pupil)
                    .disease(diseaseRes)
                    .build();
            res.add(consentRes);
        }
        return res;
    }

}
