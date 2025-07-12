package com.schoolhealth.schoolmedical.service.parentrecord;

import com.schoolhealth.schoolmedical.model.dto.request.ParentHealthRecordRequest;
import com.schoolhealth.schoolmedical.model.dto.response.ParentHealthRecordResponse;

import java.util.List;

/**
 * Service interface defining CRUD operations for parent managed health records.
 */
public interface ParentHealthRecordService {
    /**
     * Create a new health record for a child.
     */
    ParentHealthRecordResponse createParentHealthRecord(String parentId, ParentHealthRecordRequest request);

    /**
     * Retrieve all health records for a specific pupil id.
     */
    List<ParentHealthRecordResponse> getAllParentHealthRecordsByPupil(String pupilId);

    /**
     * Update an existing record by id.
     */
    ParentHealthRecordResponse updateParentHealthRecord(Long id, String parentId, ParentHealthRecordRequest request);

    /**
     * Soft delete a record by id.
     */
    void deleteParentHealthRecord(Long id, String parentId);
}
