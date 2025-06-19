package com.schoolhealth.schoolmedical.model.dto.response;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DiseaseResponse {
    private Long diseaseId;
    private String diseaseName;
    private String description;
    private String symptoms;
    private String treatment;
    private String prevention;
    private String imageUrl;

}
