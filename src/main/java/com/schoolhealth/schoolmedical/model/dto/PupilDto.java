package com.schoolhealth.schoolmedical.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class   PupilDto {
    private Integer id;
    private String lastName;
    private String firstName;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthDate;
    private char gender;
    private String className;
}


