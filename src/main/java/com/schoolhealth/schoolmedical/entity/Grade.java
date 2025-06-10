package com.schoolhealth.schoolmedical.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.schoolhealth.schoolmedical.entity.enums.GradeLevel;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "grade")
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grade_id", nullable = false, unique = true)
    private Long gradeId;

    @Column(name = "grade_name", nullable = false, length = 50)
    private String gradeName;

    @Column(name = "start_year", nullable = false)
    private String startYear;

    @Column(name = "end_year", nullable = false)
    private String endYear;

    @Enumerated(EnumType.STRING)
    private GradeLevel gradeLevel;

    @OneToMany(
            mappedBy = "grade",
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            orphanRemoval = true
    )
    private List<Pupil> pupils;

}
