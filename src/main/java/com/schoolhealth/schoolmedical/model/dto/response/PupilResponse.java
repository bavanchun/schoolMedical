package com.schoolhealth.schoolmedical.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PupilResponse {
    private String pupilId;
    private String lastName;
    private String firstName;
    private LocalDate birthDate;
    private char gender;
    private boolean isActive;
    private String gradeName;

}
