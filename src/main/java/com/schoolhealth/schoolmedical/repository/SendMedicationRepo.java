package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.SendMedication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SendMedicationRepo extends JpaRepository<SendMedication, Long> {
    // Custom query methods can be defined here if needed
    List<SendMedication> findByUserId(String userId);
}
