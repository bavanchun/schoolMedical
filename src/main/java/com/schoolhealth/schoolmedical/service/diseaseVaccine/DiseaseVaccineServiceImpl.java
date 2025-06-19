package com.schoolhealth.schoolmedical.service.diseaseVaccine;

import com.schoolhealth.schoolmedical.entity.Disease;
import com.schoolhealth.schoolmedical.entity.DiseaseVaccine;
import com.schoolhealth.schoolmedical.entity.DiseaseVaccineId;
import com.schoolhealth.schoolmedical.entity.Vaccine;
import com.schoolhealth.schoolmedical.exception.DiseaseVaccineAlreadyExistsException;
import com.schoolhealth.schoolmedical.model.dto.request.DiseaseVaccineRequest;
import com.schoolhealth.schoolmedical.model.dto.response.DiseaseVaccineResponse;
import com.schoolhealth.schoolmedical.model.dto.response.VaccineResponse;
import com.schoolhealth.schoolmedical.model.mapper.DiseaseVaccineMapper;
import com.schoolhealth.schoolmedical.model.mapper.VaccineMapper;
import com.schoolhealth.schoolmedical.repository.DiseaseRepo;
import com.schoolhealth.schoolmedical.repository.DiseaseVaccineRepository;
import com.schoolhealth.schoolmedical.exception.EntityNotFoundException;
import com.schoolhealth.schoolmedical.repository.VaccineRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiseaseVaccineServiceImpl implements DiseaseVaccineService{

    private final DiseaseVaccineRepository repository;
    private final DiseaseVaccineMapper mapper;
    private final VaccineMapper vaccineMapper;
    private final DiseaseRepo diseaseRepository;
    private final VaccineRepo vaccineRepository;

    @Override
    public DiseaseVaccineResponse create(DiseaseVaccineRequest request) {
        DiseaseVaccineId id = new DiseaseVaccineId(request.getDiseaseId(), request.getVaccineId());

        if (repository.existsById(id)) {
            throw new DiseaseVaccineAlreadyExistsException("Disease-Vaccine mapping already exists with disease ID " + id.getDiseaseId() + " and vaccine ID " + id.getVaccineId());
        }
        Disease disease = diseaseRepository.findById(request.getDiseaseId())
                .orElseThrow(() -> new EntityNotFoundException("Disease", "id", request.getDiseaseId()));
        Vaccine vaccine = vaccineRepository.findById(request.getVaccineId())
                .orElseThrow(() -> new EntityNotFoundException("Vaccine", "id", request.getVaccineId()));
        DiseaseVaccine diseaseVaccine = mapper.toEntity(request);
        diseaseVaccine.setDisease(disease);
        diseaseVaccine.setVaccine(vaccine);
        return mapper.toDto(repository.save(diseaseVaccine));
    }

    @Override
    public void delete(int diseaseId, int vaccineId) {
        DiseaseVaccineId id = new DiseaseVaccineId(diseaseId, vaccineId);
        DiseaseVaccine entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DiseaseVaccine", "id", id));
        entity.setActive(false);
        repository.save(entity);
    }

    @Override
    public List<DiseaseVaccineResponse> getAll() {
        return repository.findByIsActiveTrue().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VaccineResponse> getVaccinesByDiseaseId(int diseaseId) {
        // Kiểm tra disease có tồn tại không
        if (!diseaseRepository.existsById(diseaseId)) {
            throw new EntityNotFoundException("Disease", "id", diseaseId);
        }

        // Gọi phương thức repository để lấy danh sách vaccine
        return repository.findVaccinesByDiseaseId(diseaseId).stream()
                .map(vaccineMapper::toDto)
                .collect(Collectors.toList());
    }
}
