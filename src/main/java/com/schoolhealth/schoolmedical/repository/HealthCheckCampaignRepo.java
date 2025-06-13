package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.HealCheckCampaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthCheckCampaignRepo extends JpaRepository<HealCheckCampaign, Integer> {
}
