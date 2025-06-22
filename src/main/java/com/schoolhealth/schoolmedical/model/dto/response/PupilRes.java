package com.schoolhealth.schoolmedical.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.schoolhealth.schoolmedical.entity.enums.GradeLevel;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PupilRes {
    private String pupilId;
    private String lastName;
    private String firstName;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthDate;
    private char gender;
    private GradeLevel gradeLevel;
    private String gradeName;
}


