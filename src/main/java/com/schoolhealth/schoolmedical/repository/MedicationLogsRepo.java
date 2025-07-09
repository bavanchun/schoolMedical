package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.MedicationLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MedicationLogsRepo extends JpaRepository<MedicationLogs, Long> {
    @Query("""
        SELECT ml FROM MedicationLogs ml
        JOIN ml.sendMedication sm
        WHERE sm.sendMedicationId = :medicationId and  ml.givenTime >= :startOfDay AND ml.givenTime < :startOfNextDay
""")
    List<MedicationLogs> findMedicationLogsBySendMedicationIdAndDate(Long medicationId, @Param("startOfDay") LocalDateTime startOfDay, @Param("startOfNextDay") LocalDateTime startOfNextDay);
}
