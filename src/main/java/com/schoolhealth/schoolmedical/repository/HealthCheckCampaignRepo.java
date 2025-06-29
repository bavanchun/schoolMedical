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
import java.util.Optional;

@Repository
public interface HealthCheckCampaignRepo extends JpaRepository<HealthCheckCampaign, Long> {

    Optional<HealthCheckCampaign> findHealthCheckCampaignByCampaignId (Long campaignId);

    @Query("SELECT h FROM HealthCheckCampaign h WHERE h.statusHealthCampaign in (com.schoolhealth.schoolmedical.entity.enums.StatusHealthCampaign.PUBLISHED,com.schoolhealth.schoolmedical.entity.enums.StatusHealthCampaign.IN_PROGRESS, com.schoolhealth.schoolmedical.entity.enums.StatusHealthCampaign.COMPLETED) ORDER BY h.createdAt DESC LIMIT 1")
    HealthCheckCampaign findStatusCampaignPublishedInProgressOrderByCreatedAtDesc();

    @Query("SELECT h FROM HealthCheckCampaign h ORDER BY h.createdAt DESC LIMIT 1")
    HealthCheckCampaign findCurrentCampaign();

    List<HealthCheckCampaign> findAllByActiveTrue();
}
