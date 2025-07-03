package com.schoolhealth.schoolmedical.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePupilRequest {

    @NotBlank(message = "ID học sinh không được để trống")
    @Size(max = 20, message = "ID học sinh không được quá 20 ký tự")
    private String pupilId;

    @NotBlank(message = "Họ không được để trống")
    @Size(max = 50, message = "Họ không được quá 50 ký tự")
    private String lastName;

    @NotBlank(message = "Tên không được để trống")
    @Size(max = 50, message = "Tên không được quá 50 ký tự")
    private String firstName;

    @NotNull(message = "Ngày sinh không được để trống")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @NotNull(message = "Giới tính không được để trống")
    @Pattern(regexp = "^[MF]$", message = "Giới tính chỉ nhận giá trị M (nam) hoặc F (nữ)")
    private String gender;

    @Size(max = 255, message = "URL avatar không được quá 255 ký tự")
    private String avatar;

    @Size(max = 15, message = "Số điện thoại phụ huynh không được quá 15 ký tự")
    @Pattern(regexp = "^(0|\\+84)(3[2-9]|5[6|8|9]|7[0|6-9]|8[1-6|8|9]|9[0-9])[0-9]{7}$",
             message = "Số điện thoại phụ huynh không hợp lệ")
    private String parentPhoneNumber;
}
