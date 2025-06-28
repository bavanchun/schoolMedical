package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.entity.HealthCheckCampaign;
import com.schoolhealth.schoolmedical.entity.HealthCheckDisease;
import com.schoolhealth.schoolmedical.entity.enums.HealthCheckDiseaseStatus;
import com.schoolhealth.schoolmedical.entity.enums.StatusHealthCampaign;
import com.schoolhealth.schoolmedical.exception.UpdateNotAllowedException;
import com.schoolhealth.schoolmedical.model.dto.request.SurveyHealthCheckReq;
import com.schoolhealth.schoolmedical.repository.HealthCheckDiseaseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HealthCheckDiseaseImpl implements HealthCheckDiseaseService{

    @Autowired
    private HealthCheckDiseaseRepo healthCheckDiseaseRepo;
    @Autowired
    private HealthCheckCampaignService healthCheckCampaignService;

    @Override
    public List<HealthCheckDisease> saveHealthCheckDisease(List<HealthCheckDisease> healthCheckDisease) {
        return healthCheckDiseaseRepo.saveAll(healthCheckDisease);
    }

    @Override
    public void updateHealthCheckDiseaseStatus(SurveyHealthCheckReq survey) {
        HealthCheckCampaign healthCheckCampaign = healthCheckCampaignService.getHealthCheckCampaignEntityById(survey.getCampaignId());
        if(healthCheckCampaign.getStatusHealthCampaign() == StatusHealthCampaign.PUBLISHED){
            List<HealthCheckDisease> healthCheckDiseases = healthCheckDiseaseRepo.findByCampaignIdAndPupilId(survey.getCampaignId(), survey.getPupilId());
            for(HealthCheckDisease disease : healthCheckDiseases) {
                if (survey.getDiseaseId().contains(disease.getDisease().getDiseaseId())) {
                    disease.setStatus(HealthCheckDiseaseStatus.APPROVED);
                }
            }
            healthCheckDiseaseRepo.saveAll(healthCheckDiseases);
        }else
        {
            throw new UpdateNotAllowedException("The health check campaign has expired or been canceled.");
        }

    }
}
