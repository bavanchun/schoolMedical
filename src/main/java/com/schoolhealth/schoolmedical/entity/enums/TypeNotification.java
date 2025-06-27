package com.schoolhealth.schoolmedical.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TypeNotification {
    MED_EVENT("Sự kiện y tế"),
    HEALTH_CHECK_CAMPAGIN("Kiểm tra sức khỏe"),
    SEND_MEDICAL("Gửi thuốc"),
    VACCINATION_CAMPAIGN("Chiến dịch tiêm chủng");
    private final String description;
    TypeNotification(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
    @JsonCreator
    public static TypeNotification fromString(String value) {
        for (TypeNotification type : TypeNotification.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown TypeNotification: " + value);
    }
}
