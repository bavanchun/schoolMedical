package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.repository.DiseaseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiseaseImpl implements DiseaseService{

    @Autowired
    private DiseaseRepo diseaseRepo;

    @Override
    public List<DiseaseRepo> getAllDiseases() {
        return diseaseRepo.findAll();
    }
}
