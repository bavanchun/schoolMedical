package com.schoolhealth.schoolmedical.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum StatusMedLogs {
    GIVEN("Đã cho thuốc"),
    NOTGIVEN("Chưa cho thuốc");
    private final String description;
    StatusMedLogs(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
    @JsonCreator
    public static StatusMedLogs fromString(String value) {
        if (value == null) {
            return null;
        }
        for (StatusMedLogs status : StatusMedLogs.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status value: " + value);
    }
}
