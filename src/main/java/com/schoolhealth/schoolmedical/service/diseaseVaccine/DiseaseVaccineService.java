package com.schoolhealth.schoolmedical.service.diseaseVaccine;

import com.schoolhealth.schoolmedical.model.dto.request.DiseaseVaccineRequest;
import com.schoolhealth.schoolmedical.model.dto.response.DiseaseVaccineResponse;
import com.schoolhealth.schoolmedical.model.dto.response.VaccineResponse;

import java.util.List;

public interface DiseaseVaccineService {
    DiseaseVaccineResponse create(DiseaseVaccineRequest request);
    void delete(int diseaseId, int vaccineId);
    List<DiseaseVaccineResponse> getAll();
    List<VaccineResponse> getVaccinesByDiseaseId(int diseaseId);
}
