package com.schoolhealth.schoolmedical.service.HealthCheckHistory;

import com.schoolhealth.schoolmedical.entity.HealthCheckConsentForm;
import com.schoolhealth.schoolmedical.entity.HealthCheckHistory;
import com.schoolhealth.schoolmedical.exception.NotFoundException;
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

    @Override
    public HealthCheckHistory saveHealthCheckHistory(HealthCheckHistory healthCheckHistory, Long healthCheckConsentId) {
        HealthCheckConsentForm consentForm = healthCheckConsentRepo.findById(healthCheckConsentId)
                .orElseThrow(() -> new NotFoundException("Health Check Consent Form not found with ID: " + healthCheckConsentId));;
        healthCheckHistory.setHealthCheckConsentForm(consentForm);
        return healthCheckHistoryRepo.save(healthCheckHistory);
    }
}
