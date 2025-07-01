package com.schoolhealth.schoolmedical.service.equipment;

import com.schoolhealth.schoolmedical.model.dto.request.EquipmentRequest;
import com.schoolhealth.schoolmedical.model.dto.response.EquipmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EquipmentService {

    /**
     * Create new equipment
     * @param request Equipment data
     * @return Created equipment response
     */
    EquipmentResponse createEquipment(EquipmentRequest request);

    /**
     * Update existing equipment
     * @param equipmentId Equipment ID
     * @param request Updated equipment data
     * @return Updated equipment response
     */
    EquipmentResponse updateEquipment(Long equipmentId, EquipmentRequest request);

    /**
     * Get equipment by ID
     * @param equipmentId Equipment ID
     * @return Equipment response
     */
    EquipmentResponse getEquipmentById(Long equipmentId);

    /**
     * Get all active equipment
     * @return List of equipment responses
     */
    List<EquipmentResponse> getAllEquipment();

    /**
     * Get all active equipment with pagination
     * @param pageable Pagination information
     * @return Page of equipment responses
     */
    Page<EquipmentResponse> getAllEquipmentWithPagination(Pageable pageable);

    /**
     * Search equipment by name
     * @param name Equipment name to search
     * @return List of matching equipment responses
     */
    List<EquipmentResponse> searchEquipmentByName(String name);

    /**
     * Soft delete equipment (set isActive = false)
     * @param equipmentId Equipment ID to delete
     */
    void deleteEquipment(Long equipmentId);
}
