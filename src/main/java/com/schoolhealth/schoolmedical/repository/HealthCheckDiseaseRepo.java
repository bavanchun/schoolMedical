package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.HealthCheckDisease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HealthCheckDiseaseRepo extends JpaRepository<HealthCheckDisease, Long> {
    @Query("SELECT h FROM HealthCheckDisease h " +
            "JOIN h.healthCheckCampaign hcc " +
            "WHERE hcc.campaignId= :campaignId " )
    List<HealthCheckDisease> findByCampaignIdAndPupilId(@Param("campaignId")Long campaignId);
}
