package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.Pupil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for verifying parent ownership of pupils without modifying existing PupilRepo.
 */
@Repository
public interface ParentPupilRepo extends JpaRepository<Pupil, String> {

    @Query("""
        SELECT p FROM Pupil p
        JOIN p.parents parent
        WHERE p.pupilId = :pupilId
          AND parent.userId = :parentId
          AND p.isActive = true
    """)
    Optional<Pupil> findActiveByIdAndParentId(@Param("pupilId") String pupilId,
                                              @Param("parentId") String parentId);

    @Query("""
        SELECT COUNT(p) > 0 FROM Pupil p
        JOIN p.parents parent
        WHERE p.pupilId = :pupilId
          AND parent.userId = :parentId
          AND p.isActive = true
    """)
    boolean existsActiveByIdAndParentId(@Param("pupilId") String pupilId,
                                        @Param("parentId") String parentId);
}