package com.schoolhealth.schoolmedical.service.vaccinationHistory;

import com.schoolhealth.schoolmedical.entity.VaccinationConsentForm;
import com.schoolhealth.schoolmedical.model.dto.request.BulkVaccinationHistoryRequest;
import com.schoolhealth.schoolmedical.model.dto.request.VaccinationHistoryRequest;
import com.schoolhealth.schoolmedical.model.dto.response.BulkVaccinationHistoryResponse;
import com.schoolhealth.schoolmedical.model.dto.response.VaccinationHistoryResponse;

import java.util.List;

public interface VaccinationHistoryService {

    VaccinationHistoryResponse createFromCampaign(VaccinationConsentForm consentForm);


    VaccinationHistoryResponse createParentDeclaration(VaccinationHistoryRequest request, String parentUserId);

    BulkVaccinationHistoryResponse createBulkParentDeclaration(BulkVaccinationHistoryRequest request, String parentUserId);

    @Deprecated
    VaccinationHistoryResponse confirmParentDeclaration(Long historyId, boolean approved);


    List<VaccinationHistoryResponse> getPupilVaccinationHistory(String pupilId);


    VaccinationHistoryResponse getHistoryById(Long historyId);


    VaccinationHistoryResponse updateHistory(Long historyId, VaccinationHistoryRequest request);


    boolean hasCompletedVaccination(String pupilId, Long diseaseId, int requiredDoses);

    @Deprecated
    List<VaccinationHistoryResponse> getPendingParentDeclarations();

    @Deprecated
    List<VaccinationHistoryResponse> getPendingParentDeclarationsByPupil(String pupilId);
}
