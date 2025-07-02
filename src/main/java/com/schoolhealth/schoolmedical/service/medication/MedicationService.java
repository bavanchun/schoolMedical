package com.schoolhealth.schoolmedical.service.medication;

import com.schoolhealth.schoolmedical.entity.Medication;
import com.schoolhealth.schoolmedical.model.dto.request.MedicationRequest;
import com.schoolhealth.schoolmedical.model.dto.response.MedicationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MedicationService {


    MedicationResponse createMedication(MedicationRequest request);


    MedicationResponse updateMedication(Long medicationId, MedicationRequest request);


    MedicationResponse getMedicationById(Long medicationId);


    List<MedicationResponse> getAllMedication();


    Page<MedicationResponse> getAllMedicationWithPagination(Pageable pageable);


    List<MedicationResponse> searchMedicationByName(String name);


    void deleteMedication(Long medicationId);


    List<Medication> findMedicationByIds(List<Long> medicationIds);
}
