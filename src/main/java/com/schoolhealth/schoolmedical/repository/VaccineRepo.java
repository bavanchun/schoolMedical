package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VaccineRepo extends JpaRepository<Vaccine,Integer> {
    // Phương thức để lấy tất cả vaccine có isActive = true
    List<Vaccine> findByIsActiveTrue();
}
