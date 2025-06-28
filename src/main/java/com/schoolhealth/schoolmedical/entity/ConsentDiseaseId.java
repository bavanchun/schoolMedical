package com.schoolhealth.schoolmedical.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@Embeddable
public class ConsentDiseaseId {
    private Long consentFormId;
    private Long diseaseId;
}
