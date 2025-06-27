package com.schoolhealth.schoolmedical.service.pupil;

import com.schoolhealth.schoolmedical.entity.Grade;
import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.exception.NotFoundException;
import com.schoolhealth.schoolmedical.model.dto.response.PupilRes;
import com.schoolhealth.schoolmedical.model.dto.request.AssignClassRequest;
import com.schoolhealth.schoolmedical.model.mapper.PupilMapper;
import com.schoolhealth.schoolmedical.repository.GradeRepository;
import com.schoolhealth.schoolmedical.repository.PupilRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class PupilImpl implements PupilService {

    @Autowired
    private PupilRepo pupilRepo;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private PupilMapper pupilMapper;


    @Override
    public PupilRes createPupil(PupilRes dto) {
//        Pupil entity = pupilMapper.toEntity(dto);
//
//        // Đảm bảo học sinh mới luôn được đánh dấu là active
//        entity.setActive(true);
//        Pupil saved  = pupilRepo.save(entity);
//        return pupilMapper.toDto(saved);
        return new PupilRes();
    }

    @Override
    public List<PupilRes> getAllPupils() {
        // Chỉ lấy các học sinh có isActive = true
        return pupilRepo.findAll().stream()
                .filter(Pupil::isActive) // Lọc những học sinh đang active
                .map(pupilMapper::toDto)
                .toList();
    }

//    @Override
//    public Optional<List<Pupil>> getAll() {
//        return Optional.ofNullable(pupilRepo.findAll());
    public PupilRes getPupilById(String id) {
        Optional<Pupil> pupilOptional = pupilRepo.findById(id);
        if (pupilOptional.isEmpty() || !pupilOptional.get().isActive()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pupil not found");
        }
        return pupilMapper.toDto(pupilOptional.get());
    }


    @Override
    public List<Pupil> getAll() {
        List<Pupil> pupils = pupilRepo.findAll();
        if( pupils.isEmpty()) {
            throw new NotFoundException("No pupils found");
        }
        return pupils;
    }

    @Override
    public List<PupilRes> getAllPupilsByParent(String parentId) {
        List<Pupil> pupil = pupilRepo.getAllPupilsByParent(parentId);

        return pupilMapper.toPupilGradeDtoList(pupil);
    }

    @Override
    public PupilRes getPupilGradeById(String pupilId) {
        Pupil pupil = pupilRepo.findPupilById(pupilId).orElseThrow(() -> new NotFoundException("Pupil not found"));
        return pupilMapper.toDto(pupil);
    }


    @Override
    public PupilRes updatePupil(String id, PupilRes dto) {
        Optional<Pupil> existingOptional = pupilRepo.findById(id);
        if (existingOptional.isEmpty() || !existingOptional.get().isActive()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pupil not found");
        }

        Pupil existing = existingOptional.get();
        // apply updates (keep pupilId unchanged)
        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setBirthDate(dto.getBirthDate());
        existing.setGender(dto.getGender());
        existing.setActive(true); // Đảm bảo pupil luôn active khi update
        // if you include grade or parents in dto, map them here

        Pupil updated = pupilRepo.save(existing);
        return pupilMapper.toDto(updated);
    }

    @Override
    public void deletePupil(String id) {
        Optional<Pupil> pupilOptional = pupilRepo.findById(id);
        if (pupilOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pupil not found");
        }

        // Soft delete: Đặt isActive = false thay vì xóa khỏi database
        Pupil pupil = pupilOptional.get();
        pupil.setActive(false);
        pupilRepo.save(pupil);
    }

    @Override
    public Pupil assignPupilClass(AssignClassRequest assignClassRequest) {
//        // pupilId => pupil
//        Pupil pupil = pupilRepo.findById(assignClassRequest.getPupilId()).get();
//
//        // gradeId => grade
//        Grade grade = gradeRepository.findById(assignClassRequest.getGradeId()).get();
//
//        pupil.setGrade(grade);
//
//        return pupilRepo.save(pupil);
        return new Pupil();
    }

    @Override
    public List<Pupil> findByParentPhoneNumber(String phoneNumber) {
        // Tìm học sinh theo số điện thoại phụ huynh trong trường parentPhoneNumber
        return pupilRepo.findAll().stream()
                .filter(pupil -> phoneNumber.equals(pupil.getParentPhoneNumber()))
                .filter(Pupil::isActive) // Chỉ lấy học sinh đang active
                .toList();
    }
}
