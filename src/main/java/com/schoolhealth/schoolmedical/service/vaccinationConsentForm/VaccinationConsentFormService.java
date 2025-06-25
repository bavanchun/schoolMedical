package com.schoolhealth.schoolmedical.service.vaccinationConsentForm;

import com.schoolhealth.schoolmedical.entity.enums.GradeLevel;
import com.schoolhealth.schoolmedical.model.dto.request.VaccinationConsentFormRequest;
import com.schoolhealth.schoolmedical.model.dto.response.PupilsApprovedByGradeResponse;
import com.schoolhealth.schoolmedical.model.dto.response.VaccinationConsentFormResponse;

import java.util.List;

public interface VaccinationConsentFormService {

    VaccinationConsentFormResponse parentRespond(Long formId, String parentUserId, VaccinationConsentFormRequest request);


    VaccinationConsentFormResponse nurseUpdateStatus(Long formId, VaccinationConsentFormRequest request);

    List<VaccinationConsentFormResponse> getApprovedPupilsByCampaign(Long campaignId);

    List<VaccinationConsentFormResponse> getMyConsentForms(String parentUserId);


    List<VaccinationConsentFormResponse> getConsentFormsByCampaignAndStatus(Long campaignId, String status);


    int updateExpiredConsentForms();
    PupilsApprovedByGradeResponse getPupilsApprovedByGrade(Long campaignId);
    PupilsApprovedByGradeResponse getPupilsApprovedBySpecificGrade(Long campaignId, GradeLevel gradeLevel);
}
