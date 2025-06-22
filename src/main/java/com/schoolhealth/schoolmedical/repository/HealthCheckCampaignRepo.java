package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.HealthCheckCampaign;
import com.schoolhealth.schoolmedical.entity.enums.GradeLevel;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckCampaignFlatData;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckPupilListRes;
import com.schoolhealth.schoolmedical.model.dto.response.LatestHealthCheckCampaignRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HealthCheckCampaignRepo extends JpaRepository<HealthCheckCampaign, Long> {
    @Query("SELECT new com.schoolhealth.schoolmedical.model.dto.response.HealthCheckCampaignFlatData(" +
            "hcc.campaignId, hcc.address, hcc.title, hcc.description, hcc.deadlineDate, hcc.startExaminationDate, hcc.endExaminationDate, hcc.createdAt, hcc.statusHealthCampaign, " +
            "hccs.consentFormId, hccs.schoolYear, " +
            "pp.pupilId, pp.lastName, pp.firstName, pp.birthDate, pp.gender, pp.avatar,g.gradeId, g.gradeLevel,g.gradeName, " +
            "hcd.healthCheckDiseaseId,d.name) " +
            "FROM HealthCheckCampaign hcc " +
            "LEFT JOIN hcc.healthCheckConsentForms hccs " +
            "LEFT JOIN hccs.pupil pp " +
            "LEFT JOIN pp.pupilGrade pg " +
            "LEFT JOIN pg.grade g " +
            "LEFT JOIN hccs.healthCheckDiseases hcd " +
            "LEFT JOIN hcd.disease d " +
            " where hcc.active = true AND hcd.status = com.schoolhealth.schoolmedical.entity.enums.HealthCheckDiseaseStatus.APPROVED AND hcc.campaignId = :campaignId")
    List<HealthCheckCampaignFlatData> findHealthCheckCampaignDetails(@Param("campaignId") Long campaignId);

    @Query("SELECT new com.schoolhealth.schoolmedical.model.dto.response.HealthCheckPupilListRes(" +
            "hcc.campaignId, hcc.address, hcc.title, hcc.deadlineDate, hcc.startExaminationDate, hcc.endExaminationDate, " +
            "hccs.consentFormId, hccs.schoolYear, " +
            "pp.pupilId, pp.lastName, pp.firstName, pp.birthDate, pp.gender, g.gradeLevel,g.gradeName, " +
            "hcd.healthCheckDiseaseId,d.name) " +
            "FROM HealthCheckConsentForm hccs " +
            "LEFT JOIN hccs.healthCheckCampaign hcc " +
            "LEFT JOIN hccs.pupil pp " +
            "LEFT JOIN pp.grade g " +
            "LEFT JOIN hccs.healthCheckDiseases hcd " +
            "LEFT JOIN hcd.disease d " +
            "where hcc.active = true AND hcd.status = com.schoolhealth.schoolmedical.entity.enums.HealthCheckDiseaseStatus.APPROVED " +
            "AND g.gradeLevel= :gradeLevel AND hccs.schoolYear= :schoolYear" )
    List<HealthCheckPupilListRes> findHealthCheckCampaignByGradeLevelAndSchoolYear(@Param("gradeLevel") GradeLevel gradeLevel, @Param("schoolYear") int schoolYear);
    HealthCheckCampaign findTopByActiveTrueOrderByCreatedAtDesc();
}
