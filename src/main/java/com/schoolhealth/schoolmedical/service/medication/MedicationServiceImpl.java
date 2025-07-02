package com.schoolhealth.schoolmedical.service.medication;

import com.schoolhealth.schoolmedical.entity.Medication;
import com.schoolhealth.schoolmedical.exception.NotFoundException;
import com.schoolhealth.schoolmedical.model.dto.request.MedicationRequest;
import com.schoolhealth.schoolmedical.model.dto.response.MedicationResponse;
import com.schoolhealth.schoolmedical.model.mapper.MedicationMapper;
import com.schoolhealth.schoolmedical.repository.MedicationRepository;
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
public class MedicationServiceImpl implements MedicationService {

    private final MedicationRepository medicationRepository;
    private final MedicationMapper medicationMapper;

    @Override
    public MedicationResponse createMedication(MedicationRequest request) {
        log.info("Creating new medication with name: {}", request.getName());

        // Validate medication name uniqueness
        if (medicationRepository.existsByNameAndIsActiveTrue(request.getName().trim())) {
            throw new IllegalArgumentException("Medication with name '" + request.getName().trim() + "' already exists");
        }

        Medication medication = medicationMapper.toEntity(request);
        medication.setName(medication.getName().trim()); // Clean name
        Medication savedMedication = medicationRepository.save(medication);

        log.info("Successfully created medication with ID: {}", savedMedication.getMedicationId());
        return medicationMapper.toResponse(savedMedication);
    }

    @Override
    public MedicationResponse updateMedication(Long medicationId, MedicationRequest request) {
        log.info("Updating medication with ID: {}", medicationId);

        Medication existingMedication = medicationRepository.findByMedicationIdAndIsActiveTrue(medicationId)
                .orElseThrow(() -> new NotFoundException("Medication not found with ID: " + medicationId));

        // Validate medication name uniqueness (excluding current medication)
        String trimmedName = request.getName().trim();
        if (medicationRepository.existsByNameAndIsActiveTrueAndMedicationIdNot(trimmedName, medicationId)) {
            throw new IllegalArgumentException("Medication with name '" + trimmedName + "' already exists");
        }

        medicationMapper.updateEntityFromRequest(request, existingMedication);
        existingMedication.setName(existingMedication.getName().trim()); // Clean name
        Medication updatedMedication = medicationRepository.save(existingMedication);

        log.info("Successfully updated medication with ID: {}", medicationId);
        return medicationMapper.toResponse(updatedMedication);
    }

    @Override
    @Transactional(readOnly = true)
    public MedicationResponse getMedicationById(Long medicationId) {
        log.info("Fetching medication with ID: {}", medicationId);

        Medication medication = medicationRepository.findByMedicationIdAndIsActiveTrue(medicationId)
                .orElseThrow(() -> new NotFoundException("Medication not found with ID: " + medicationId));

        return medicationMapper.toResponse(medication);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicationResponse> getAllMedication() {
        log.info("Fetching all active medication");

        List<Medication> medications = medicationRepository.findByIsActiveTrue();
        return medicationMapper.toResponseList(medications);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MedicationResponse> getAllMedicationWithPagination(Pageable pageable) {
        log.info("Fetching medication with pagination: page {}, size {}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<Medication> medicationPage = medicationRepository.findByIsActiveTrue(pageable);
        return medicationPage.map(medicationMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicationResponse> searchMedicationByName(String name) {
        log.info("Searching medication by name: {}", name);

        if (name == null || name.trim().isEmpty()) {
            return getAllMedication();
        }

        List<Medication> medications = medicationRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(name.trim());
        return medicationMapper.toResponseList(medications);
    }

    @Override
    public void deleteMedication(Long medicationId) {
        log.info("Soft deleting medication with ID: {}", medicationId);

        Medication medication = medicationRepository.findByMedicationIdAndIsActiveTrue(medicationId)
                .orElseThrow(() -> new NotFoundException("Medication not found with ID: " + medicationId));

        medication.setIsActive(false);
        medicationRepository.save(medication);

        log.info("Successfully soft deleted medication with ID: {}", medicationId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medication> findMedicationByIds(List<Long> medicationIds) {
        log.info("Finding medication by IDs: {}", medicationIds);

        if (medicationIds == null || medicationIds.isEmpty()) {
            return List.of();
        }

        List<Medication> medications = medicationRepository.findByMedicationIdInAndIsActiveTrue(medicationIds);

        // Validate that all requested medication were found
        if (medications.size() != medicationIds.size()) {
            List<Long> foundIds = medications.stream().map(Medication::getMedicationId).toList();
            List<Long> notFoundIds = medicationIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();
            throw new NotFoundException("Medication not found with IDs: " + notFoundIds);
        }

        return medications;
    }
}
