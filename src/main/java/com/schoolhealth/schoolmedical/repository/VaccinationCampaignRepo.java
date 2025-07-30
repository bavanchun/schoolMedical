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
    @Query("SELECT v FROM VaccinationCampagin v WHERE v.status = :status AND v.isActive = true ORDER BY v.campaignId DESC")
    Optional<VaccinationCampagin> findTopByStatusOrderByCampaignIdDesc(@Param("status") VaccinationCampaignStatus status);

    // Optimized query to fetch all active campaigns with vaccine and disease in one query
    @Query("SELECT DISTINCT v FROM VaccinationCampagin v " +
            "LEFT JOIN FETCH v.vaccine " +
            "LEFT JOIN FETCH v.disease " +
            "WHERE v.isActive = true " +
            "ORDER BY v.campaignId DESC")
    List<VaccinationCampagin> findAllWithVaccineAndDisease();

    // Optimized query to find active campaign by ID with related entities
    @Query("SELECT v FROM VaccinationCampagin v " +
            "LEFT JOIN FETCH v.vaccine " +
            "LEFT JOIN FETCH v.disease " +
            "WHERE v.campaignId = :campaignId AND v.isActive = true")
    Optional<VaccinationCampagin> findByIdWithVaccineAndDisease(@Param("campaignId") Long campaignId);

    // Optimized query for newest published active campaign with related entities
    @Query("SELECT v FROM VaccinationCampagin v " +
            "LEFT JOIN FETCH v.vaccine " +
            "LEFT JOIN FETCH v.disease " +
            "WHERE v.status = :status AND v.isActive = true " +
            "ORDER BY v.campaignId DESC")
    Optional<VaccinationCampagin> findTopByStatusWithVaccineAndDisease(@Param("status") VaccinationCampaignStatus status);

    // Find active campaign by ID (for delete operations)
    @Query("SELECT v FROM VaccinationCampagin v WHERE v.campaignId = :campaignId AND v.isActive = true")
    Optional<VaccinationCampagin> findByIdAndIsActiveTrue(@Param("campaignId") Long campaignId);

    @Query("""
        SELECT
            vc.titleCampaign,
            d.name,
            v.name,
            vcf.status,
            COUNT(vcf.consentFormId)
        FROM VaccinationCampagin vc
        JOIN vc.disease d
        JOIN vc.vaccine v
        LEFT JOIN VaccinationConsentForm vcf ON vc.campaignId = vcf.campaign.campaignId
        WHERE vc.isActive = true AND YEAR(vc.startDate) = :year
        GROUP BY vc.campaignId, vc.titleCampaign, d.name, v.name, vcf.status
        ORDER BY vc.startDate ASC, vcf.status
    """)
    List<Object[]> getVaccinationCampaignStatsByYear(@Param("year") int year);
}
