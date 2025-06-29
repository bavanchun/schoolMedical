package com.schoolhealth.schoolmedical.model.dto.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HealthCheckCampaginReq {
    @NotBlank
    private String address;
    private String title;
    @NotBlank
    private String description;
    @NotNull
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate deadlineDate;
    @NotNull
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime startExaminationDate;
    @NotNull
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime endExaminationDate;
    private List<Long> diseaseIds;

}