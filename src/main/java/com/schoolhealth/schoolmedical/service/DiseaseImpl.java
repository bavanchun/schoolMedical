package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.entity.Disease;
import com.schoolhealth.schoolmedical.repository.DiseaseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiseaseImpl implements DiseaseService{

    @Autowired
    private DiseaseRepo diseaseRepo;


    @Override
    public Optional<List<Disease>> getAllDiseases() {
        return Optional.ofNullable(diseaseRepo.findAll());
    }
}
