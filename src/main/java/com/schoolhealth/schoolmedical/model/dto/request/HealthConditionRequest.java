package com.schoolhealth.schoolmedical.model.dto.request;

import lombok.Data;

/**
 * DTO nhận dữ liệu từ client khi tạo hoặc cập nhật hồ sơ sức khỏe học sinh.
 */
@Data
public class HealthConditionRequest {
    private String name;
    private String reactionOrNote;
    private String imageUrl;
    private String typeHistory; // "ALLERGY" hoặc "MEDICAL_HISTORY"
    private String pupilId;     // Đúng kiểu của trường pupilId trong Entity Pupil
}
