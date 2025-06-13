package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.model.dto.PupilDto;

import java.util.List;
import java.util.Optional;

public interface PupilService {
    List<PupilDto> getAllPupils();
    Optional<List<Pupil>> getAll();
}
