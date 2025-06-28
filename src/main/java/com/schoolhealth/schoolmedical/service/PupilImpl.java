package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.constant.ValidationConstants;
import com.schoolhealth.schoolmedical.entity.Grade;
import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.entity.User;
import com.schoolhealth.schoolmedical.model.dto.PupilDto;
import com.schoolhealth.schoolmedical.model.dto.request.AssignClassRequest;
import com.schoolhealth.schoolmedical.model.mapper.PupilMapper;
import com.schoolhealth.schoolmedical.repository.GradeRepository;
import com.schoolhealth.schoolmedical.repository.PupilRepo;
import com.schoolhealth.schoolmedical.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class PupilImpl implements PupilService{

    @Autowired
    private PupilRepo pupilRepo;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PupilMapper pupilMapper;


    @Override
    @Transactional
    public PupilDto createPupil(PupilDto dto) {
        // Kiểm tra ngày sinh
        if (dto.getBirthDate() != null && dto.getBirthDate().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ValidationConstants.BIRTH_DATE_MESSAGE);
        }

        // Kiểm tra số điện thoại của phụ huynh
        if (dto.getParentPhoneNumber() != null && !Pattern.matches(ValidationConstants.PHONE_NUMBER_REGEX, dto.getParentPhoneNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ValidationConstants.PHONE_NUMBER_MESSAGE);
        }

        // Tìm phụ huynh theo số điện thoại
        User parent = null;
        if (dto.getParentPhoneNumber() != null) {
            parent = userRepository.findActiveParentByPhoneNumber(dto.getParentPhoneNumber())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ValidationConstants.PARENT_PHONE_NOT_EXIST_MESSAGE));
        }

        Pupil entity = pupilMapper.toEntity(dto);

        // Đảm bảo học sinh mới luôn được đánh dấu là active
        entity.setActive(true);

        // Khởi tạo danh sách phụ huynh nếu cần
        if (entity.getParents() == null) {
            entity.setParents(new ArrayList<>());
        }

        // Lưu học sinh vào database
        Pupil saved = pupilRepo.save(entity);

        // Tạo mối quan hệ giữa học sinh và phụ huynh trong bảng pupil_parent
        if (parent != null) {
            // Thêm phụ huynh vào danh sách của học sinh
            saved.getParents().add(parent);
            // Lưu lại để cập nhật quan hệ
            saved = pupilRepo.save(saved);
        }

        return pupilMapper.toDto(saved);
    }

    @Override
    public List<PupilDto> getAllPupils() {
        // Chỉ lấy các học sinh có isActive = true
        return pupilRepo.findAll().stream()
                .filter(Pupil::isActive) // Lọc những học sinh đang active
                .map(pupilMapper::toDto)
                .toList();
    }

    public Optional<List<Pupil>> getAll() {
        return Optional.of(pupilRepo.findAll());
    }

    public PupilDto getPupilById(String id) {
        Optional<Pupil> pupilOptional = pupilRepo.findById(id);
        if (pupilOptional.isEmpty() || !pupilOptional.get().isActive()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pupil not found");
        }
        return pupilMapper.toDto(pupilOptional.get());
    }

    @Override
    @Transactional
    public PupilDto updatePupil(String id, PupilDto dto) {
        Optional<Pupil> existingOptional = pupilRepo.findById(id);
        if (existingOptional.isEmpty() || !existingOptional.get().isActive()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pupil not found");
        }

        // Kiểm tra ngày sinh
        if (dto.getBirthDate() != null && dto.getBirthDate().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ValidationConstants.BIRTH_DATE_MESSAGE);
        }

        // Kiểm tra số điện thoại của phụ huynh
        if (dto.getParentPhoneNumber() != null && !Pattern.matches(ValidationConstants.PHONE_NUMBER_REGEX, dto.getParentPhoneNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ValidationConstants.PHONE_NUMBER_MESSAGE);
        }

        Pupil existing = existingOptional.get();

        // Cập nhật mối quan hệ phụ huynh - học sinh nếu số điện thoại phụ huynh thay đổi
        if (dto.getParentPhoneNumber() != null &&
            !dto.getParentPhoneNumber().equals(existing.getParentPhoneNumber())) {

            // Tìm phụ huynh mới theo số điện thoại
            User newParent = userRepository.findActiveParentByPhoneNumber(dto.getParentPhoneNumber())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            ValidationConstants.PARENT_PHONE_NOT_EXIST_MESSAGE));

            // Khởi tạo danh sách phụ huynh nếu cần
            if (existing.getParents() == null) {
                existing.setParents(new ArrayList<>());
            } else {
                // Tìm phụ huynh cũ dựa trên số điện thoại cũ (nếu có)
                String oldPhoneNumber = existing.getParentPhoneNumber();
                if (oldPhoneNumber != null && !oldPhoneNumber.isEmpty()) {
                    // Xóa quan hệ với phụ huynh cũ (nếu có)
                    existing.getParents().removeIf(parent ->
                        oldPhoneNumber.equals(parent.getPhoneNumber()));
                }
            }

            // Thêm quan hệ với phụ huynh mới
            if (!existing.getParents().contains(newParent)) {
                existing.getParents().add(newParent);
            }
        }

        // Cập nhật thông tin cơ bản của học sinh
        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setBirthDate(dto.getBirthDate());
        existing.setGender(dto.getGender());
        existing.setParentPhoneNumber(dto.getParentPhoneNumber());
        existing.setActive(true); // Đảm bảo pupil luôn active khi update

        // Lưu thay đổi
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
        // pupilId => pupil
        Optional<Pupil> pupilOptional = pupilRepo.findById(assignClassRequest.getPupilId());
        if (pupilOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy học sinh với ID đã cung cấp");
        }
        Pupil pupil = pupilOptional.get();

        // gradeId => grade
        Optional<Grade> gradeOptional = gradeRepository.findById(assignClassRequest.getGradeId());
        if (gradeOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy lớp học với ID đã cung cấp");
        }
        Grade grade = gradeOptional.get();

        pupil.setGrade(grade);

        return pupilRepo.save(pupil);
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
