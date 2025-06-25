package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.VaccinationCampagin;
import com.schoolhealth.schoolmedical.entity.enums.VaccinationCampaignStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VaccinationCampaignRepo extends JpaRepository<VaccinationCampagin, Long> {
    @Query("SELECT v FROM VaccinationCampagin v WHERE v.status = :status ORDER BY v.campaignId DESC")
    Optional<VaccinationCampagin> findTopByStatusOrderByCampaignIdDesc(VaccinationCampaignStatus status);
}
