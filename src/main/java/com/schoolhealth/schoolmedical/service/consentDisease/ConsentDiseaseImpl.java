package com.schoolhealth.schoolmedical.service.consentDisease;

import com.schoolhealth.schoolmedical.entity.ConsentDisease;
import com.schoolhealth.schoolmedical.entity.ConsentDiseaseId;
import com.schoolhealth.schoolmedical.entity.Disease;
import com.schoolhealth.schoolmedical.entity.HealthCheckConsentForm;
import com.schoolhealth.schoolmedical.exception.NotFoundException;
import com.schoolhealth.schoolmedical.model.dto.request.SurveyHealthCheckReq;
import com.schoolhealth.schoolmedical.repository.ConsentDiseaseRepo;
import com.schoolhealth.schoolmedical.service.DiseaseService;
import com.schoolhealth.schoolmedical.service.HealthCheckCampaignService;
import com.schoolhealth.schoolmedical.service.HealthCheckConsentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConsentDiseaseImpl implements ConsentDiseaseService{
    @Autowired
    private HealthCheckCampaignService healthCheckCampaignService;
    @Autowired
    private HealthCheckConsentService healthCheckConsentService;
    @Autowired
    private ConsentDiseaseRepo consentDiseaseRepo;
    @Autowired
    private DiseaseService diseaseService;
//    public void updateConsentDisease(SurveyHealthCheckReq survey) {
//        HealthCheckConsentForm consentForm = healthCheckConsentService.getHealthCheckConsentByPupilIdAndCampaignId(survey.getPupilId(), survey.getCampaignId());
//        if(consentForm ==null)
//            throw new NotFoundException("Consent disease not found for student ID: " + survey.getPupilId() + " and campaign ID: " + survey.getCampaignId());
//        List<ConsentDisease> existingDisease = consentForm.getConsentDiseases();
//        List<Disease> diseases = diseaseService.getAllDiseasesById(survey.getDiseaseId());
//        if(existingDisease == null || existingDisease.isEmpty()) {
//            existingDisease = diseases.stream()
//                    .map(disease -> {
//                        ConsentDiseaseId id = new ConsentDiseaseId();
//                        id.setConsentFormId(consentForm.getConsentFormId());
//                        id.setDiseaseId(disease.getDiseaseId());
//
//                        return ConsentDisease.builder()
//                                .id(id)
//                                .disease(disease)
//                                .healthCheckConsentForm(consentForm)
//                                .build();
//                    })
//                    .toList();
//            consentDiseaseRepo.saveAll(existingDisease);
//        }else{
//           // existingDisease.clear();
//            consentDiseaseRepo.deleteByConsentForm(consentForm);
//            //consentForm.setConsentDiseases(existingDisease);
//            //healthCheckConsentService.saveHealthCheckConsentForm(consentForm);
//            List<ConsentDisease> newConsentDiseases = diseases.stream()
//                    .map(disease -> {
//                        ConsentDiseaseId id = new ConsentDiseaseId();
//                        id.setConsentFormId(consentForm.getConsentFormId());
//                        id.setDiseaseId(disease.getDiseaseId());
//
//                        return ConsentDisease.builder()
//                                .id(id)
//                                .disease(disease)
//                                .healthCheckConsentForm(consentForm)
//                                .build();
//                    })
//                    .toList();
//            existingDisease.addAll(newConsentDiseases);
//            consentDiseaseRepo.saveAll(newConsentDiseases);
//        }
//    }
    @Transactional
    public void updateConsentDisease(SurveyHealthCheckReq survey) {
        HealthCheckConsentForm consentForm = healthCheckConsentService.getHealthCheckConsentByPupilIdAndCampaignId(survey.getPupilId(), survey.getCampaignId());
        if(consentForm == null)
            throw new NotFoundException("Consent disease not found for student ID: " + survey.getPupilId() + " and campaign ID: " + survey.getCampaignId());

        List<Disease> diseases = diseaseService.getAllDiseasesById(survey.getDiseaseId());

        // Delete existing consent diseases first
        consentDiseaseRepo.deleteByConsentForm(consentForm);

        // Create new consent diseases
        List<ConsentDisease> newConsentDiseases = diseases.stream()
                .map(disease -> {
                    ConsentDiseaseId id = new ConsentDiseaseId();
                    id.setConsentFormId(consentForm.getConsentFormId());
                    id.setDiseaseId(disease.getDiseaseId());

                    return ConsentDisease.builder()
                            .id(id)
                            .disease(disease)
                            .healthCheckConsentForm(consentForm)
                            .build();
                })
                .toList();

        // Save new consent diseases
        consentDiseaseRepo.saveAll(newConsentDiseases);
    }

    @Override
    public void saveConsentDisease(List<ConsentDisease> consentDisease) {
        consentDiseaseRepo.saveAll(consentDisease);
    }

    @Override
    public ConsentDisease getConsentDiseaseById(ConsentDiseaseId consentDiseaseId) {
        return consentDiseaseRepo.findById(consentDiseaseId)
                .orElseThrow(() -> new NotFoundException("Consent disease not found with ID: " + consentDiseaseId));
    }
}
