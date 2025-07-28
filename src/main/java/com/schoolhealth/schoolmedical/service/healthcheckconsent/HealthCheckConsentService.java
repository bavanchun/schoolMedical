package com.schoolhealth.schoolmedical.service.healthcheckconsent;

import com.schoolhealth.schoolmedical.entity.HealthCheckCampaign;
import com.schoolhealth.schoolmedical.entity.HealthCheckConsentForm;
import com.schoolhealth.schoolmedical.entity.enums.GradeLevel;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckConsentRes;

import java.util.List;

public interface HealthCheckConsentService {
    List<HealthCheckConsentForm> getAllHealthCheckConsents();
    HealthCheckConsentForm saveHealthCheckConsent(HealthCheckConsentForm healthCheckConsentForm);
    List<HealthCheckConsentForm> saveAll(List<HealthCheckConsentForm> healthCheckConsentForms);
    List<HealthCheckConsentRes> getHealthCheckConsentByGradeAndSchoolYear(GradeLevel gradeId);
    HealthCheckConsentForm getHealthCheckConsentByPupilIdAndCampaignId(String pupilId, Long campaignId);
    void saveHealthCheckConsentForm(HealthCheckConsentForm healthCheckConsentForm);
    List<HealthCheckConsentRes> getHealthCheckConsentByCampaignId(Long campaignId);
    HealthCheckConsentRes getHealthCheckConsentById(Long consentId);
    List<HealthCheckConsentForm> getHealthCheckConsentByPupilId(String pupilId);
    List<HealthCheckConsentForm> getHealthCheckConsentByNotYetAndCampaign(HealthCheckCampaign healthCheckCampaign);
}
