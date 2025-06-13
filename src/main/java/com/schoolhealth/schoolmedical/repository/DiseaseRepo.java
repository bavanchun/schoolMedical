package com.schoolhealth.schoolmedical.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiseaseRepo extends JpaRepository<DiseaseRepo, Integer> {
}
