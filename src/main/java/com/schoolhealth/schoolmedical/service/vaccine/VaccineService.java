package com.schoolhealth.schoolmedical.service.vaccine;

import com.schoolhealth.schoolmedical.model.dto.request.VaccineRequest;
import com.schoolhealth.schoolmedical.model.dto.response.VaccineResponse;

import java.util.List;

public interface VaccineService {
    VaccineResponse createVaccine(VaccineRequest request);
    VaccineResponse updateVaccine(int id, VaccineRequest request);
    void deleteVaccine(int id);
    VaccineResponse getVaccineById(int id);
    List<VaccineResponse> getAllVaccines();
}
