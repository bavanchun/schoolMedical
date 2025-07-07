package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.entity.SendMedication;
import com.schoolhealth.schoolmedical.entity.enums.StatusSendMedication;
import com.schoolhealth.schoolmedical.model.dto.response.QuantityPupilByGradeRes;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SendMedicationRepo extends JpaRepository<SendMedication, Long> {
    // Custom query methods can be defined here if needed
    @Query("SELECT sm FROM SendMedication sm " +
            "WHERE sm.pupil.pupilId = :pupilId AND sm.active = true " )
    List<SendMedication> findByPupilId(@Param("pupilId") String pupilId);

    @Query("SELECT sm FROM SendMedication sm WHERE sm.status = :status AND sm.active = true")
    List<SendMedication> findAllByStatus(@Param("status") StatusSendMedication status);
    

    @Query(value = """
        select pg.grade_id as grade, count(distinct p.pupil_id) as quantity
        from pupil p
                 join pupil_grade pg on p.pupil_id = pg.pupil_id
                 join send_medication sm on sm.pupil_id = p.pupil_id
                 join medication_item mi on sm.send_medication_id = mi.send_medication_id
        where sm.status = 'APPROVED' and mi.medication_schedule = 'After breakfast: 9h00-9h30' and sm.is_active = true and pg.start_year = Year(CURRENT_DATE)
        group by pg.grade_id
        """, nativeQuery = true)
    List<QuantityPupilByGradeRes> getQuantityPupilByGradeAndAfterBreakfast();

    @Query(value = """
        select pg.grade_id as grade, count(distinct p.pupil_id) as quantity
        from pupil p
                 join pupil_grade pg on p.pupil_id = pg.pupil_id
                 join send_medication sm on sm.pupil_id = p.pupil_id
                 join medication_item mi on sm.send_medication_id = mi.send_medication_id
        where sm.status = 'APPROVED' and mi.medication_schedule = 'After lunch: 11h30-12h00'  and sm.is_active = true and pg.start_year = Year(CURRENT_DATE)
        group by pg.grade_id
        """, nativeQuery = true)
    List<QuantityPupilByGradeRes> getQuantityPupilByGradeAndAfterLunch();

    @Query(value = """
        select pg.grade_id as grade, count(distinct p.pupil_id) as quantity
        from pupil p
                 join pupil_grade pg on p.pupil_id = pg.pupil_id
                 join send_medication sm on sm.pupil_id = p.pupil_id
                 join medication_item mi on sm.send_medication_id = mi.send_medication_id
        where sm.status = 'APPROVED' and mi.medication_schedule = 'Before lunch: 10h30-11h00'  and sm.is_active = true and pg.start_year = Year(CURRENT_DATE)
        group by pg.grade_id
        """, nativeQuery = true)
    List<QuantityPupilByGradeRes> getQuantityPupilByGradeAndBeforeLunch();

    @Query(value = """
        select distinct p.* from pupil p
        join pupil_grade pg on p.pupil_id = pg.pupil_id
        join send_medication sm on sm.pupil_id = p.pupil_id
        join medication_item mi on sm.send_medication_id = mi.send_medication_id
        where sm.status = :status and mi.medication_schedule = :session and pg.grade_id = :gradeId and sm.is_active = true and pg.start_year = Year(CURRENT_DATE)
    """, nativeQuery = true)
    List<Pupil> findAllPupilBySessionAndGrade(@Param("session") String session, @Param("gradeId") Long gradeId, @Param("status") StatusSendMedication status);

    @Query(value = """
    select sm.*
    from send_medication sm
    where sm.status = :status and sm.pupil_id = :pupilId and sm.is_active = true
""", nativeQuery = true)
    List<SendMedication> findByPupilIdAndStatus(@Param("pupilId") String pupilId, @Param("status") StatusSendMedication status);

    @Query("""
    SELECT sm FROM SendMedication sm
    JOIN FETCH sm.medicationLogs ml
    JOIN FETCH sm.pupil p 
    WHERE ml.logId = :medicationLogId 
""")
    SendMedication findByMedicationLogs(Long medicationLogId);

    List<SendMedication> findAllByActiveTrue();

    @Query("""
        SELECT sm FROM SendMedication sm
        WHERE sm.status = com.schoolhealth.schoolmedical.entity.enums.StatusSendMedication.APPROVED 
        AND sm.active = true
        AND :date BETWEEN sm.startDate AND sm.endDate
""")
    List<SendMedication> findAllByInProgress(@Param("date") LocalDate date);
}
