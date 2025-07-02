package com.schoolhealth.schoolmedical.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@Embeddable
public class ConsentDiseaseId implements Serializable {
    private Long consentFormId;
    private Long diseaseId;
}
