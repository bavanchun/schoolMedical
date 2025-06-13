package com.schoolhealth.schoolmedical.service.pupil;

import com.schoolhealth.schoolmedical.entity.Pupil;
import java.util.List;
import java.util.Optional;

public interface PupilService {
    Optional<Pupil> findByPupilId(String pupilId);

    List<Pupil> findAll();
    Pupil save(Pupil pupil);
    void delete(String pupilId);
}
