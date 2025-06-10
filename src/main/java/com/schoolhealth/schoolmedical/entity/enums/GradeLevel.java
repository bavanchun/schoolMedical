package com.schoolhealth.schoolmedical.entity.enums;

public enum GradeLevel {
    GRADE_1("Lớp 1"),
    GRADE_2("Lớp 2"),
    GRADE_3("Lớp 3"),
    GRADE_4("Lớp 4"),
    GRADE_5("Lớp 5");

    private final String value;

    GradeLevel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
