package com.schoolhealth.schoolmedical.entity.enums;

public enum StatusSendMedication {
    PENDING("Đang chờ"),
    APPROVED("Đã duyệt"),
    REJECTED("Đã từ chối");

    private final String description;

    StatusSendMedication(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
