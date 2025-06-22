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
public class PupilGradeId implements Serializable {
    private String pupilId;
    private Long gradeId;
}
