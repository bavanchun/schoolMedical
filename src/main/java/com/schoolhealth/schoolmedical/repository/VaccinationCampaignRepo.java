package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.VaccinationCampagin;
import com.schoolhealth.schoolmedical.entity.enums.VaccinationCampaignStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VaccinationCampaignRepo extends JpaRepository<VaccinationCampagin, Long> {
    @Query("SELECT v FROM VaccinationCampagin v WHERE v.status = :status ORDER BY v.campaignId DESC")
    Optional<VaccinationCampagin> findTopByStatusOrderByCampaignIdDesc(@Param("status") VaccinationCampaignStatus status);

    // Optimized query to fetch all campaigns with vaccine and disease in one query
    @Query("SELECT DISTINCT v FROM VaccinationCampagin v " +
            "LEFT JOIN FETCH v.vaccine " +
            "LEFT JOIN FETCH v.disease " +
            "ORDER BY v.campaignId DESC")
    List<VaccinationCampagin> findAllWithVaccineAndDisease();

    // Optimized query to find campaign by ID with related entities
    @Query("SELECT v FROM VaccinationCampagin v " +
            "LEFT JOIN FETCH v.vaccine " +
            "LEFT JOIN FETCH v.disease " +
            "WHERE v.campaignId = :campaignId")
    Optional<VaccinationCampagin> findByIdWithVaccineAndDisease(@Param("campaignId") Long campaignId);

    // Optimized query for newest published campaign with related entities
    @Query("SELECT v FROM VaccinationCampagin v " +
            "LEFT JOIN FETCH v.vaccine " +
            "LEFT JOIN FETCH v.disease " +
            "WHERE v.status = :status " +
            "ORDER BY v.campaignId DESC")
    Optional<VaccinationCampagin> findTopByStatusWithVaccineAndDisease(@Param("status") VaccinationCampaignStatus status);
}
