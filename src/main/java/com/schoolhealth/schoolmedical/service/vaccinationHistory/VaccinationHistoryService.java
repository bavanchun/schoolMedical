package com.schoolhealth.schoolmedical.service.vaccinationHistory;

import com.schoolhealth.schoolmedical.entity.VaccinationConsentForm;
import com.schoolhealth.schoolmedical.model.dto.request.VaccinationHistoryRequest;
import com.schoolhealth.schoolmedical.model.dto.response.VaccinationHistoryResponse;

import java.util.List;

public interface VaccinationHistoryService {

    VaccinationHistoryResponse createFromCampaign(VaccinationConsentForm consentForm);


    VaccinationHistoryResponse createParentDeclaration(VaccinationHistoryRequest request, String parentUserId);


    VaccinationHistoryResponse confirmParentDeclaration(Long historyId, boolean approved);


    List<VaccinationHistoryResponse> getPupilVaccinationHistory(String pupilId);


    VaccinationHistoryResponse getHistoryById(Long historyId);


    VaccinationHistoryResponse updateHistory(Long historyId, VaccinationHistoryRequest request);


    boolean hasCompletedVaccination(String pupilId, Long diseaseId, int requiredDoses);
}
