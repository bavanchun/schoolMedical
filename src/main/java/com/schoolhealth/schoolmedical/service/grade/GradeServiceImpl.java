package com.schoolhealth.schoolmedical.service.grade;

import com.schoolhealth.schoolmedical.entity.Grade;
import com.schoolhealth.schoolmedical.model.dto.GradeDTO;
import com.schoolhealth.schoolmedical.model.mapper.GradeMapper;
import com.schoolhealth.schoolmedical.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GradeServiceImpl implements GradeService{

    private final GradeRepository gradeRepository;
    private final GradeMapper gradeMapper;

    @Autowired
    public GradeServiceImpl(GradeRepository gradeRepository, GradeMapper gradeMapper) {
        this.gradeRepository = gradeRepository;
        this.gradeMapper   = gradeMapper;
    }

    @Override
    public GradeDTO createGrade(GradeDTO dto) {
        Grade entity = gradeMapper.toEntity(dto);
        Grade saved  = gradeRepository.save(entity);
        return gradeMapper.toDto(saved);
    }

    @Override
    public List<GradeDTO> getAllGrades() {
        return gradeRepository.findAll()
                .stream()
                .map(gradeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public GradeDTO getGradeById(Long id) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade not found"));
        return gradeMapper.toDto(grade);
    }

    @Override
    public GradeDTO updateGrade(Long id, GradeDTO dto) {
        Grade existing = gradeRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade not found"));

        // apply updates
        existing.setGradeName(dto.getGradeName());
//        existing.setStartYear(dto.getStartYear());
        existing.setEndYear(dto.getEndYear());
        existing.setGradeLevel(dto.getGradeLevel());

        Grade updated = gradeRepository.save(existing);
        return gradeMapper.toDto(updated);
    }

    @Override
    public void deleteGrade(Long id) {
        if (!gradeRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade not found");
        }
        gradeRepository.deleteById(id);
    }
}
