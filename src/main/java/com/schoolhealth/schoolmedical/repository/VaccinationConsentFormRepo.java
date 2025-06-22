package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.VaccinationCampagin;
import com.schoolhealth.schoolmedical.entity.VaccinationConsentForm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VaccinationConsentFormRepo extends JpaRepository<VaccinationConsentForm,Long> {
    List<VaccinationConsentForm> findByCampaign(VaccinationCampagin campaign);
}
