package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.entity.Disease;
import com.schoolhealth.schoolmedical.exception.DiseaseAlreadyExistsException;
import com.schoolhealth.schoolmedical.exception.EntityNotFoundException;
import com.schoolhealth.schoolmedical.exception.NotFoundException;
import com.schoolhealth.schoolmedical.model.dto.request.DiseaseRequest;
import com.schoolhealth.schoolmedical.model.dto.request.DiseaseVaccineRequest;
import com.schoolhealth.schoolmedical.model.dto.response.*;
import com.schoolhealth.schoolmedical.model.mapper.DiseaseMapper;
import com.schoolhealth.schoolmedical.repository.DiseaseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DiseaseImpl implements DiseaseService{

    @Autowired
    private DiseaseRepo diseaseRepo;

    @Autowired
    private DiseaseMapper diseaseMapper;

    @Autowired
    private com.schoolhealth.schoolmedical.repository.VaccineRepository vaccineRepo;

    @Autowired
    private com.schoolhealth.schoolmedical.model.mapper.VaccineMapper vaccineMapper;

    @Override
    public DiseaseResponse createDisease(DiseaseRequest request) {
        if (diseaseRepo.findByNameIgnoreCase(request.getName()).isPresent()) {
            throw new DiseaseAlreadyExistsException("Disease with name " + request.getName() + " already exists");
        }
        Disease disease = diseaseMapper.toEntity(request);
        Disease savedDisease = diseaseRepo.save(disease);
        return diseaseMapper.toDto(savedDisease);
    }

    @Override
    public DiseaseResponse updateDisease(Long id, DiseaseRequest request) {
        Disease existingDisease = diseaseRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Disease", "id", id));
        existingDisease.setName(request.getName());
        existingDisease.setDescription(request.getDescription());
        existingDisease.setIsInjectedVaccination(request.getIsInjectedVaccination());
        existingDisease.setDoseQuantity(request.getDoseQuantity());
        return diseaseMapper.toDto(diseaseRepo.save(existingDisease));
    }

    @Override
    public void deleteDisease(Long id) {
        Disease existing = diseaseRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Disease", "id", id));
        existing.setActive(false);
        diseaseRepo.save(existing);
    }

    @Override
    public DiseaseResponse getDiseaseById(Long id) {
        Disease disease = diseaseRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Disease", "id", id));
        return diseaseMapper.toDto(disease);
    }

    @Override
    public Page<DiseaseResponse> getAllDiseases(int pageNo, int pageSize, Boolean isActive) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        Page<Disease> diseasePage = diseaseRepo.findAllByisActiveTrue(isActive, pageable);
        return diseasePage.map(diseaseMapper::toDto);
    }

    @Override
    public List<ConsentDiseaseRes> getAllDiseasesByisInjectedVaccinationFalse() {
        List<Disease> diseases = diseaseRepo.findAllByisActiveTrueAndIsInjectedVaccinationFalse();
        if (diseases.isEmpty()) {
            throw new NotFoundException("No diseases found");
        }
        return diseaseMapper.toDiseasesDtoList(diseases);
    }



    @Override
    public List<Disease> getAllDiseasesById(List<Long> diseaseIds) {
        return diseaseRepo.findAllByDiseaseIdInAndIsActiveTrue(diseaseIds);
    }

    @Override
    public DiseaseVaccineResponse assignVaccineToDisease(DiseaseVaccineRequest request) {
        try {
            // Find disease by ID
            Disease disease = diseaseRepo.findById(request.getDiseaseId())
                    .orElseThrow(() -> new EntityNotFoundException("Disease", "id", request.getDiseaseId()));

            // Find vaccine by ID
            com.schoolhealth.schoolmedical.entity.Vaccine vaccine = vaccineRepo.findById(request.getVaccineId())
                    .orElseThrow(() -> new EntityNotFoundException("Vaccine", "id", request.getVaccineId()));

            // Check if vaccine is already assigned to this disease
            if (disease.getVaccines() != null && disease.getVaccines().stream()
                    .anyMatch(v -> v.getVaccineId().equals(request.getVaccineId()))) {
                // Vaccine already assigned
                return diseaseMapper.toDiseaseVaccineResponse(
                    disease,
                    vaccine,
                    false,
                    "Vaccine is already assigned to this disease"
                );
            }

            // Add vaccine to the disease's list
            if (disease.getVaccines() == null) {
                disease.setVaccines(new java.util.ArrayList<>());
            }
            disease.getVaccines().add(vaccine);

            // Save to database
            diseaseRepo.save(disease);

            return diseaseMapper.toDiseaseVaccineResponse(
                disease,
                vaccine,
                true,
                "Vaccine has been successfully assigned to the disease"
            );
        } catch (EntityNotFoundException e) {
            // Create a response with error information
            return DiseaseVaccineResponse.builder()
                .success(false)
                .message(e.getMessage())
                .build();
        } catch (Exception e) {
            e.printStackTrace();
            return DiseaseVaccineResponse.builder()
                .success(false)
                .message("An unexpected error occurred: " + e.getMessage())
                .build();
        }
    }

    @Override
    public DiseaseVaccineResponse removeVaccineFromDisease(DiseaseVaccineRequest request) {
        try {
            // Find disease by ID
            Disease disease = diseaseRepo.findById(request.getDiseaseId())
                    .orElseThrow(() -> new EntityNotFoundException("Disease", "id", request.getDiseaseId()));

            // Find vaccine by ID
            com.schoolhealth.schoolmedical.entity.Vaccine vaccine = vaccineRepo.findById(request.getVaccineId())
                    .orElseThrow(() -> new EntityNotFoundException("Vaccine", "id", request.getVaccineId()));

            // Check if the vaccines list is null or empty
            if (disease.getVaccines() == null || disease.getVaccines().isEmpty()) {
                return diseaseMapper.toDiseaseVaccineResponse(
                    disease,
                    vaccine,
                    false,
                    "Disease doesn't have any vaccines assigned"
                );
            }

            // Check if the vaccine exists in the list
            if (!disease.getVaccines().contains(vaccine)) {
                return diseaseMapper.toDiseaseVaccineResponse(
                    disease,
                    vaccine,
                    false,
                    "Vaccine is not assigned to this disease"
                );
            }

            // Remove vaccine from the list
            disease.getVaccines().remove(vaccine);

            // Save to database
            diseaseRepo.save(disease);

            return diseaseMapper.toDiseaseVaccineResponse(
                disease,
                vaccine,
                true,
                "Vaccine has been successfully removed from the disease"
            );
        } catch (EntityNotFoundException e) {
            // Create a response with error information
            return DiseaseVaccineResponse.builder()
                .success(false)
                .message(e.getMessage())
                .build();
        } catch (Exception e) {
            e.printStackTrace();
            return DiseaseVaccineResponse.builder()
                .success(false)
                .message("An unexpected error occurred: " + e.getMessage())
                .build();
        }
    }

    @Override
    @Transactional
    public DiseaseWithVaccinesWrapper getAllDiseasesWithVaccines() {
        List<Disease> diseases = diseaseRepo.findAllByisActiveTrue();

        List<DiseaseVaccineInfo> diseaseVaccineInfos = diseases.stream()
                .map(disease -> {
                    // Filter only active vaccines
                    List<VaccineInfo> vaccineInfos = (disease.getVaccines() != null)
                            ? disease.getVaccines().stream()
                            .filter(vaccine -> vaccine.isActive())
                            .map(vaccine -> VaccineInfo.builder()
                                    .vaccineId(vaccine.getVaccineId())
                                    .name(vaccine.getName())
                                    .build())
                            .collect(Collectors.toList())
                            : new ArrayList<>();

                    return DiseaseVaccineInfo.builder()
                            .diseaseId(disease.getDiseaseId())
                            .diseaseName(disease.getName())
                            .doseQuantity(disease.getDoseQuantity())
                            .vaccines(vaccineInfos)
                            .build();
                })
                .collect(Collectors.toList());

        return DiseaseWithVaccinesWrapper.builder()
                .getVaccineByDisease(diseaseVaccineInfos)
                .build();
    }

    @Override
    public List<VaccineResponse> getVaccinesByDiseaseId(Long diseaseId) {
        // Tìm disease theo ID
        Disease disease = diseaseRepo.findById(diseaseId)
                .orElseThrow(() -> new EntityNotFoundException("Disease", "id", diseaseId));

        // Kiểm tra nếu danh sách vaccine là null
        if (disease.getVaccines() == null) {
            return new java.util.ArrayList<>();
        }

        // Chuyển đổi các entity vaccine sang DTO và trả về
        return disease.getVaccines().stream()
                .map(vaccineMapper::toDto)
                .toList();
    }
}
