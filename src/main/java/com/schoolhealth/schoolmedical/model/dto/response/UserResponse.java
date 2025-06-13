package com.schoolhealth.schoolmedical.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.schoolhealth.schoolmedical.entity.enums.Role;
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
public class UserResponse {
    private String userId;
    private String lastName;
    private String firstName;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthDate;

    private String email;
    private int phoneNumber;
    private String avatar;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate createdAt;

    private Role role;
    private boolean isActive;

    // Chỉ trả về các ID của các quan hệ thay vì toàn bộ object
    private List<String> blogIds;
    private List<String> pupilIds;
}
