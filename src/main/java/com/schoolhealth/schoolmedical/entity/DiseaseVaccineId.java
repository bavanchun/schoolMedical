package com.schoolhealth.schoolmedical.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class DiseaseVaccineId implements Serializable {

    private int diseaseId;
    private int vaccineId;

}
