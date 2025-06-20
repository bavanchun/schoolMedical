package com.schoolhealth.schoolmedical.service.disease;

import com.schoolhealth.schoolmedical.model.dto.request.DiseaseRequest;
import com.schoolhealth.schoolmedical.model.dto.response.DiseaseResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DiseaseService {
    DiseaseResponse createDisease(DiseaseRequest request);
    DiseaseResponse updateDisease(int id, DiseaseRequest request);
    void deleteDisease(int id);
    DiseaseResponse getDiseaseById(int id);
    List<DiseaseResponse> getAllDiseases();
    Page<DiseaseResponse> getAllDiseases(int pageNo, int pageSize, boolean isActive);
}
