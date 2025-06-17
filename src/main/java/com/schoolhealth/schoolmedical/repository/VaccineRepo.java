package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccineRepo extends JpaRepository<Vaccine,Integer> {
}
