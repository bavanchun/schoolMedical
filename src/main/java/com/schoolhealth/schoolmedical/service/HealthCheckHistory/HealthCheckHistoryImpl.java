package com.schoolhealth.schoolmedical.service.HealthCheckHistory;

import com.schoolhealth.schoolmedical.entity.HealthCheckConsentForm;
import com.schoolhealth.schoolmedical.entity.HealthCheckHistory;
import com.schoolhealth.schoolmedical.exception.NotFoundException;
import com.schoolhealth.schoolmedical.model.dto.request.HealthCheckHistoryReq;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckHistoryRes;
import com.schoolhealth.schoolmedical.model.mapper.HealthCheckHistoryMapper;
import com.schoolhealth.schoolmedical.repository.HealthCheckConsentRepo;
import com.schoolhealth.schoolmedical.repository.HealthCheckHistoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HealthCheckHistoryImpl implements HealthCheckHistoryService {
    @Autowired
    private HealthCheckHistoryRepo healthCheckHistoryRepo;

    @Autowired
    private HealthCheckConsentRepo healthCheckConsentRepo;

    @Autowired
    private HealthCheckHistoryMapper healthCheckHistoryMapper;

    @Override
    public HealthCheckHistory saveHealthCheckHistory(HealthCheckHistoryReq healthCheckHistoryReq, Long healthCheckConsentId) {
        HealthCheckConsentForm consentForm = healthCheckConsentRepo.findById(healthCheckConsentId)
                .orElseThrow(() -> new NotFoundException("Health Check Consent Form not found with ID: " + healthCheckConsentId));;
        HealthCheckHistory healthCheckHistory = healthCheckHistoryMapper.toHealthCheckHistory(healthCheckHistoryReq);
        healthCheckHistory.setHealthCheckConsentForm(consentForm);
        return  healthCheckHistoryRepo.save(healthCheckHistory);
    }

    @Override
    public HealthCheckHistoryRes getHealthCheckHistoryByPupilIdAndSchoolYear(String pupilId, int schoolYear) {
        HealthCheckHistory healthCheckHistory =  healthCheckHistoryRepo.findHealthCheckHistoryByPupilIdAndSchoolYear(pupilId, schoolYear)
                .orElseThrow(() -> new NotFoundException("Health Check History not found for pupil ID: " + pupilId + " and school year: " + schoolYear));

        return healthCheckHistoryMapper.toHealthCheckHistoryRes(healthCheckHistory);
    }
}
