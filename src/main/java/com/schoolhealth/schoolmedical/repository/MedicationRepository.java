package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.Medication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {

    // Find all active medication (no need for EntityGraph for basic queries)
    @Query("SELECT m FROM Medication m WHERE m.isActive = true ORDER BY m.name")
    List<Medication> findByIsActiveTrue();

    // Find active medication with pagination
    @Query("SELECT m FROM Medication m WHERE m.isActive = true ORDER BY m.name")
    Page<Medication> findByIsActiveTrue(Pageable pageable);

    // Find medication by ID and active status (no relationships needed for single item)
    @Query("SELECT m FROM Medication m WHERE m.medicationId = :medicationId AND m.isActive = true")
    Optional<Medication> findByMedicationIdAndIsActiveTrue(@Param("medicationId") Long medicationId);

    // Find medication by ID with medical events (for detailed view if needed)
    @EntityGraph(attributePaths = {"medicalEvents"})
    @Query("SELECT m FROM Medication m WHERE m.medicationId = :medicationId AND m.isActive = true")
    Optional<Medication> findByMedicationIdAndIsActiveTrueWithMedicalEvents(@Param("medicationId") Long medicationId);

    // Search medication by name (case insensitive)
    @Query("SELECT m FROM Medication m WHERE m.isActive = true AND LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY m.name")
    List<Medication> findByNameContainingIgnoreCaseAndIsActiveTrue(@Param("name") String name);

    // Check if medication name exists (for validation)
    @Query("SELECT COUNT(m) > 0 FROM Medication m WHERE m.name = :name AND m.isActive = true")
    boolean existsByNameAndIsActiveTrue(@Param("name") String name);

    // Check if medication name exists excluding current medication (for update validation)
    @Query("SELECT COUNT(m) > 0 FROM Medication m WHERE m.name = :name AND m.isActive = true AND m.medicationId != :medicationId")
    boolean existsByNameAndIsActiveTrueAndMedicationIdNot(@Param("name") String name, @Param("medicationId") Long medicationId);

    // Find medication by IDs (for medical event creation)
    @Query("SELECT m FROM Medication m WHERE m.medicationId IN :medicationIds AND m.isActive = true")
    List<Medication> findByMedicationIdInAndIsActiveTrue(@Param("medicationIds") List<Long> medicationIds);

}
