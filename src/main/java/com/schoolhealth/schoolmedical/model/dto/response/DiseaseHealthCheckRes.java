package com.schoolhealth.schoolmedical.model.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DiseaseHealthCheckRes {
    private Long diseaseId;
    private String name;
    private String description;
}
