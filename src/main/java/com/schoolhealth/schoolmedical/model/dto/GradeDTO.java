package com.schoolhealth.schoolmedical.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.schoolhealth.schoolmedical.entity.enums.GradeLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long gradeId;
    private String gradeName;
    private String startYear;
    private String endYear;
    private GradeLevel gradeLevel;
}
