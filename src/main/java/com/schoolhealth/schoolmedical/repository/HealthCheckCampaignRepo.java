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
            "pp.pupilId, pp.lastName, pp.firstName, pp.birthDate, pp.gender, pp.avatar,pg.grade.gradeId, g.gradeLevel,pg.gradeName, " +
            "hcd.healthCheckDiseaseId,d.name) " +
            "FROM HealthCheckCampaign hcc " +
            "LEFT JOIN hcc.healthCheckConsentForms hccs " +
            "LEFT JOIN hccs.pupil pp " +
            "LEFT JOIN pp.pupilGrade pg " +
            "LEFT JOIN pg.grade g " +
            "LEFT JOIN hccs.healthCheckDiseases hcd ON hcd.status = com.schoolhealth.schoolmedical.entity.enums.HealthCheckDiseaseStatus.APPROVED " +
            "LEFT JOIN hcd.disease d " +
            " where hcc.active = true AND hcc.campaignId = :campaignId")
    List<HealthCheckCampaignFlatData> findHealthCheckCampaignDetails(@Param("campaignId") Long campaignId);

    HealthCheckCampaign findTopByActiveTrueOrderByCreatedAtDesc();
}
