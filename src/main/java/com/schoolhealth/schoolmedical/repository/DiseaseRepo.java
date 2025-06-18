package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiseaseRepo extends JpaRepository<Disease, Integer> {
    List<Disease> findAllByisActiveTrue();
    Optional<Disease> findByNameIgnoreCase(String name);
}

