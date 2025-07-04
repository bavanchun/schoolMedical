package com.schoolhealth.schoolmedical.model.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class QuantityPupilForSessionRes {
    private String session;
    private List<QuantityPupilByGradeRes> quantityPupilByGrade;
}
