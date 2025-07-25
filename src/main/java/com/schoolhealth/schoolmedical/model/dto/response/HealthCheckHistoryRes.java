package com.schoolhealth.schoolmedical.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HealthCheckHistoryRes {
    private Integer stage ;
    private Long healthId;
    private BigDecimal height;
    private BigDecimal weight;
    private String rightEyeVision;
    private String leftEyeVision;
    private String bloodPressure;
    private int heartRate;
    private String dentalCheck;
    private String earCondition;
    private String noseCondition;
    private String throatCondition;
    private String skinAndMucosa;
    private String hearAnuscultaion;
    private String chestShape;
    private String lungs;
    private String digestiveSystem;
    private String urinarySystem;
    private String musculoskeletalSystem;
    private String neurologyAndPsychiatry;
    private String additionalNotes;
    private String unusualSigns;
    private LocalDate createdAt;
}
