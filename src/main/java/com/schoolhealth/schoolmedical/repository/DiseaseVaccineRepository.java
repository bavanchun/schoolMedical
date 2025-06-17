package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.DiseaseVaccine;
import com.schoolhealth.schoolmedical.entity.DiseaseVaccineId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiseaseVaccineRepository extends JpaRepository<DiseaseVaccine, DiseaseVaccineId> {
    // Phương thức để lấy tất cả DiseaseVaccine có isActive = true
    List<DiseaseVaccine> findByIsActiveTrue();
}
