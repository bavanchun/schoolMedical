package com.schoolhealth.schoolmedical.service.parentrecord;

import com.schoolhealth.schoolmedical.entity.HealthConditionHistory;
import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.exception.NotFoundException;
import com.schoolhealth.schoolmedical.model.dto.request.ParentHealthRecordRequest;
import com.schoolhealth.schoolmedical.model.dto.response.ParentHealthRecordResponse;
import com.schoolhealth.schoolmedical.model.mapper.ParentHealthRecordMapper;
import com.schoolhealth.schoolmedical.repository.ParentHealthRecordRepository;
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

    @Override
    public ParentHealthRecordResponse createParentHealthRecord(ParentHealthRecordRequest request) {
        HealthConditionHistory entity = parentHealthRecordMapper.toEntity(request);
        // link pupil by id only to avoid using existing repositories
        Pupil pupil = new Pupil();
        pupil.setPupilId(request.getPupilId());
        entity.setPupil(pupil);
        // ensure isActive flag
        entity.setActive(request.getIsActive() != null ? request.getIsActive() : true);
        HealthConditionHistory saved = parentHealthRecordRepository.save(entity);
        return parentHealthRecordMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParentHealthRecordResponse> getAllParentHealthRecordsByPupil(String pupilId) {
        List<HealthConditionHistory> entities = parentHealthRecordRepository.findByPupil_PupilId(pupilId);
        return parentHealthRecordMapper.toDtoList(entities);
    }

    @Override
    public ParentHealthRecordResponse updateParentHealthRecord(Long id, ParentHealthRecordRequest request) {
        HealthConditionHistory existing = parentHealthRecordRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Health record not found with id: " + id));
        existing.setName(request.getName());
        existing.setReactionOrNote(request.getReactionOrNote());
        existing.setImageUrl(request.getImageUrl());
        existing.setTypeHistory(request.getTypeHistory());
        existing.setActive(request.getIsActive() != null ? request.getIsActive() : existing.isActive());
        if (request.getPupilId() != null) {
            Pupil pupil = new Pupil();
            pupil.setPupilId(request.getPupilId());
            existing.setPupil(pupil);
        }
        HealthConditionHistory updated = parentHealthRecordRepository.save(existing);
        return parentHealthRecordMapper.toDto(updated);
    }

    @Override
    public void deleteParentHealthRecord(Long id) {
        HealthConditionHistory existing = parentHealthRecordRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Health record not found with id: " + id));
        existing.setActive(false);
        parentHealthRecordRepository.save(existing);
    }
}
