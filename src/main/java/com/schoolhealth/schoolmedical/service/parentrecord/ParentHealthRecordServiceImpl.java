package com.schoolhealth.schoolmedical.service.parentrecord;

import com.schoolhealth.schoolmedical.entity.HealthConditionHistory;
import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.exception.UpdateNotAllowedException;
import com.schoolhealth.schoolmedical.model.dto.request.ParentHealthRecordRequest;
import com.schoolhealth.schoolmedical.model.dto.response.ParentHealthRecordResponse;
import com.schoolhealth.schoolmedical.model.mapper.ParentHealthRecordMapper;
import com.schoolhealth.schoolmedical.repository.ParentHealthRecordRepository;
import com.schoolhealth.schoolmedical.repository.ParentPupilRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Basic CRUD implementation for parent managed health records.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ParentHealthRecordServiceImpl implements ParentHealthRecordService {

    private final ParentHealthRecordRepository parentHealthRecordRepository;
    private final ParentHealthRecordMapper parentHealthRecordMapper;
    private final ParentPupilRepo parentPupilRepo;

    @Override
    public ParentHealthRecordResponse createParentHealthRecord(String parentId, ParentHealthRecordRequest request) {
        Pupil pupil = validatePupilOwnership(parentId, request.getPupilId());

        HealthConditionHistory entity = parentHealthRecordMapper.toEntity(request);
        entity.setPupil(pupil);
        entity.setActive(request.getIsActive() != null ? request.getIsActive() : true);

        HealthConditionHistory saved = parentHealthRecordRepository.save(entity);
        return parentHealthRecordMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParentHealthRecordResponse> getAllParentHealthRecordsByPupil(String parentId, String pupilId) {
        validatePupilOwnership(parentId, pupilId);
        List<HealthConditionHistory> entities = parentHealthRecordRepository.findByPupilIdAndParentId(pupilId, parentId);
        return parentHealthRecordMapper.toDtoList(entities);
    }

    @Override
    public ParentHealthRecordResponse updateParentHealthRecord(Long id, String parentId, ParentHealthRecordRequest request) {
        HealthConditionHistory existing = fetchRecordForParent(id, parentId);
        existing.setName(request.getName());
        existing.setReactionOrNote(request.getReactionOrNote());
        existing.setImageUrl(request.getImageUrl());
        existing.setTypeHistory(request.getTypeHistory());
        existing.setActive(request.getIsActive() != null ? request.getIsActive() : existing.isActive());
        if (request.getPupilId() != null && !request.getPupilId().equals(existing.getPupil().getPupilId())) {
            Pupil pupil = validatePupilOwnership(parentId, request.getPupilId());
            existing.setPupil(pupil);
        }
        HealthConditionHistory updated = parentHealthRecordRepository.save(existing);
        return parentHealthRecordMapper.toDto(updated);
    }

    @Override
    public void deleteParentHealthRecord(Long id, String parentId) {
        HealthConditionHistory existing = fetchRecordForParent(id, parentId);
        existing.setActive(false);
        parentHealthRecordRepository.save(existing);
    }

    private HealthConditionHistory fetchRecordForParent(Long id, String parentId) {
        HealthConditionHistory record = parentHealthRecordRepository.findById(id)
                .orElseThrow(() -> new UpdateNotAllowedException("Health record not found"));
        boolean owns = parentPupilRepo.existsActiveByIdAndParentId(
                record.getPupil().getPupilId(), parentId);
        if (!owns) {
            throw new UpdateNotAllowedException("Health record not found or not your child");
        }
        return record;
    }

    private Pupil validatePupilOwnership(String parentId, String pupilId) {
        Pupil pupil = parentPupilRepo.findActiveByIdAndParentId(pupilId, parentId)
                .orElseThrow(() -> new UpdateNotAllowedException("Pupil not found or not your child"));
        return pupil;
    }
}
