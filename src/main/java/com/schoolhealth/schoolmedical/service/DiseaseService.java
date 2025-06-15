package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.entity.Disease;

import java.util.List;
import java.util.Optional;

public interface DiseaseService {
    Optional<List<Disease>> getAllDiseases();
}
