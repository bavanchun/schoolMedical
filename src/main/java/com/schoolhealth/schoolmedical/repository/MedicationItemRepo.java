package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.MedicationItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationItemRepo extends JpaRepository<MedicationItem, Long> {

}
