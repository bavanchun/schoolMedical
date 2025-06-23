package com.schoolhealth.schoolmedical.entity;

/**
 * Enum đại diện cho loại hồ sơ sức khỏe học sinh:
 * ALLERGY = Dị ứng, MEDICAL_HISTORY = Bệnh mãn tính/tiền sử điều trị.
 * Có thể mở rộng nếu muốn (ví dụ: VISION, HEARING...)
 */
public enum ConditionType {
    ALLERGY,
    MEDICAL_HISTORY
}
