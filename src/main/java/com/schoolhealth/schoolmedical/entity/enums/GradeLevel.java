package com.schoolhealth.schoolmedical.entity.enums;




public enum GradeLevel {
    GRADE_1("1"),
    GRADE_2("2"),
    GRADE_3("3"),
    GRADE_4("4"),
    GRADE_5("5");

    private final String value;

    GradeLevel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    public static GradeLevel fromValue(String value) {
        for (GradeLevel level : GradeLevel.values()) {
            if (level.getValue().equals(value)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Invalid grade level: " + value);
    }
}
