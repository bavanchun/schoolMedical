package com.schoolhealth.schoolmedical.model.dto.response;
import lombok.Data;

/**
 * DTO trả dữ liệu về cho client khi lấy hồ sơ sức khỏe học sinh.
 */
@Data
public class HealthConditionResponse {
    private int conditionId;
    private String name;
    private String reactionOrNote;
    private String imageUrl;
    private String typeHistory;
    private String pupilId;
    private boolean isActive;
}
