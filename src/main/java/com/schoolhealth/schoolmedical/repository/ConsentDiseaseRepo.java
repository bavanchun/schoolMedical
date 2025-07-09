package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.ConsentDisease;
import com.schoolhealth.schoolmedical.entity.ConsentDiseaseId;
import com.schoolhealth.schoolmedical.entity.HealthCheckConsentForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ConsentDiseaseRepo extends JpaRepository<ConsentDisease, ConsentDiseaseId> {
    @Modifying
    @Transactional
    @Query("DELETE FROM ConsentDisease cd WHERE cd.healthCheckConsentForm = :form")
    void deleteByConsentForm(@Param("form") HealthCheckConsentForm form);

}
