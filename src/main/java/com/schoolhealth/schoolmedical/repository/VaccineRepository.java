package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaccineRepository extends JpaRepository<Vaccine, Long> {

}