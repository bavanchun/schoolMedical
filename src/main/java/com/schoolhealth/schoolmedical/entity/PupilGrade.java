package com.schoolhealth.schoolmedical.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "pupil_grade")
public class PupilGrade {
    @EmbeddedId
    private PupilGradeId pupilGradeId;
    @Column(name = "start_year", nullable = false)
    private int startYear;

    @Column(name = "grade_name", nullable = false, length = 50)
    private String gradeName;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("pupilId")
    @JoinColumn(name = "pupil_id", nullable = false)
    private Pupil pupil;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("gradeId")
    @JoinColumn(name = "grade_id", nullable = false)
    private Grade grade;
}
