package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.Disease;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiseaseRepo extends JpaRepository<Disease, Long> {
    @Query(value = "SELECT * FROM disease d where d.is_active =?1", countQuery = "SELECT COUNT(*) FROM disease d where d.is_active =?1", nativeQuery = true)
    Page<Disease> findAllByisActiveTrue(boolean isActive, Pageable pageable);

    List<Disease> findAllByisActiveTrue();

    Optional<Disease> findByNameIgnoreCase(String name);

    List<Disease> findAllByisActiveTrueAndIsInjectedVaccinationFalse();
    Page<Disease> findAllByisActiveTrueAndIsInjectedVaccinationFalse(Pageable pageable);

    List<Disease> findAllByisActiveTrueAndIsInjectedVaccinationTrue();

    List<Disease> findAllByDiseaseIdInAndIsActiveTrue(List<Long> diseaseIds);
}
