package com.schoolhealth.schoolmedical.model.dto.response;

import com.schoolhealth.schoolmedical.entity.enums.GradeLevel;
import lombok.*;

import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HealthCheckConsentFlatData {
    private Long healthCheckConsentId;
    private int schoolYear;
    private String pupilId;
    private String lastName;
    private String firstName;
    private LocalDate birthDate;
    private char gender;
    private String avatar;
    private String gradeName;
    private Long healthCheckDiseaseId;
    private String diseaseName;
}
