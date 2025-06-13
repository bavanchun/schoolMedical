package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.entity.HealthCheckConsentForm;

import java.util.List;

public interface HealthCheckConsentService {
    List<HealthCheckConsentForm> getAllHealthCheckConsents();
    HealthCheckConsentForm saveHealthCheckConsent(HealthCheckConsentForm healthCheckConsentForm);
    List<HealthCheckConsentForm> saveAll(List<HealthCheckConsentForm> healthCheckConsentForms);
}
