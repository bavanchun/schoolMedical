package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.entity.HealthConditionHistory;
import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.entity.enums.HealthTypeHistory;
import com.schoolhealth.schoolmedical.repository.HealthConditionHistoryRepository;
import com.schoolhealth.schoolmedical.repository.PupilRepo;
import com.schoolhealth.schoolmedical.model.dto.request.HealthConditionRequest;
import com.schoolhealth.schoolmedical.model.dto.response.HealthConditionResponse;
import com.schoolhealth.schoolmedical.service.HealthConditionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Lombok tạo constructor cho các final field
public class HealthConditionServiceImpl implements HealthConditionService {

    private final HealthConditionHistoryRepository healthConditionHistoryRepository;
    private final PupilRepo pupilRepository;

    /**
     * Chuyển đổi từ entity sang DTO response (có thể dùng MapStruct hoặc ModelMapper nếu muốn tự động hóa)
     */
    private HealthConditionResponse toResponse(HealthConditionHistory entity) {
        HealthConditionResponse res = new HealthConditionResponse();
        res.setConditionId(entity.getConditionHistoryId().intValue()); // hoặc .longValue() nếu DTO dùng Long
        res.setName(entity.getName());
        res.setReactionOrNote(entity.getReactionOrNote());
        res.setImageUrl(entity.getImageUrl());
        res.setTypeHistory(entity.getTypeHistory().name()); // "ALLERGY" hoặc "MEDICAL_HISTORY"
        res.setPupilId(entity.getPupil().getPupilId());
        res.setActive(entity.isActive());
        return res;
    }

    @Override
    public HealthConditionResponse create(HealthConditionRequest request) {
        // Lấy học sinh theo pupilId
        Pupil pupil = pupilRepository.findById(request.getPupilId())
                .orElseThrow(() -> new RuntimeException("Pupil not found with id: " + request.getPupilId()));

        HealthConditionHistory entity = HealthConditionHistory.builder()
                .name(request.getName())
                .reactionOrNote(request.getReactionOrNote())
                .imageUrl(request.getImageUrl())
                .typeHistory(HealthTypeHistory.valueOf(request.getTypeHistory())) // Yêu cầu request gửi đúng: "ALLERGY" hoặc "MEDICAL_HISTORY"
                .isActive(true)
                .pupil(pupil)
                .build();

        healthConditionHistoryRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    public List<HealthConditionResponse> getAllByPupil(String pupilId) {
        return healthConditionHistoryRepository.findByPupil_PupilIdAndIsActiveTrue(pupilId)
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public HealthConditionResponse update(Long id, HealthConditionRequest request) {
        HealthConditionHistory entity = healthConditionHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("HealthConditionHistory not found with id: " + id));

        if (request.getName() != null) entity.setName(request.getName());
        if (request.getReactionOrNote() != null) entity.setReactionOrNote(request.getReactionOrNote());
        if (request.getImageUrl() != null) entity.setImageUrl(request.getImageUrl());
        if (request.getTypeHistory() != null)
            entity.setTypeHistory(HealthTypeHistory.valueOf(request.getTypeHistory()));

        healthConditionHistoryRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    public void delete(Long id) {
        HealthConditionHistory entity = healthConditionHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("HealthConditionHistory not found with id: " + id));
        entity.setActive(false);
        healthConditionHistoryRepository.save(entity);
    }
}