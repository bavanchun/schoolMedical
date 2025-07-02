package com.schoolhealth.schoolmedical.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum StatusSendMedication {
    PENDING("Đang chờ"),
    APPROVED("Đã duyệt"),
    IN_PROGRESS("Đang thực hiện"),
    COMPLETED("Đã hoàn thành"),
    REJECTED("Đã từ chối");

    private final String description;

    StatusSendMedication(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static StatusSendMedication fromString(String key) {
        for (StatusSendMedication status : StatusSendMedication.values()) {
            if (status.name().equalsIgnoreCase(key)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + key);
    }
}
