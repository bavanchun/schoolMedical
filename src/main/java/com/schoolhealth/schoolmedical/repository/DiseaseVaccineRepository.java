package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.DiseaseVaccine;
import com.schoolhealth.schoolmedical.entity.DiseaseVaccineId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiseaseVaccineRepository extends JpaRepository<DiseaseVaccine, DiseaseVaccineId> {
}
