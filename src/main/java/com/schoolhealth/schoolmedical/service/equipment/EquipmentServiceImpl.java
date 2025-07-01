package com.schoolhealth.schoolmedical.service.equipment;

import com.schoolhealth.schoolmedical.entity.Equipment;
import com.schoolhealth.schoolmedical.exception.NotFoundException;
import com.schoolhealth.schoolmedical.model.dto.request.EquipmentRequest;
import com.schoolhealth.schoolmedical.model.dto.response.EquipmentResponse;
import com.schoolhealth.schoolmedical.model.mapper.EquipmentMapper;
import com.schoolhealth.schoolmedical.repository.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentMapper equipmentMapper;

    @Override
    public EquipmentResponse createEquipment(EquipmentRequest request) {
        log.info("Creating new equipment with name: {}", request.getName());

        // Validate equipment name uniqueness
        if (equipmentRepository.existsByNameAndIsActiveTrue(request.getName())) {
            throw new IllegalArgumentException("Equipment with name '" + request.getName() + "' already exists");
        }

        Equipment equipment = equipmentMapper.toEntity(request);
        Equipment savedEquipment = equipmentRepository.save(equipment);

        log.info("Successfully created equipment with ID: {}", savedEquipment.getEquipmentId());
        return equipmentMapper.toResponse(savedEquipment);
    }

    @Override
    public EquipmentResponse updateEquipment(Long equipmentId, EquipmentRequest request) {
        log.info("Updating equipment with ID: {}", equipmentId);

        Equipment existingEquipment = equipmentRepository.findByEquipmentIdAndIsActiveTrue(equipmentId)
                .orElseThrow(() -> new NotFoundException("Equipment not found with ID: " + equipmentId));

        // Validate equipment name uniqueness (excluding current equipment)
        if (equipmentRepository.existsByNameAndIsActiveTrueAndEquipmentIdNot(request.getName(), equipmentId)) {
            throw new IllegalArgumentException("Equipment with name '" + request.getName() + "' already exists");
        }

        equipmentMapper.updateEntityFromRequest(request, existingEquipment);
        Equipment updatedEquipment = equipmentRepository.save(existingEquipment);

        log.info("Successfully updated equipment with ID: {}", equipmentId);
        return equipmentMapper.toResponse(updatedEquipment);
    }

    @Override
    @Transactional(readOnly = true)
    public EquipmentResponse getEquipmentById(Long equipmentId) {
        log.info("Fetching equipment with ID: {}", equipmentId);

        Equipment equipment = equipmentRepository.findByEquipmentIdAndIsActiveTrue(equipmentId)
                .orElseThrow(() -> new NotFoundException("Equipment not found with ID: " + equipmentId));

        return equipmentMapper.toResponse(equipment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipmentResponse> getAllEquipment() {
        log.info("Fetching all active equipment");

        List<Equipment> equipments = equipmentRepository.findByIsActiveTrue();
        return equipmentMapper.toResponseList(equipments);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EquipmentResponse> getAllEquipmentWithPagination(Pageable pageable) {
        log.info("Fetching equipment with pagination: page {}, size {}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<Equipment> equipmentPage = equipmentRepository.findByIsActiveTrue(pageable);
        return equipmentPage.map(equipmentMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipmentResponse> searchEquipmentByName(String name) {
        log.info("Searching equipment by name: {}", name);

        if (name == null || name.trim().isEmpty()) {
            return getAllEquipment();
        }

        List<Equipment> equipments = equipmentRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(name.trim());
        return equipmentMapper.toResponseList(equipments);
    }

    @Override
    public void deleteEquipment(Long equipmentId) {
        log.info("Soft deleting equipment with ID: {}", equipmentId);

        Equipment equipment = equipmentRepository.findByEquipmentIdAndIsActiveTrue(equipmentId)
                .orElseThrow(() -> new NotFoundException("Equipment not found with ID: " + equipmentId));

        equipment.setIsActive(false);
        equipmentRepository.save(equipment);

        log.info("Successfully soft deleted equipment with ID: {}", equipmentId);
    }
}
