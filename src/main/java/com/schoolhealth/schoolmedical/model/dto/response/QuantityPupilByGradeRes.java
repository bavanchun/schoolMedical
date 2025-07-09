package com.schoolhealth.schoolmedical.model.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class QuantityPupilByGradeRes {
    private Long grade;
    private Long quantity;
}
