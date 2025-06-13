package com.schoolhealth.schoolmedical.service.grade;

import com.schoolhealth.schoolmedical.entity.Grade;

import java.util.List;

public interface GradeService {
    Grade createGrade(Grade grade);
    List<Grade> getAllGrades();
    Grade getGradeById(Long id);
    Grade updateGrade(Long id, Grade grade);
    void deleteGrade(Long id);
}
