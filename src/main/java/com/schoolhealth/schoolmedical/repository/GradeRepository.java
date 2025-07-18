package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
}
