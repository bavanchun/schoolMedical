package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.MedicationLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationLogsRepo extends JpaRepository<MedicationLogs, Long> {
}
