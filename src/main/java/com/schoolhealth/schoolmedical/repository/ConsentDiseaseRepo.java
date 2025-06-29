package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.ConsentDisease;
import com.schoolhealth.schoolmedical.entity.ConsentDiseaseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsentDiseaseRepo extends JpaRepository<ConsentDisease, ConsentDiseaseId> {
}
