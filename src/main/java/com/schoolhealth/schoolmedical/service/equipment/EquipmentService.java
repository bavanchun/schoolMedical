package com.schoolhealth.schoolmedical.service.equipment;

import com.schoolhealth.schoolmedical.entity.Equipment;
import com.schoolhealth.schoolmedical.model.dto.request.EquipmentRequest;
import com.schoolhealth.schoolmedical.model.dto.response.EquipmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EquipmentService {


    EquipmentResponse createEquipment(EquipmentRequest request);


    EquipmentResponse updateEquipment(Long equipmentId, EquipmentRequest request);


    EquipmentResponse getEquipmentById(Long equipmentId);


    List<EquipmentResponse> getAllEquipment();


    Page<EquipmentResponse> getAllEquipmentWithPagination(Pageable pageable);


    List<EquipmentResponse> searchEquipmentByName(String name);


    void deleteEquipment(Long equipmentId);


    List<Equipment> findEquipmentByIds(List<Long> equipmentIds);
}
