package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.model.dto.PupilDto;
import com.schoolhealth.schoolmedical.model.mapper.PupilMapper;
import com.schoolhealth.schoolmedical.repository.PupilRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PupilImpl implements PupilService{

    @Autowired
    private PupilRepo pupilRepo;

    @Autowired
    private PupilMapper pupilMapper;

    @Override
    public List<PupilDto> getAllPupils() {
        return pupilRepo.findAll().stream().map(pupilMapper::toDto).toList();
    }

    @Override
    public List<Pupil> getAll() {
        return pupilRepo.findAll();
    }

}
