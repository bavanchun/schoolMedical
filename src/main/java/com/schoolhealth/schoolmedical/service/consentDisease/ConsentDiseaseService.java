package com.schoolhealth.schoolmedical.service.consentDisease;

import com.schoolhealth.schoolmedical.entity.ConsentDisease;
import com.schoolhealth.schoolmedical.entity.ConsentDiseaseId;
import com.schoolhealth.schoolmedical.model.dto.request.SurveyHealthCheckReq;

import java.util.List;

public interface ConsentDiseaseService {
    void updateConsentDisease(SurveyHealthCheckReq survey);
    void saveConsentDisease(List<ConsentDisease> consentDisease);
    ConsentDisease getConsentDiseaseById(ConsentDiseaseId consentDiseaseId);
}
