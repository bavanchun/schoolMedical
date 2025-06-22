package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.VaccinationCampagin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccinationCampaignRepo extends JpaRepository<VaccinationCampagin, Long> {
}
