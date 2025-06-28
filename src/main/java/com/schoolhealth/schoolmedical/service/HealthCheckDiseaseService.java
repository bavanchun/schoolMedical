package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.entity.HealthCheckDisease;
import com.schoolhealth.schoolmedical.model.dto.request.SurveyHealthCheckReq;

import java.util.List;

public interface HealthCheckDiseaseService {
    List<HealthCheckDisease> saveHealthCheckDisease(List<HealthCheckDisease> healthCheckDisease);

}
