package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    // Find all active equipment
    List<Equipment> findByIsActiveTrue();

    // Find active equipment with pagination
    Page<Equipment> findByIsActiveTrue(Pageable pageable);

    // Find equipment by ID and active status
    Optional<Equipment> findByEquipmentIdAndIsActiveTrue(Long equipmentId);

    // Search equipment by name (case insensitive)
    @Query("SELECT e FROM Equipment e WHERE e.isActive = true AND LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Equipment> findByNameContainingIgnoreCaseAndIsActiveTrue(@Param("name") String name);

    // Check if equipment name exists (for validation)
    boolean existsByNameAndIsActiveTrue(String name);

    // Check if equipment name exists excluding current equipment (for update validation)
    @Query("SELECT COUNT(e) > 0 FROM Equipment e WHERE e.name = :name AND e.isActive = true AND e.equipmentId != :equipmentId")
    boolean existsByNameAndIsActiveTrueAndEquipmentIdNot(@Param("name") String name, @Param("equipmentId") Long equipmentId);
}
