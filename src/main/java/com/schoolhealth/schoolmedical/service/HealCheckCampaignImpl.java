package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.entity.*;
import com.schoolhealth.schoolmedical.exception.NotFoundException;
import com.schoolhealth.schoolmedical.repository.HealthCheckCampaignRepo;
import com.schoolhealth.schoolmedical.repository.HealthCheckConsentRepo;
import com.schoolhealth.schoolmedical.repository.PupilRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class HealCheckCampaignImpl implements HealthCheckCampaignService {

    @Autowired
    private HealthCheckCampaignRepo healthCheckCampaignRepo;

    @Autowired
    private PupilService pupilService;

    @Autowired
    private HealthCheckConsentService healthCheckConsentService;

    @Autowired
    private DiseaseService diseaseService;

    @Override
    public HealCheckCampaign getHealthCheckCampaignById() {
        return null;
    }

    // This method saves a health check campaign and creates consent forms for all pupils
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HealCheckCampaign saveHealthCheckCampaign(HealCheckCampaign healCheckCampaign) {
        HealCheckCampaign campaign = healthCheckCampaignRepo.save(healCheckCampaign);
        List<Pupil> pupils = pupilService.getAll().orElseThrow(() -> new NotFoundException("No pupils found in the system"));

        List<HealthCheckConsentForm> healthCheckConsentForm = new ArrayList<>();
        for (Pupil pupil : pupils) {
            HealthCheckConsentForm consentForm = new HealthCheckConsentForm();
            consentForm.setPupil(pupil);
            consentForm.setHealCheckCampaign(campaign);
            healthCheckConsentForm.add(consentForm);
        }
        healthCheckConsentService.saveAll(healthCheckConsentForm);

        List<HealthCheckDisease> healthCheckDiseases = new ArrayList<>();
        List<Disease> diseases = diseaseService.getAllDiseases().orElseThrow(() -> new NotFoundException("No diseases found in the system"));
        List<HealthCheckConsentForm> consentForms = healthCheckConsentService.getAllHealthCheckConsents();
        for( HealthCheckConsentForm consentForm : consentForms) {
            for (Disease disease : diseases) {
                HealthCheckDisease healthCheckDisease = new HealthCheckDisease();
                healthCheckDisease.setHealthCheckCampaign(campaign);
                healthCheckDisease.setDisease(disease);
                healthCheckDisease.setHealthCheckConsentForm(consentForm);
                healthCheckDiseases.add(healthCheckDisease);
            }
        }
        return campaign;
    }
}
