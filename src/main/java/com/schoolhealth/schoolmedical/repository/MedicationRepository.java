package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.Medication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {

    // Find all active medication
    List<Medication> findByIsActiveTrue();

    // Find active medication with pagination
    Page<Medication> findByIsActiveTrue(Pageable pageable);

    // Find medication by ID and active status
    Optional<Medication> findByMedicationIdAndIsActiveTrue(Long medicationId);

    // Search medication by name (case insensitive)
    @Query("SELECT m FROM Medication m WHERE m.isActive = true AND LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Medication> findByNameContainingIgnoreCaseAndIsActiveTrue(@Param("name") String name);

    // Check if medication name exists (for validation)
    boolean existsByNameAndIsActiveTrue(String name);

    // Check if medication name exists excluding current medication (for update validation)
    @Query("SELECT COUNT(m) > 0 FROM Medication m WHERE m.name = :name AND m.isActive = true AND m.medicationId != :medicationId")
    boolean existsByNameAndIsActiveTrueAndMedicationIdNot(@Param("name") String name, @Param("medicationId") Long medicationId);
}
