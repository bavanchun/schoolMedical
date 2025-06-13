package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.entity.HealthCheckConsentForm;
import com.schoolhealth.schoolmedical.repository.HealthCheckConsentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HealthCheckCosentImpl implements HealthCheckConsentService{

    @Autowired
    private HealthCheckConsentRepo healthCheckConsentRepo;

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
}
