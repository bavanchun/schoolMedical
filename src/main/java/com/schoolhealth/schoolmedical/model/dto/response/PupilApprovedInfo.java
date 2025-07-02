package com.schoolhealth.schoolmedical.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PupilApprovedInfo {
    private String pupilId;
    private Long classId;
    private LocalDate dateOfBirth;
    private String gender;
    private String lastName;
    private String firstName;
    private String avatar;
    private Boolean isActive;
    private Long consentFormId;
    private Long campaignId;
    private Long vaccineId;
    private LocalDateTime respondedAt;
    private String status;
    private Integer Grade; // Note: Capital G to match JSON
}
