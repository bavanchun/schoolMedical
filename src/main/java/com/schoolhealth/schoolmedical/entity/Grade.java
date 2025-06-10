package com.schoolhealth.schoolmedical.entity;

import com.schoolhealth.schoolmedical.entity.enums.GradeLevel;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
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
    private Date startYear;

    @Column(name = "end_year", nullable = false)
    private Date endYear;

    @Enumerated(EnumType.STRING)
    private GradeLevel gradeLevel;

    @OneToMany(
            mappedBy = "grade",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Pupil> pupils;

}
