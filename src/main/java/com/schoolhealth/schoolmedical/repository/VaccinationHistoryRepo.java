package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.Disease;
import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.entity.VaccinationHistory;
import com.schoolhealth.schoolmedical.entity.Vaccine;
import com.schoolhealth.schoolmedical.entity.enums.VaccinationSource;
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

    @Query("SELECT vh FROM VaccinationHistory vh WHERE vh.pupil = :pupil AND vh.disease = :disease AND vh.isActive = true ORDER BY vh.vaccinatedAt DESC")
    List<VaccinationHistory> findByPupilAndDiseaseOrderByVaccinatedAtDesc(@Param("pupil") Pupil pupil, @Param("disease") Disease disease);

    @Query("SELECT COUNT(vh) FROM VaccinationHistory vh WHERE vh.pupil = :pupil AND vh.disease = :disease AND vh.isActive = true")
    int countByPupilAndDiseaseAndIsActiveTrue(@Param("pupil") Pupil pupil, @Param("disease") Disease disease);

    @Query("SELECT vh FROM VaccinationHistory vh WHERE vh.source = :source AND vh.isActive = false ORDER BY vh.vaccinatedAt DESC")
    List<VaccinationHistory> findBySourceAndIsActiveFalseOrderByVaccinatedAtDesc(@Param("source") VaccinationSource source);

    @Query("SELECT vh FROM VaccinationHistory vh WHERE vh.pupil = :pupil AND vh.source = :source AND vh.isActive = false ORDER BY vh.vaccinatedAt DESC")
    List<VaccinationHistory> findByPupilAndSourceAndIsActiveFalseOrderByVaccinatedAtDesc(@Param("pupil") Pupil pupil, @Param("source") VaccinationSource source);

    @Query("""
        SELECT COUNT(DISTINCT c)
        FROM VaccinationCampagin c
        WHERE c.isActive = true AND YEAR(c.startDate) = :year
        AND c.status = com.schoolhealth.schoolmedical.entity.enums.VaccinationCampaignStatus.COMPLETED
    """)
    Long countCompletedVaccinationCampaignsByYear(@Param("year") int year);

    /**
     * Get vaccination statistics by vaccine type for a year
     * @param year School year
     * @return List of Object arrays containing [vaccine_name, count]
     */
    @Query("""
        SELECT v.name, COUNT(DISTINCT vh.pupil.pupilId)
        FROM VaccinationHistory vh
        JOIN vh.vaccine v
        JOIN vh.campaign c
        WHERE vh.isActive = true AND YEAR(c.startDate) = :year
        GROUP BY v.name
        ORDER BY COUNT(DISTINCT vh.pupil.pupilId) DESC
    """)
    List<Object[]> getVaccinationStatsByYear(@Param("year") int year);
}
