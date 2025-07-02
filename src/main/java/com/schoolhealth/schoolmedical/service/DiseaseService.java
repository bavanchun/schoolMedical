package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.entity.Disease;
import com.schoolhealth.schoolmedical.model.dto.request.DiseaseRequest;
import com.schoolhealth.schoolmedical.model.dto.request.DiseaseVaccineRequest;
import com.schoolhealth.schoolmedical.model.dto.response.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface DiseaseService {
    DiseaseResponse createDisease(DiseaseRequest request);
    DiseaseResponse updateDisease(Long id, DiseaseRequest request);
    void deleteDisease(Long id);
    DiseaseResponse getDiseaseById(Long id);
//    List<DiseaseResponse> getAllDiseases();
    Page<DiseaseResponse> getAllDiseases(int pageNo, int pageSize, Boolean isActive);

    DiseaseVaccineResponse assignVaccineToDisease(DiseaseVaccineRequest request);

    List<VaccineResponse> getVaccinesByDiseaseId(Long diseaseId);

    DiseaseVaccineResponse removeVaccineFromDisease(DiseaseVaccineRequest request);
    DiseaseWithVaccinesWrapper getAllDiseasesWithVaccines();

    List<ConsentDiseaseRes> getAllDiseasesByisInjectedVaccinationFalse();
    List<Disease> getAllDiseasesById(List<Long> diseaseIds);
    Disease getDiseaseEntityById( Long diseaseId);
}
