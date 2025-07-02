package com.schoolhealth.schoolmedical.model.dto.request;

import com.schoolhealth.schoolmedical.constant.ValidationConstants;
import com.schoolhealth.schoolmedical.entity.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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

    private LocalDate birthDate;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = ValidationConstants.PHONE_NUMBER_REGEX,
            message = "Invalid phone number format. Must be a valid Vietnamese phone number")
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    private String password;

    private String avatar;

    private Role role;
}
