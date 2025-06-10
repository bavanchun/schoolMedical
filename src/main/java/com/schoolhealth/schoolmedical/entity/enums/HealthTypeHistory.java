package com.schoolhealth.schoolmedical.entity.enums;

public enum HealthTypeHistory {
    ALLERGY("Dị ứng"),
    MEDICAL_HISTORY("Tiền sử bệnh"),;
    private final String description;
    HealthTypeHistory(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
}
