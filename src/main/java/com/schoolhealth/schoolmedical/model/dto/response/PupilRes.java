package com.schoolhealth.schoolmedical.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.schoolhealth.schoolmedical.entity.enums.GradeLevel;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PupilRes {
    private String pupilId;
    private String lastName;
    private String firstName;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthDate;
    private char gender;
    private String avatar;
    private Long gradeId;
    private int startYear;
    private GradeLevel gradeLevel;
    private String gradeName;
    private List<UserResponse> parents;
}


