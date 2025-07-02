package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.Vaccine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VaccineRepository extends JpaRepository<Vaccine, Long> {
    @Query(value = "SELECT * FROM vaccine v WHERE v.is_active = ?1",
            countQuery = "SELECT COUNT(*) FROM vaccine v WHERE v.is_active = ?1",
            nativeQuery = true)
    Page<Vaccine> findByIsActiveTrue(boolean isActive, Pageable pageable);

    Optional<Vaccine> findByNameIgnoreCase(String name);
}