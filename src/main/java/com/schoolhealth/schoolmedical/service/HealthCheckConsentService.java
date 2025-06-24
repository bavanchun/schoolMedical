package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.entity.HealthCheckConsentForm;
import com.schoolhealth.schoolmedical.entity.enums.GradeLevel;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckConsentRes;

import java.util.List;

public interface HealthCheckConsentService {
    List<HealthCheckConsentForm> getAllHealthCheckConsents();
    HealthCheckConsentForm saveHealthCheckConsent(HealthCheckConsentForm healthCheckConsentForm);
    List<HealthCheckConsentForm> saveAll(List<HealthCheckConsentForm> healthCheckConsentForms);
    List<HealthCheckConsentRes> getHealthCheckConsentByGradeAndSchoolYear(GradeLevel gradeId, int schoolYear);
}
