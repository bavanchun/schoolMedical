package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiseaseRepo extends JpaRepository<Disease, Integer> {
}
