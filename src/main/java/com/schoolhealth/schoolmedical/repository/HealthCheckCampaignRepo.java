package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.HealthCheckCampaign;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckCampaignFlatData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HealthCheckCampaignRepo extends JpaRepository<HealthCheckCampaign, Long> {
    @Query("SELECT new com.schoolhealth.schoolmedical.model.dto.response.HealthCheckCampaignFlatData(" +
            "hcc.campaignId, hcc.address, hcc.description, hcc.deadlineDate, hcc.startExaminationDate, hcc.endExaminationDate, hcc.createdAt, hcc.statusHealthCampaign, " +
            "hccs.consentFormId, hccs.schoolYear, " +
            "pp.pupilId, pp.lastName, pp.firstName, pp.birthDate, pp.gender, g.gradeName, " +
            "d.name) " +
            "FROM HealthCheckCampaign hcc " +
            "JOIN hcc.healthCheckConsentForms hccs " +
            "JOIN hccs.pupil pp " +
            "JOIN pp.grade g " +
            "JOIN hccs.healthCheckDiseases hcd " +
            "JOIN hcd.disease d " +
            " where hcc.isActive = true AND hcc.campaignId = :campaignId")
    List<HealthCheckCampaignFlatData> findHealthCheckCampaignDetails(@Param("campaignId") Long campaignId);
}
