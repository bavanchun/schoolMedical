package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.entity.Disease;
import com.schoolhealth.schoolmedical.model.dto.request.DiseaseRequest;
import com.schoolhealth.schoolmedical.model.dto.request.DiseaseVaccineRequest;
import com.schoolhealth.schoolmedical.model.dto.response.DiseaseResponse;
import com.schoolhealth.schoolmedical.model.dto.response.DiseaseVaccineResponse;
import com.schoolhealth.schoolmedical.model.dto.response.VaccineResponse;
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

    /**
     * Assign a vaccine to a disease
     *
     * @param request Contains diseaseId and vaccineId
     * @return DiseaseVaccineResponse with information about the operation result
     */
    DiseaseVaccineResponse assignVaccineToDisease(DiseaseVaccineRequest request);

    /**
     * Get list of all vaccines assigned to a disease
     *
     * @param diseaseId ID of the disease
     * @return List of vaccines assigned to the disease
     */
    List<VaccineResponse> getVaccinesByDiseaseId(Long diseaseId);

    /**
     * Remove a vaccine from a disease
     *
     * @param request Contains diseaseId and vaccineId
     * @return DiseaseVaccineResponse with information about the operation result
     */
    DiseaseVaccineResponse removeVaccineFromDisease(DiseaseVaccineRequest request);

    List<Disease> getAllDiseases();
}
