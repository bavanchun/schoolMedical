package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.HealthCheckHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HealthCheckHistoryRepo extends JpaRepository<HealthCheckHistory, Long> {
    @Query("SELECT h FROM HealthCheckHistory h " +
            "left JOIN FETCH h.healthCheckConsentForm hc " +
            "left JOIN FETCH hc.consentDiseases cd " +
            "left JOIN FETCH cd.disease " +
            "WHERE hc.pupil.pupilId = :pupilId " +
            "AND hc.schoolYear = :schoolYear " +
            "ORDER BY h.createdAt ASC")
    List<HealthCheckHistory> findHealthCheckHistoryByPupilIdAndSchoolYear(String pupilId, int schoolYear);
}

