package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.HealthCheckConsentForm;
import com.schoolhealth.schoolmedical.entity.enums.GradeLevel;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckConsentFlatData;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HealthCheckConsentRepo extends JpaRepository<HealthCheckConsentForm, Long> {
    @Query("SELECT new com.schoolhealth.schoolmedical.model.dto.response.HealthCheckConsentFlatData(" +
            "hccf.consentFormId, hccf.schoolYear,p.pupilId,p.lastName,p.firstName,p.birthDate,p.gender,p.avatar,pg.gradeName,d.diseaseId,d.name)" +
            "FROM HealthCheckConsentForm hccf " +
            "JOIN hccf.pupil p " +
            "JOIN p.pupilGrade pg " +
            "JOIN pg.grade g ON g.gradeLevel = :grade  " +
            "LEFT JOIN hccf.consentDiseases hcd " +
            "LEFT JOIN hcd.disease d " +
            "JOIN hccf.healthCheckCampaign hc " +
            "WHERE hccf.schoolYear = :schoolYear AND hc.statusHealthCampaign IN (com.schoolhealth.schoolmedical.entity.enums.StatusHealthCampaign.PUBLISHED,com.schoolhealth.schoolmedical.entity.enums.StatusHealthCampaign.IN_PROGRESS) " )
    List<HealthCheckConsentFlatData> findListPupilByGradeAndSchoolYear(@Param("grade") GradeLevel grade, @Param("schoolYear") int schoolYear);

    @Query("SELECT h FROM " +
            "HealthCheckConsentForm h " +
            "WHERE h.pupil.pupilId = :pupilId AND h.healthCheckCampaign.campaignId = :campaignId ")
    HealthCheckConsentForm findHealthCheckConsentByPupilIdAndCampaignId(@Param("pupilId") String pupilId, @Param("campaignId") Long campaignId);

}
