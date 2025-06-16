package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.entity.HealthCheckDisease;
import com.schoolhealth.schoolmedical.repository.HealthCheckDiseaseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HealthCheckDiseaseImpl implements HealthCheckDiseaseService{

    @Autowired
    private HealthCheckDiseaseRepo healthCheckDiseaseRepo;

    @Override
    public List<HealthCheckDisease> saveHealthCheckDisease(List<HealthCheckDisease> healthCheckDisease) {
        return healthCheckDiseaseRepo.saveAll(healthCheckDisease);
    }
}
