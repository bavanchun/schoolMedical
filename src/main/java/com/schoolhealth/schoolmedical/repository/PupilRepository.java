package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.Pupil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PupilRepository extends JpaRepository<Pupil, String> {
    List<Pupil> findByPupilId(String pupilId);


}
