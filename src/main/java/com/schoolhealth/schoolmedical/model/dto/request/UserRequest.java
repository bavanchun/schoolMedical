package com.schoolhealth.schoolmedical.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.schoolhealth.schoolmedical.constant.ValidationConstants;
import com.schoolhealth.schoolmedical.entity.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @PastOrPresent(message = ValidationConstants.BIRTH_DATE_MESSAGE)
    private LocalDate birthDate;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = ValidationConstants.PHONE_NUMBER_REGEX,
            message = ValidationConstants.PHONE_NUMBER_MESSAGE)
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    private String password;

    private String avatar;

    private Role role;
}
