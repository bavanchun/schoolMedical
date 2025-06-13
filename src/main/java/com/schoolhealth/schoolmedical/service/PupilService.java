package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.model.dto.PupilDto;

import java.util.List;

public interface PupilService {
    PupilDto createPupil(PupilDto dto);
    List<PupilDto> getAllPupils();
    PupilDto getPupilById(String id);
    PupilDto updatePupil(String id, PupilDto dto);
    void deletePupil(String id);
}
