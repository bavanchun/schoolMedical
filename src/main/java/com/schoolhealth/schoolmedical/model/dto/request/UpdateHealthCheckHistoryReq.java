package com.schoolhealth.schoolmedical.model.dto.request;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateHealthCheckHistoryReq {
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
    private List<ConsentDiseaseReq> diseases;
}
