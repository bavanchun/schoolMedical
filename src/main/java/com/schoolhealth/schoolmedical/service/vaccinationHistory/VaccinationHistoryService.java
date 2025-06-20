package com.schoolhealth.schoolmedical.service.vaccinationHistory;

import com.schoolhealth.schoolmedical.model.dto.request.VaccinationHistoryRequest;
import com.schoolhealth.schoolmedical.model.dto.response.VaccinationHistoryResponse;

import java.util.List;

public interface VaccinationHistoryService {
    VaccinationHistoryResponse create(VaccinationHistoryRequest request);
    VaccinationHistoryResponse update(int historyId, VaccinationHistoryRequest request);
      void delete(int historyId);
    VaccinationHistoryResponse getById(int historyId);
    List<VaccinationHistoryResponse> getAll();
    List<VaccinationHistoryResponse> getByPupil(String pupilId);
}
