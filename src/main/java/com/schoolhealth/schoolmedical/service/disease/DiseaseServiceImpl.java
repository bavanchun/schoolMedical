package com.schoolhealth.schoolmedical.service.disease;

import com.schoolhealth.schoolmedical.entity.Disease;
import com.schoolhealth.schoolmedical.exception.DiseaseAlreadyExistsException;
import com.schoolhealth.schoolmedical.exception.EntityNotFoundException;
import com.schoolhealth.schoolmedical.model.dto.request.DiseaseRequest;
import com.schoolhealth.schoolmedical.model.dto.response.DiseaseResponse;
import com.schoolhealth.schoolmedical.model.mapper.DiseaseMapper;
import com.schoolhealth.schoolmedical.repository.DiseaseRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiseaseServiceImpl implements DiseaseService{

    private final DiseaseRepo diseaseRepository;
    private final DiseaseMapper diseaseMapper;
    @Override
    /*
    public DiseaseResponse createDisease(DiseaseRequest request) {
        Disease disease = diseaseMapper.toEntity(request);
        return diseaseMapper.toDto(diseaseRepository.save(disease));
    }
     */
    public DiseaseResponse createDisease(DiseaseRequest request) {
        if (diseaseRepository.findByNameIgnoreCase(request.getName()).isPresent()) {
            throw new DiseaseAlreadyExistsException("Disease with name " + request.getName() + " already exists");
        }
        Disease disease = diseaseMapper.toEntity(request);
        Disease savedDisease = diseaseRepository.save(disease);
        return diseaseMapper.toDto(savedDisease);
    }

    @Override
    public DiseaseResponse updateDisease(int id, DiseaseRequest request) {
        Disease existingDisease = diseaseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Disease", "id", id));
        existingDisease.setName(request.getName());
        existingDisease.setDescription(request.getDescription());
        existingDisease.setIsInjectedVaccination(request.isInjectedVaccination());
        existingDisease.setDoseQuantity(request.getDoseQuantity());
        return diseaseMapper.toDto(diseaseRepository.save(existingDisease));
    }

    @Override
    public void deleteDisease(int id) {
        Disease existing = diseaseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Disease", "id", id));
        existing.setActive(false);
        diseaseRepository.save(existing);

    }

    @Override
    public DiseaseResponse getDiseaseById(int id) {
        Disease disease = diseaseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Disease", "id", id));
        return diseaseMapper.toDto(disease);
    }

    @Override
    public List<DiseaseResponse> getAllDiseases() {
        return diseaseRepository.findAllByisActiveTrue()
                .stream()
                .map(diseaseMapper::toDto)
                .collect(Collectors.toList());
    }
}
