package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.HealthCheckCampaign;
import com.schoolhealth.schoolmedical.entity.enums.GradeLevel;
import com.schoolhealth.schoolmedical.entity.enums.StatusHealthCampaign;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckCampaignFlatData;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckPupilListRes;
import com.schoolhealth.schoolmedical.model.dto.response.LatestHealthCheckCampaignRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HealthCheckCampaignRepo extends JpaRepository<HealthCheckCampaign, Long> {

    Optional<HealthCheckCampaign> findHealthCheckCampaignByCampaignId (Long campaignId);

    @Query("SELECT h FROM HealthCheckCampaign h LEFT JOIN FETCH h.healthCheckDiseases WHERE h.statusHealthCampaign = com.schoolhealth.schoolmedical.entity.enums.StatusHealthCampaign.PUBLISHED ORDER BY h.createdAt DESC LIMIT 1")
    HealthCheckCampaign findStatusCampaignPublishedInProgressOrderByCreatedAtDesc();

    @Query("SELECT h FROM HealthCheckCampaign h " +
            "WHERE h.statusHealthCampaign = :status AND YEAR(h.createdAt) = :year " )
    Optional<HealthCheckCampaign> findCurrentCampaignByStatus(@Param("year") int year, @Param("status") StatusHealthCampaign status);

    List<HealthCheckCampaign> findAllByActiveTrue();

    @Query("""
        SELECT hc.title, COUNT(DISTINCT h.healthCheckConsentForm.pupil.pupilId)
        FROM HealthCheckCampaign hc
        LEFT JOIN HealthCheckConsentForm hcf ON hc.campaignId = hcf.healthCheckCampaign.campaignId
        LEFT JOIN HealthCheckHistory h ON hcf.consentFormId = h.healthCheckConsentForm.consentFormId
        WHERE hc.active = true AND YEAR(hc.createdAt) = :year AND h.active = true
        GROUP BY hc.campaignId, hc.title
        ORDER BY hc.createdAt ASC
    """)
    List<Object[]> getCampaignStatsByYear(@Param("year") int year);
}
