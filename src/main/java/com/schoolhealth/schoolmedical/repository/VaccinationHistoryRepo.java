package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.Disease;
import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.entity.VaccinationHistory;
import com.schoolhealth.schoolmedical.entity.Vaccine;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VaccinationHistoryRepo extends CrudRepository<VaccinationHistory,Long>
{
    List<VaccinationHistory> findByPupilAndIsActiveTrueOrderByVaccinatedAtDesc(Pupil pupil);

    List<VaccinationHistory> findByPupilAndVaccineAndIsActiveTrue(Pupil pupil, Vaccine vaccine);

    List<VaccinationHistory> findByPupilAndDiseaseAndIsActiveTrue(Pupil pupil, Disease disease);

    @Query("SELECT vh FROM VaccinationHistory vh WHERE vh.pupil = :pupil AND vh.disease = :disease AND vh.isActive = true ORDER BY vh.doseNumber DESC")
    List<VaccinationHistory> findByPupilAndDiseaseOrderByDoseNumberDesc(@Param("pupil") Pupil pupil, @Param("disease") Disease disease);

    @Query("SELECT COUNT(vh) FROM VaccinationHistory vh WHERE vh.pupil = :pupil AND vh.disease = :disease AND vh.isActive = true")
    int countByPupilAndDiseaseAndIsActiveTrue(@Param("pupil") Pupil pupil, @Param("disease") Disease disease);
}
