package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.model.dto.request.HealthConditionRequest;
import com.schoolhealth.schoolmedical.model.dto.response.HealthConditionResponse;

import java.util.List;

/**
 * Interface định nghĩa các dịch vụ xử lý logic nghiệp vụ cho hồ sơ sức khỏe học sinh.
 */
public interface HealthConditionService {
    /**
     * Thêm mới hồ sơ sức khỏe cho học sinh.
     */
    HealthConditionResponse create(HealthConditionRequest request);

    /**
     * Lấy danh sách hồ sơ sức khỏe của học sinh theo pupilId.
     */
    List<HealthConditionResponse> getAllByPupil(String pupilId);

    /**
     * Sửa thông tin hồ sơ sức khỏe.
     */
    HealthConditionResponse update(Long id, HealthConditionRequest request);

    /**
     * Xóa mềm (isActive = false) hồ sơ sức khỏe.
     */
    void delete(Long id);
}