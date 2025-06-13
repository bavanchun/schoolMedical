package com.schoolhealth.schoolmedical.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Tên không được để trống")
    private String firstName;

    @NotBlank(message = "Họ không được để trống")
    private String lastName;

    @Email(message = "Email không hợp lệ")
    private String email;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthDate;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = ValidationConstants.PHONE_NUMBER_REGEX, message = "Số điện thoại không hợp lệ")
    private String phoneNumber;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;

    private String avatar;
}
