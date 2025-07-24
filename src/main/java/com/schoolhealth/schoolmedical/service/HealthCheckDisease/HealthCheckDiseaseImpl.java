package com.schoolhealth.schoolmedical.service.HealthCheckDisease;

import com.schoolhealth.schoolmedical.entity.HealthCheckDisease;
import com.schoolhealth.schoolmedical.repository.HealthCheckDiseaseRepo;
import com.schoolhealth.schoolmedical.service.HealthCheckCampaign.HealthCheckCampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HealthCheckDiseaseImpl implements HealthCheckDiseaseService {

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
