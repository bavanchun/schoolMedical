package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.SendMedication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SendMedicationRepo extends JpaRepository<SendMedication, Long> {
    // Custom query methods can be defined here if needed
}
