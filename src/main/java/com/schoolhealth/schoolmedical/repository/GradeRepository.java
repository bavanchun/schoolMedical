package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade, Long> {
}
