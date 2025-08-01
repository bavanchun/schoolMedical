package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.MedicalEvent;
import com.schoolhealth.schoolmedical.entity.enums.GradeLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalEventRepository extends JpaRepository<MedicalEvent, Long> {


    // Find medical event by ID with all relationships (prevent N+1) - FIXED
    @EntityGraph(attributePaths = {
            "pupil",
            "schoolNurse"
    })
    @Query("SELECT me FROM MedicalEvent me WHERE me.medicalEventId = :eventId AND me.isActive = true")
    Optional<MedicalEvent> findByIdWithAllRelationships(@Param("eventId") Long eventId);

    // Find all medical events with basic relationships (for list views)
    @EntityGraph(attributePaths = {
            "pupil",
            "pupil.pupilGrade",
            "pupil.pupilGrade.grade",
            "schoolNurse"
    })
    @Query("SELECT me FROM MedicalEvent me WHERE me.isActive = true ORDER BY me.dateTime DESC")
    Page<MedicalEvent> findAllWithBasicRelationships(Pageable pageable);

    // Find medical events by pupil ID - FIXED MultipleBagFetchException
    @Query("SELECT me FROM MedicalEvent me WHERE me.pupil.pupilId = :pupilId AND me.isActive = true ORDER BY me.dateTime DESC")
    List<MedicalEvent> findByPupilIdBasic(@Param("pupilId") String pupilId);

    @EntityGraph(attributePaths = {
            "pupil",
            "schoolNurse"
    })
    @Query("SELECT me FROM MedicalEvent me WHERE me.pupil.pupilId = :pupilId AND me.isActive = true ORDER BY me.dateTime DESC")
    List<MedicalEvent> findByPupilIdWithRelationships(@Param("pupilId") String pupilId);

    // Find medical events by pupil ID with pagination - FIXED
    @EntityGraph(attributePaths = {
            "pupil",
            "schoolNurse"
    })
    @Query("SELECT me FROM MedicalEvent me WHERE me.pupil.pupilId = :pupilId AND me.isActive = true ORDER BY me.dateTime DESC")
    Page<MedicalEvent> findByPupilIdWithRelationships(@Param("pupilId") String pupilId, Pageable pageable);

    // Find medical events by school nurse ID - FIXED
    @EntityGraph(attributePaths = {
            "pupil",
            "pupil.pupilGrade",
            "pupil.pupilGrade.grade"
    })
    @Query("SELECT me FROM MedicalEvent me WHERE me.schoolNurse.userId = :schoolNurseId AND me.isActive = true ORDER BY me.dateTime DESC")
    Page<MedicalEvent> findBySchoolNurseIdWithRelationships(@Param("schoolNurseId") String schoolNurseId, Pageable pageable);

    // Find medical events by grade level - FIXED
    @EntityGraph(attributePaths = {
            "pupil",
            "pupil.pupilGrade",
            "pupil.pupilGrade.grade",
            "schoolNurse"
    })
    @Query("""
        SELECT me FROM MedicalEvent me
        JOIN me.pupil p
        JOIN p.pupilGrade pg
        JOIN pg.grade g
        WHERE g.gradeLevel = :gradeLevel
        AND me.isActive = true
        AND pg.startYear = (
            SELECT MAX(sub_pg.startYear)
            FROM PupilGrade sub_pg
            WHERE sub_pg.pupil.pupilId = p.pupilId
        )
        ORDER BY me.dateTime DESC
    """)
    Page<MedicalEvent> findByGradeLevelWithRelationships(@Param("gradeLevel") GradeLevel gradeLevel, Pageable pageable);

    // Find medical events by parent ID - FIXED
    @EntityGraph(attributePaths = {
            "pupil",
            "pupil.pupilGrade",
            "pupil.pupilGrade.grade",
            "schoolNurse"
    })
    @Query("""
        SELECT me FROM MedicalEvent me
        JOIN me.pupil p
        JOIN p.parents parent
        WHERE parent.userId = :parentId
        AND me.isActive = true
        ORDER BY me.dateTime DESC
    """)
    List<MedicalEvent> findByParentIdWithRelationships(@Param("parentId") String parentId);

    // Find medical events by parent ID and year - FIXED
    @EntityGraph(attributePaths = {
            "pupil",
            "pupil.pupilGrade",
            "pupil.pupilGrade.grade",
            "schoolNurse"
    })
    @Query("""
        SELECT me FROM MedicalEvent me
        JOIN me.pupil p
        JOIN p.parents parent
        WHERE parent.userId = :parentId
        AND YEAR(me.dateTime) = :year
        AND me.isActive = true
        ORDER BY me.dateTime DESC
    """)
    List<MedicalEvent> findByParentIdAndYearWithRelationships(@Param("parentId") String parentId, @Param("year") int year);

    // Find medical events by parent ID and pupil ID and year (for specific child)
    @EntityGraph(attributePaths = {
            "pupil",
            "pupil.pupilGrade",
            "pupil.pupilGrade.grade",
            "schoolNurse",
            "equipmentUsed",
            "medicationUsed"
    })
    @Query("""
        SELECT me FROM MedicalEvent me
        JOIN me.pupil p
        JOIN p.parents parent
        WHERE parent.userId = :parentId
        AND p.pupilId = :pupilId
        AND YEAR(me.dateTime) = :year
        AND me.isActive = true
        ORDER BY me.dateTime DESC
    """)
    List<MedicalEvent> findByParentIdAndPupilIdAndYearWithRelationships(
            @Param("parentId") String parentId,
            @Param("pupilId") String pupilId,
            @Param("year") int year
    );

    // Find medical events by date range
    @EntityGraph(attributePaths = {
            "pupil",
            "pupil.pupilGrade",
            "pupil.pupilGrade.grade",
            "schoolNurse"
    })
    @Query("SELECT me FROM MedicalEvent me WHERE me.dateTime BETWEEN :startDate AND :endDate AND me.isActive = true ORDER BY me.dateTime DESC")
    Page<MedicalEvent> findByDateRangeWithRelationships(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    // Count medical events by pupil (for statistics)
    @Query("SELECT COUNT(me) FROM MedicalEvent me WHERE me.pupil.pupilId = :pupilId AND me.isActive = true")
    long countByPupilId(@Param("pupilId") String pupilId);

    // Count medical events by grade level (for statistics)
    @Query("""
        SELECT COUNT(me) FROM MedicalEvent me
        JOIN me.pupil p
        JOIN p.pupilGrade pg
        JOIN pg.grade g
        WHERE g.gradeLevel = :gradeLevel
        AND me.isActive = true
        AND pg.startYear = (
            SELECT MAX(sub_pg.startYear)
            FROM PupilGrade sub_pg
            WHERE sub_pg.pupil.pupilId = p.pupilId
        )
    """)
    long countByGradeLevel(@Param("gradeLevel") GradeLevel gradeLevel);

    // Find all active medical events with relationships (for getting all without pagination)
    @EntityGraph(attributePaths = {
            "pupil",
            "schoolNurse"
    })
    @Query("SELECT me FROM MedicalEvent me WHERE me.isActive = true ORDER BY me.dateTime DESC")
    List<MedicalEvent> findAllActiveWithRelationships();

    // Search medical events by pupil name or pupil ID
    @EntityGraph(attributePaths = {
            "pupil",
            "schoolNurse"
    })
    @Query("""
        SELECT me FROM MedicalEvent me
        JOIN me.pupil p
        WHERE me.isActive = true
        AND (
            LOWER(CONCAT(p.lastName, ' ', p.firstName)) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(p.pupilId) LIKE LOWER(CONCAT('%', :search, '%'))
        )
        ORDER BY me.dateTime DESC
    """)
    List<MedicalEvent> findBySearchCriteria(@Param("search") String search);

    @Query("""
        SELECT COUNT(me) FROM MedicalEvent me
        WHERE me.isActive = true AND YEAR(me.dateTime) = :year
    """)
    Long countMedicalEventsByYear(@Param("year") int year);


    @Query("""
        SELECT
            CASE MONTH(me.dateTime)
                WHEN 1 THEN 'Jan'
                WHEN 2 THEN 'Feb'
                WHEN 3 THEN 'Mar'
                WHEN 4 THEN 'Apr'
                WHEN 5 THEN 'May'
                WHEN 6 THEN 'Jun'
                WHEN 7 THEN 'Jul'
                WHEN 8 THEN 'Aug'
                WHEN 9 THEN 'Sep'
                WHEN 10 THEN 'Oct'
                WHEN 11 THEN 'Nov'
                WHEN 12 THEN 'Dec'
            END,
            COUNT(me)
        FROM MedicalEvent me
        WHERE me.isActive = true AND YEAR(me.dateTime) = :year
        GROUP BY MONTH(me.dateTime),
            CASE MONTH(me.dateTime)
                WHEN 1 THEN 'Jan'
                WHEN 2 THEN 'Feb'
                WHEN 3 THEN 'Mar'
                WHEN 4 THEN 'Apr'
                WHEN 5 THEN 'May'
                WHEN 6 THEN 'Jun'
                WHEN 7 THEN 'Jul'
                WHEN 8 THEN 'Aug'
                WHEN 9 THEN 'Sep'
                WHEN 10 THEN 'Oct'
                WHEN 11 THEN 'Nov'
                WHEN 12 THEN 'Dec'
            END
        ORDER BY MONTH(me.dateTime)
    """)
    List<Object[]> getEventStatsByMonthAndYear(@Param("year") int year);
}
