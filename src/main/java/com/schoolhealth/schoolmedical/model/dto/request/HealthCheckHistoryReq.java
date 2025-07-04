package com.schoolhealth.schoolmedical.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HealthCheckHistoryReq {
    @NotNull
    private BigDecimal height;
    @NotNull
    private BigDecimal weight;
    @NotBlank
    private String rightEyeVision;
    @NotBlank
    private String leftEyeVision;
    @NotBlank
    private String bloodPressure;
    @NotNull
    private int heartRate;
    @NotBlank
    private String dentalCheck;
    @NotBlank
    private String earCondition;
    @NotBlank
    private String noseCondition;
    @NotBlank
    private String throatCondition;
    @NotBlank
    private String skinAndMucosa;
    @NotBlank
    private String hearAnuscultaion;
    @NotBlank
    private String chestShape;
    @NotBlank
    private String lungs;
    @NotBlank
    private String digestiveSystem;
    @NotBlank
    private String urinarySystem;
    @NotBlank
    private String musculoskeletalSystem;
    @NotBlank
    private String neurologyAndPsychiatry;
    @NotBlank
    private String additionalNotes;
    @NotBlank
    private String unusualSigns;
    private List<ConsentDiseaseReq> diseases;
}
