package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.HealthCheckHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HealthCheckHistoryRepo extends JpaRepository<HealthCheckHistory, Long> {
    @Query("SELECT h FROM HealthCheckHistory h " +
            "JOIN FETCH h.healthCheckConsentForm hc " +
            "JOIN FETCH hc.healthCheckDiseases hcd " +
            "JOIN FETCH hcd.disease " +
            "WHERE hc.pupil.pupilId = :pupilId AND hc.schoolYear = :schoolYear AND hcd.status = 'APPROVED' ")
    Optional<HealthCheckHistory> findHealthCheckHistoryByPupilIdAndSchoolYear(String pupilId, int schoolYear);
}
