package com.schoolhealth.schoolmedical.service.pupil;

import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.repository.PupilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PupilServiceImpl implements PupilService {

    private final PupilRepository pupilRepository;

    @Autowired
    public PupilServiceImpl(PupilRepository pupilRepository) {
        this.pupilRepository = pupilRepository;
    }

    @Override
    public Optional<Pupil> findByPupilId(String pupilId) {
        List<Pupil> pupils = pupilRepository.findByPupilId(pupilId);
        return pupils.isEmpty() ? Optional.empty() : Optional.of(pupils.get(0));
    }



    @Override
    public List<Pupil> findAll() {
        return List.of();
    }

    @Override
    public Pupil save(Pupil pupil) {
        return null;
    }

    @Override
    public void delete(String pupilId) {

    }
}
