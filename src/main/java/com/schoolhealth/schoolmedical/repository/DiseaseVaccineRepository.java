package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.DiseaseVaccine;
import com.schoolhealth.schoolmedical.entity.DiseaseVaccineId;
import com.schoolhealth.schoolmedical.entity.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiseaseVaccineRepository extends JpaRepository<DiseaseVaccine, DiseaseVaccineId> {
    // Phương thức để lấy tất cả DiseaseVaccine có isActive = true
    List<DiseaseVaccine> findByIsActiveTrue();

    // Tìm vaccine theo diseaseId
    @Query("SELECT dv.vaccine FROM DiseaseVaccine dv WHERE dv.disease.diseaseId = :diseaseId AND dv.isActive = true")
    List<Vaccine> findVaccinesByDiseaseId(@Param("diseaseId") int diseaseId);
}
