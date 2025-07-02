package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.SendMedication;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SendMedicationRepo extends JpaRepository<SendMedication, Long> {
    // Custom query methods can be defined here if needed
    @Query("SELECT sm FROM SendMedication sm " +
            "WHERE sm.pupil.pupilId = :pupilId AND sm.active = true " )
    List<SendMedication> findByPupilId(@Param("pupilId") String pupilId);
}
