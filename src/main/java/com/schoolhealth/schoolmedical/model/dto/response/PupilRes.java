package com.schoolhealth.schoolmedical.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.schoolhealth.schoolmedical.entity.enums.GradeLevel;
import lombok.*;

import java.time.LocalDate;

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
    private String gender;
    private String avatar;
    private Long gradeId;
    @JsonInclude(value = JsonInclude.Include.NON_DEFAULT)
    private int startYear;
    private GradeLevel gradeLevel;
    private String gradeName;
    private String parentPhoneNumber;
}


