package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.HealthConditionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository dedicated to operations on HealthConditionHistory for parent-managed records.
 */
@Repository
public interface ParentHealthRecordRepository extends JpaRepository<HealthConditionHistory, Long> {
    List<HealthConditionHistory> findByPupil_PupilId(String pupilId);

    @Query("""
        SELECT h FROM HealthConditionHistory h
        JOIN h.pupil p
        JOIN p.parents parent
        WHERE p.pupilId = :pupilId
          AND parent.userId = :parentId
          AND p.isActive = true
    """)
    List<HealthConditionHistory> findByPupilIdAndParentId(
            @Param("pupilId") String pupilId,
            @Param("parentId") String parentId);

    @Query("""
        SELECT h FROM HealthConditionHistory h
        JOIN h.pupil p
        JOIN p.parents parent
        WHERE h.conditionId = :id
          AND parent.userId = :parentId
          AND p.isActive = true
    """)
    Optional<HealthConditionHistory> findByIdAndParentId(
            @Param("id") Long id,
            @Param("parentId") String parentId);
}