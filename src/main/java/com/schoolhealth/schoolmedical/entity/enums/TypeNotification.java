package com.schoolhealth.schoolmedical.entity.enums;

public enum TypeNotification {
    med_event("Sự kiện y tế"),
    health_check_campaign("Kiểm tra sức khỏe"),
    send_medication("Gửi thuốc");
    private final String description;
    TypeNotification(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
}
