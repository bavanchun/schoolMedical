package com.schoolhealth.schoolmedical.service.grade;

import com.schoolhealth.schoolmedical.model.dto.GradeDTO;

import java.util.List;

public interface GradeService {
    GradeDTO createGrade(GradeDTO grade);
    List<GradeDTO> getAllGrades();
    GradeDTO getGradeById(Long id);
    GradeDTO updateGrade(Long id, GradeDTO grade);
    void deleteGrade(Long id);
}
