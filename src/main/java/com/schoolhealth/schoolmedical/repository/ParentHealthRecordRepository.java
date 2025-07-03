package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.HealthConditionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository dedicated to operations on HealthConditionHistory for parent-managed records.
 */
@Repository
public interface ParentHealthRecordRepository extends JpaRepository<HealthConditionHistory, Long> {
    List<HealthConditionHistory> findByPupil_PupilId(String pupilId);
}
