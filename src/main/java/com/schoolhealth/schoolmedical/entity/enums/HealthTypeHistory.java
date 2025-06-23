package com.schoolhealth.schoolmedical.entity.enums;

/**
 * Enum đại diện cho loại hồ sơ sức khỏe học sinh.
 * ALLERGY: Dị ứng
 * MEDICAL_HISTORY: Tiền sử bệnh
 */
public enum HealthTypeHistory {
    ALLERGY("Dị ứng"),
    MEDICAL_HISTORY("Tiền sử bệnh");

    private final String description;

    HealthTypeHistory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
