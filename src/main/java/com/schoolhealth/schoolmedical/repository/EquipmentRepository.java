package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.Equipment;
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
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    // Find all active equipment (no need for EntityGraph for basic queries)
    @Query("SELECT e FROM Equipment e WHERE e.isActive = true ORDER BY e.name")
    List<Equipment> findByIsActiveTrue();

    // Find active equipment with pagination
    @Query("SELECT e FROM Equipment e WHERE e.isActive = true ORDER BY e.name")
    Page<Equipment> findByIsActiveTrue(Pageable pageable);

    // Find equipment by ID and active status (no relationships needed for single item)
    @Query("SELECT e FROM Equipment e WHERE e.equipmentId = :equipmentId AND e.isActive = true")
    Optional<Equipment> findByEquipmentIdAndIsActiveTrue(@Param("equipmentId") Long equipmentId);

    // Find equipment by ID with medical events (for detailed view if needed)
    @EntityGraph(attributePaths = {"medicalEvents"})
    @Query("SELECT e FROM Equipment e WHERE e.equipmentId = :equipmentId AND e.isActive = true")
    Optional<Equipment> findByEquipmentIdAndIsActiveTrueWithMedicalEvents(@Param("equipmentId") Long equipmentId);

    // Search equipment by name (case insensitive)
    @Query("SELECT e FROM Equipment e WHERE e.isActive = true AND LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY e.name")
    List<Equipment> findByNameContainingIgnoreCaseAndIsActiveTrue(@Param("name") String name);

    // Check if equipment name exists (for validation)
    @Query("SELECT COUNT(e) > 0 FROM Equipment e WHERE e.name = :name AND e.isActive = true")
    boolean existsByNameAndIsActiveTrue(@Param("name") String name);

    // Check if equipment name exists excluding current equipment (for update validation)
    @Query("SELECT COUNT(e) > 0 FROM Equipment e WHERE e.name = :name AND e.isActive = true AND e.equipmentId != :equipmentId")
    boolean existsByNameAndIsActiveTrueAndEquipmentIdNot(@Param("name") String name, @Param("equipmentId") Long equipmentId);

    // Find equipment by IDs (for medical event creation)
    @Query("SELECT e FROM Equipment e WHERE e.equipmentId IN :equipmentIds AND e.isActive = true")
    List<Equipment> findByEquipmentIdInAndIsActiveTrue(@Param("equipmentIds") List<Long> equipmentIds);
}
