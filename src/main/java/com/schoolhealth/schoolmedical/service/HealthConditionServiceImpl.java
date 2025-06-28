package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.entity.HealthConditionHistory;
import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.entity.User;
import com.schoolhealth.schoolmedical.entity.enums.HealthTypeHistory;
import com.schoolhealth.schoolmedical.exception.AccessDeniedException;
import com.schoolhealth.schoolmedical.exception.DuplicateException;
import com.schoolhealth.schoolmedical.exception.NotFoundException;
import com.schoolhealth.schoolmedical.model.dto.request.HealthConditionRequest;
import com.schoolhealth.schoolmedical.model.dto.response.HealthConditionResponse;
import com.schoolhealth.schoolmedical.repository.HealthConditionHistoryRepository;
import com.schoolhealth.schoolmedical.repository.PupilRepo;
import com.schoolhealth.schoolmedical.repository.UserRepository;
import com.schoolhealth.schoolmedical.service.HealthConditionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Lombok tạo constructor cho các final field
public class HealthConditionServiceImpl implements HealthConditionService {

    private final HealthConditionHistoryRepository healthConditionHistoryRepository;
    private final PupilRepo pupilRepository;
    private final UserRepository userRepository;

    /**
     * Chuyển đổi từ entity sang DTO response (có thể dùng MapStruct hoặc ModelMapper nếu muốn tự động hóa)
     */
    private HealthConditionResponse toResponse(HealthConditionHistory entity) {
        HealthConditionResponse res = new HealthConditionResponse();
        res.setConditionId(entity.getConditionHistoryId().intValue());
        res.setName(entity.getName());
        res.setReactionOrNote(entity.getReactionOrNote());
        res.setImageUrl(entity.getImageUrl());
        res.setTypeHistory(entity.getTypeHistory().name()); // "ALLERGY" hoặc "MEDICAL_HISTORY"
        res.setPupilId(entity.getPupil().getPupilId());
        res.setActive(entity.isActive());
        return res;
    }

    /**
     * Kiểm tra xem phụ huynh đang đăng nhập có quyền thao tác với học sinh không
     */
    private Pupil validateParentOwnsPupil(String pupilId) {
        Pupil pupil = pupilRepository.findById(pupilId)
                .orElseThrow(() -> new NotFoundException("Pupil not found with id: " + pupilId));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phone = authentication.getName();

        boolean owns = phone.equals(pupil.getParentPhoneNumber());
        if (!owns) {
            User parent = userRepository.findByPhoneNumberWithPupils(phone)
                    .orElseThrow(() -> new NotFoundException("User not found"));
            owns = parent.getPupils() != null && parent.getPupils().stream()
                    .anyMatch(p -> p.getPupilId().equals(pupilId));
        }
        if (!owns) {
            throw new AccessDeniedException("You do not have permission for this pupil");
        }
        return pupil;
    }

    @Override
    public HealthConditionResponse create(HealthConditionRequest request) {
        Pupil pupil = validateParentOwnsPupil(request.getPupilId());

        HealthTypeHistory type = HealthTypeHistory.valueOf(request.getTypeHistory());
        if (healthConditionHistoryRepository.existsByPupil_PupilIdAndNameAndTypeHistoryAndIsActiveTrue(
                request.getPupilId(), request.getName(), type)) {
            throw new DuplicateException("Health condition already exists for this pupil");
        }

        HealthConditionHistory entity = HealthConditionHistory.builder()
                .name(request.getName())
                .reactionOrNote(request.getReactionOrNote())
                .imageUrl(request.getImageUrl())
                .typeHistory(type)
                .isActive(true)
                .pupil(pupil)
                .build();

        healthConditionHistoryRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    public List<HealthConditionResponse> getAllByPupil(String pupilId) {
        validateParentOwnsPupil(pupilId);
        return healthConditionHistoryRepository.findByPupil_PupilIdAndIsActiveTrue(pupilId)
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public HealthConditionResponse update(Long id, HealthConditionRequest request) {
        HealthConditionHistory entity = healthConditionHistoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("HealthConditionHistory not found with id: " + id));
        validateParentOwnsPupil(entity.getPupil().getPupilId());
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getReactionOrNote() != null) entity.setReactionOrNote(request.getReactionOrNote());
        if (request.getImageUrl() != null) entity.setImageUrl(request.getImageUrl());
        if (request.getTypeHistory() != null) {
            HealthTypeHistory type = HealthTypeHistory.valueOf(request.getTypeHistory());
            if (healthConditionHistoryRepository.existsByPupil_PupilIdAndNameAndTypeHistoryAndIsActiveTrue(
                    entity.getPupil().getPupilId(), request.getName(), type)) {
                throw new DuplicateException("Health condition already exists for this pupil");
            }
            entity.setTypeHistory(type);
        }

        healthConditionHistoryRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    public void delete(Long id) {
        HealthConditionHistory entity = healthConditionHistoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("HealthConditionHistory not found with id: " + id));
        validateParentOwnsPupil(entity.getPupil().getPupilId());
        entity.setActive(false);
        healthConditionHistoryRepository.save(entity);
    }
}