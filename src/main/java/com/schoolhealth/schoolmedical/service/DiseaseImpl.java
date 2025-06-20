package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.entity.Disease;
import com.schoolhealth.schoolmedical.exception.DiseaseAlreadyExistsException;
import com.schoolhealth.schoolmedical.exception.EntityNotFoundException;
import com.schoolhealth.schoolmedical.exception.NotFoundException;
import com.schoolhealth.schoolmedical.model.dto.request.DiseaseRequest;
import com.schoolhealth.schoolmedical.model.dto.response.DiseaseResponse;
import com.schoolhealth.schoolmedical.model.mapper.DiseaseMapper;
import com.schoolhealth.schoolmedical.repository.DiseaseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiseaseImpl implements DiseaseService{

    @Autowired
    private DiseaseRepo diseaseRepo;

    @Autowired
    private DiseaseMapper diseaseMapper;

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
    public List<Disease> getAllDiseases() {
        List<Disease> diseases = diseaseRepo.findAllByisActiveTrue();
        if (diseases.isEmpty()) {
            throw new NotFoundException("No diseases found");
        }
        return diseases;
    }
}
