package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.model.dto.PupilDto;

import java.util.List;

public interface PupilService {
    List<PupilDto> getAllPupils();
    List<Pupil> getAll();
}
