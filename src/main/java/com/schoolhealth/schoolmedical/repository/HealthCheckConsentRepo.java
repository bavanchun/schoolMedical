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

@Repository
public interface HealthCheckConsentRepo extends JpaRepository<HealthCheckConsentForm, Long> {
    @Query("SELECT new com.schoolhealth.schoolmedical.model.dto.response.HealthCheckConsentFlatData(" +
            "hccf.consentFormId, hccf.schoolYear,p.pupilId,p.lastName,p.firstName,p.birthDate,p.gender,p.avatar,pg.gradeName,hcd.healthCheckDiseaseId,d.name)" +
            "FROM HealthCheckConsentForm hccf " +
            "JOIN hccf.pupil p " +
            "JOIN p.pupilGrade pg " +
            "JOIN pg.grade g ON g.gradeLevel = :grade  " +
            "LEFT JOIN hccf.healthCheckDiseases hcd ON hcd.status = com.schoolhealth.schoolmedical.entity.enums.HealthCheckDiseaseStatus.APPROVED " +
            "LEFT JOIN hcd.disease d " +
            "JOIN hccf.healthCheckCampaign hc " +
            "WHERE hccf.schoolYear = :schoolYear AND hc.statusHealthCampaign IN (com.schoolhealth.schoolmedical.entity.enums.StatusHealthCampaign.PUBLISHED,com.schoolhealth.schoolmedical.entity.enums.StatusHealthCampaign.IN_PROGRESS) " )
    List<HealthCheckConsentFlatData> findListPupilByGradeAndSchoolYear(@Param("grade") GradeLevel grade, @Param("schoolYear") int schoolYear);

}
