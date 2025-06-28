package com.schoolhealth.schoolmedical.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.schoolhealth.schoolmedical.constant.ValidationConstants;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PupilDto {
    private String pupilId;
    private String lastName;
    private String firstName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = ValidationConstants.BIRTH_DATE_MESSAGE)
    private LocalDate birthDate;

    private char gender;

    @Pattern(regexp = ValidationConstants.PHONE_NUMBER_REGEX, message = ValidationConstants.PHONE_NUMBER_MESSAGE)
    private String parentPhoneNumber;

    private boolean isActive;
}
