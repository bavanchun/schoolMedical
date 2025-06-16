package com.schoolhealth.schoolmedical.model.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HealthCheckCampaginReq {
    @NotBlank
    private String address;
    @NotBlank
    private String description;
    @NotNull
    private LocalDate deadlineDate;
    @NotNull
    private LocalDateTime startExaminationDate;
    @NotNull
    private LocalDateTime endExaminationDate;

}