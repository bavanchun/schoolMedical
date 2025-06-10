package com.schoolhealth.schoolmedical.entity.enums;

public enum StatusMedLogs {
    Given("Đã cho thuốc"),
    NotGiven("Chưa cho thuốc");
    private final String description;
    StatusMedLogs(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
}
