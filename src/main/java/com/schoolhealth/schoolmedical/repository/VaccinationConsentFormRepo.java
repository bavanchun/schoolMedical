package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.entity.VaccinationCampagin;
import com.schoolhealth.schoolmedical.entity.VaccinationConsentForm;
import com.schoolhealth.schoolmedical.entity.enums.ConsentFormStatus;
import com.schoolhealth.schoolmedical.entity.enums.GradeLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VaccinationConsentFormRepo extends JpaRepository<VaccinationConsentForm,Long> {
    List<VaccinationConsentForm> findByCampaign(VaccinationCampagin campaign);
    List<VaccinationConsentForm> findByCampaignAndStatus(VaccinationCampagin campaign, ConsentFormStatus status);

    List<VaccinationConsentForm> findByPupilAndIsActiveTrue(Pupil pupil);

    Optional<VaccinationConsentForm> findByCampaignAndPupil(VaccinationCampagin campaign, Pupil pupil);

//    @Query("SELECT vcf FROM VaccinationConsentForm vcf WHERE vcf.status = :status AND vcf.campaign.formDeadline < :currentTime AND vcf.isActive = true")
//    List<VaccinationConsentForm> findExpiredWaitingForms(@Param("status") ConsentFormStatus status, @Param("currentTime") LocalDateTime currentTime);
    // Removed: findExpiredWaitingForms() - no longer needed with new business logic where default status is REJECTED

    @Query("SELECT vcf FROM VaccinationConsentForm vcf JOIN vcf.pupil p JOIN p.pupilGrade pg WHERE vcf.campaign.campaignId = :campaignId AND vcf.status = :status AND vcf.isActive = true ORDER BY pg.grade.gradeLevel, p.firstName")
    List<VaccinationConsentForm> findByCampaignIdAndStatusOrderByGradeAndName(@Param("campaignId") Long campaignId, @Param("status") ConsentFormStatus status);

    @Query("SELECT vcf FROM VaccinationConsentForm vcf JOIN vcf.pupil p JOIN p.pupilGrade pg WHERE vcf.campaign.campaignId = :campaignId AND vcf.status = :status AND pg.grade.gradeLevel = :gradeLevel AND vcf.isActive = true ORDER BY p.firstName")
    List<VaccinationConsentForm> findByCampaignIdAndStatusAndGradeLevelOrderByName(@Param("campaignId") Long campaignId, @Param("status") ConsentFormStatus status, @Param("gradeLevel") GradeLevel gradeLevel);

    @Query("SELECT vcf FROM VaccinationConsentForm vcf JOIN vcf.pupil p JOIN p.pupilGrade pg WHERE vcf.campaign.campaignId = :campaignId AND vcf.isActive = true ORDER BY pg.grade.gradeLevel, p.firstName")
    List<VaccinationConsentForm> findAllActiveByCampaignIdOrderByGradeAndName(@Param("campaignId") Long campaignId);
}
