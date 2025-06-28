package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.entity.HealthCheckCampaign;
import com.schoolhealth.schoolmedical.entity.HealthCheckDisease;
import com.schoolhealth.schoolmedical.entity.enums.DiseaseConsentStatus;
import com.schoolhealth.schoolmedical.entity.enums.StatusHealthCampaign;
import com.schoolhealth.schoolmedical.exception.UpdateNotAllowedException;
import com.schoolhealth.schoolmedical.model.dto.request.SurveyHealthCheckReq;
import com.schoolhealth.schoolmedical.repository.HealthCheckDiseaseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HealthCheckDiseaseImpl implements HealthCheckDiseaseService{

    @Autowired
    private HealthCheckDiseaseRepo healthCheckDiseaseRepo;
    @Autowired
    @Lazy
    private HealthCheckCampaignService healthCheckCampaignService;

    @Override
    public List<HealthCheckDisease> saveHealthCheckDisease(List<HealthCheckDisease> healthCheckDisease) {
        return healthCheckDiseaseRepo.saveAll(healthCheckDisease);
    }


}
