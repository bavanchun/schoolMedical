package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.entity.HealthCheckDisease;
import com.schoolhealth.schoolmedical.repository.HealthCheckDiseaseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HealthCheckDiseaseImpl implements HealthCheckDiseaseService{

    @Autowired
    private HealthCheckDiseaseRepo healthCheckDiseaseRepo;

    @Override
    public HealthCheckDisease saveHealthCheckDisease(HealthCheckDisease healthCheckDisease) {
        return healthCheckDiseaseRepo.save(healthCheckDisease);
    }
}
