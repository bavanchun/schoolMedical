package com.schoolhealth.schoolmedical.service.HealthCheckHistory;

import com.schoolhealth.schoolmedical.entity.*;
import com.schoolhealth.schoolmedical.exception.NotFoundException;
import com.schoolhealth.schoolmedical.model.dto.request.HealthCheckHistoryReq;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckHistoryRes;
import com.schoolhealth.schoolmedical.model.mapper.HealthCheckHistoryMapper;
import com.schoolhealth.schoolmedical.repository.HealthCheckConsentRepo;
import com.schoolhealth.schoolmedical.repository.HealthCheckHistoryRepo;
import com.schoolhealth.schoolmedical.service.DiseaseService;
import com.schoolhealth.schoolmedical.service.consentDisease.ConsentDiseaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HealthCheckHistoryImpl implements HealthCheckHistoryService {
    @Autowired
    private HealthCheckHistoryRepo healthCheckHistoryRepo;

    @Autowired
    private HealthCheckConsentRepo healthCheckConsentRepo;

    @Autowired
    private HealthCheckHistoryMapper healthCheckHistoryMapper;
    @Autowired
    private DiseaseService diseaseService;
    @Autowired
    @Lazy
    private ConsentDiseaseService consentDiseaseService;
    @Override
    public HealthCheckHistory saveHealthCheckHistory(HealthCheckHistoryReq healthCheckHistoryReq, Long healthCheckConsentId) {
        HealthCheckConsentForm consentForm = healthCheckConsentRepo.findById(healthCheckConsentId)
                .orElseThrow(() -> new NotFoundException("Health Check Consent Form not found with ID: " + healthCheckConsentId));;
        HealthCheckHistory healthCheckHistory = healthCheckHistoryMapper.toHealthCheckHistory(healthCheckHistoryReq);
        healthCheckHistory.setHealthCheckConsentForm(consentForm);
        healthCheckHistory.setActive(true);
        consentForm.setActive(true);
        if(healthCheckHistoryReq.getDiseases()!=null){
            List<ConsentDisease> consentDisease = healthCheckHistoryReq.getDiseases().stream()
                    .map(diseaseReq -> {
                        Disease disease = diseaseService.getDiseaseEntityById(diseaseReq.getDiseaseId());
                        ConsentDiseaseId consentDiseaseId = new ConsentDiseaseId(consentForm.getConsentFormId(), disease.getDiseaseId());

                        return ConsentDisease.builder()
                                .id(consentDiseaseId)
                                .healthCheckConsentForm(consentForm)
                                .disease(disease)
                                .note(diseaseReq.getNote())
                                .build();
                    })
                    .toList();
            consentDiseaseService.saveConsentDisease(consentDisease);
        }
        healthCheckConsentRepo.save(consentForm);
        return  healthCheckHistoryRepo.save(healthCheckHistory);
    }

    @Override
    public HealthCheckHistoryRes getHealthCheckHistoryByPupilIdAndSchoolYear(String pupilId, int schoolYear) {
        HealthCheckHistory healthCheckHistory =  healthCheckHistoryRepo.findHealthCheckHistoryByPupilIdAndSchoolYear(pupilId, schoolYear)
                .orElseThrow(() -> new NotFoundException("Health Check History not found for pupil ID: " + pupilId + " and school year: " + schoolYear));

        return healthCheckHistoryMapper.toHealthCheckHistoryRes(healthCheckHistory);
    }


}
