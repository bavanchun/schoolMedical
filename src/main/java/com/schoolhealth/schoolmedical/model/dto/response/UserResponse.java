package com.schoolhealth.schoolmedical.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.schoolhealth.schoolmedical.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    private String userId;
    private String lastName;
    private String firstName;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthDate;

    private String email;
    private String phoneNumber;
    private String avatar;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate createdAt;

    private Role role;
    private boolean active;
}
