package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.HealthCheckCampaign;
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
import java.util.Set;

@Repository
public interface HealthCheckConsentRepo extends JpaRepository<HealthCheckConsentForm, Long> {
    @Query("SELECT new com.schoolhealth.schoolmedical.model.dto.response.HealthCheckConsentFlatData(" +
            "hccf.consentFormId, hccf.schoolYear,p.pupilId,p.lastName,p.firstName,p.birthDate,p.gender,p.avatar,pg.gradeName,d.diseaseId,d.name,d.description,hcd.note,hccf.active)" +
            "FROM HealthCheckConsentForm hccf " +
            "JOIN hccf.pupil p " +
            "JOIN p.pupilGrade pg ON pg.startYear = ( " +
            " SELECT MAX(sub_pg.startYear) " +
            "  FROM PupilGrade sub_pg " +
            "  WHERE sub_pg.pupil.pupilId = p.pupilId )" +
            "JOIN pg.grade g  " +
            "LEFT JOIN hccf.consentDiseases hcd " +
            "LEFT JOIN hcd.disease d " +
            "WHERE  g.gradeLevel = :grade  " +
            "AND hccf.healthCheckCampaign.campaignId = (" +
            "       SELECT hc.campaignId " +
            "       From HealthCheckCampaign hc " +
            "       WHERE hc.statusHealthCampaign = com.schoolhealth.schoolmedical.entity.enums.StatusHealthCampaign.IN_PROGRESS" +
            "       Order by hc.createdAt DESC " +
            "       LIMIT 1" +
            ")" )
    List<HealthCheckConsentFlatData> findListPupilByGradeAndSchoolYear(@Param("grade") GradeLevel grade);

    @Query("SELECT h FROM " +
            "HealthCheckConsentForm h " +
            "WHERE h.pupil.pupilId = :pupilId AND h.healthCheckCampaign.campaignId = :campaignId ")
    HealthCheckConsentForm findHealthCheckConsentByPupilIdAndCampaignId(@Param("pupilId") String pupilId, @Param("campaignId") Long campaignId);

    @Query("SELECT new com.schoolhealth.schoolmedical.model.dto.response.HealthCheckConsentFlatData(" +
            "hccf.consentFormId, hccf.schoolYear,p.pupilId,p.lastName,p.firstName,p.birthDate,p.gender,p.avatar,pg.gradeName,d.diseaseId,d.name,d.description,hcd.note,hccf.active)" +
            "FROM HealthCheckConsentForm hccf " +
            "JOIN hccf.pupil p " +
            "JOIN p.pupilGrade pg ON pg.startYear = (" +
            "    SELECT MAX(sub_pg.startYear)" +
            "    FROM PupilGrade sub_pg" +
            "    WHERE sub_pg.pupil.pupilId = p.pupilId" +
            ") " +
            "JOIN pg.grade g " +
            "LEFT JOIN hccf.consentDiseases hcd " +
            "LEFT JOIN hcd.disease d " +
            "JOIN hccf.healthCheckCampaign hc " +
            "WHERE hccf.healthCheckCampaign.campaignId = :campaignId " )
    List<HealthCheckConsentFlatData> findHealthCheckConsentFormByCampaignId(@Param("campaignId") Long campaignId);

    @Query("SELECT c FROM HealthCheckConsentForm c " +
            "join fetch c.healthCheckHistory " +
            "where c.consentFormId in :consentFormId")
    List<HealthCheckConsentForm> findAllById(@Param("consentFormId") List<Long> consentFormId);

    @Query("SELECT hc FROM HealthCheckConsentForm hc " +
            "JOIN FETCH hc.healthCheckHistory hch " +
            "LEFT JOIN FETCH hc.consentDiseases cd " +
            "WHERE hc.consentFormId = :consentFormId " )
    HealthCheckConsentForm findByConsentFormId(@Param("consentFormId") Long consentFormId);

    @Query("""
    SELECT hccf FROM HealthCheckConsentForm hccf
    LEFT JOIN FETCH hccf.healthCheckHistory hch
    LEFT JOIN FETCH hccf.consentDiseases cd
    LEFT JOIN FETCH cd.disease d
    WHERE hccf.pupil.pupilId = :pupilId
""")
    List<HealthCheckConsentForm> findAllByPupilID(@Param("pupilId") String pupilId);

    @Query("""
    SELECT hccf FROM HealthCheckConsentForm hccf
    LEFT JOIN FETCH hccf.healthCheckHistory hch
    LEFT JOIN FETCH hccf.consentDiseases cd
    LEFT JOIN FETCH cd.disease d
    join fetch hccf.healthCheckCampaign 
    WHERE hccf.pupil.pupilId = :pupilId
""")
    List<HealthCheckConsentForm> findAllByPupilPupilIdIn(@Param("pupilId") String pupilId );

    @EntityGraph(attributePaths = {"pupil","pupil.parents"})
    List<HealthCheckConsentForm> findAllByActiveFalseAndHealthCheckCampaign(HealthCheckCampaign healthCheckCampaign);
}
