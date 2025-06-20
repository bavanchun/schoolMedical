package com.schoolhealth.schoolmedical.service.vaccine;

import com.schoolhealth.schoolmedical.model.dto.request.VaccineRequest;
import com.schoolhealth.schoolmedical.model.dto.response.VaccineResponse;
import org.springframework.data.domain.Page;

public interface VaccineService {
    VaccineResponse createVaccine(VaccineRequest request);
    VaccineResponse updateVaccine(Long id, VaccineRequest request);
    void deleteVaccine(Long id);
    VaccineResponse getVaccineById(Long id);
    Page<VaccineResponse> getAllVaccines(int pageNo, int pageSize, boolean isActive);
}
