package com.schoolhealth.schoolmedical.service.grade;

import com.schoolhealth.schoolmedical.entity.Grade;
import com.schoolhealth.schoolmedical.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class GradeServiceImpl implements GradeService{


    private GradeRepository gradeRepository;

    @Override
    public Grade createGrade(Grade grade) {
        return gradeRepository.save(grade);
    }

    @Override
    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    @Override
    public Grade getGradeById(Long id) {
        return gradeRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade not found"));
    }

    @Override
    public Grade updateGrade(Long id, Grade grade) {
        Grade existing = getGradeById(id);
        existing.setGradeName(grade.getGradeName());
        existing.setStartYear(grade.getStartYear());
        existing.setEndYear(grade.getEndYear());
        existing.setGradeLevel(grade.getGradeLevel());
        // note: pupils list typically managed elsewhere
        return gradeRepository.save(existing);
    }

    @Override
    public void deleteGrade(Long id) {
        gradeRepository.delete(getGradeById(id));
    }
}
