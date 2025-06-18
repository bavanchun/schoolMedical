package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.Disease;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiseaseRepo extends JpaRepository<Disease, Integer> {
    @Query(value = "SELECT * FROM disease", countQuery = "SELECT COUNT(*) FROM disease", nativeQuery = true)
    Page<Disease> findAllByisActiveTrue(Pageable pageable);

    List<Disease> findAllByisActiveTrue();

    Optional<Disease> findByNameIgnoreCase(String name);
}
