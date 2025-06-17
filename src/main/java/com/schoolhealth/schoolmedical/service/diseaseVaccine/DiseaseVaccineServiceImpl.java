package com.schoolhealth.schoolmedical.service.diseaseVaccine;

import com.schoolhealth.schoolmedical.entity.DiseaseVaccine;
import com.schoolhealth.schoolmedical.entity.DiseaseVaccineId;
import com.schoolhealth.schoolmedical.model.dto.request.DiseaseVaccineRequest;
import com.schoolhealth.schoolmedical.model.dto.response.DiseaseVaccineResponse;
import com.schoolhealth.schoolmedical.model.dto.response.VaccineResponse;
import com.schoolhealth.schoolmedical.model.mapper.DiseaseVaccineMapper;
import com.schoolhealth.schoolmedical.model.mapper.VaccineMapper;
import com.schoolhealth.schoolmedical.repository.DiseaseRepo;
import com.schoolhealth.schoolmedical.repository.DiseaseVaccineRepository;
import jakarta.persistence.EntityNotFoundException;
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

    @Override
    public DiseaseVaccineResponse create(DiseaseVaccineRequest request) {
        DiseaseVaccine entity = mapper.toEntity(request);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public void delete(int diseaseId, int vaccineId) {
        DiseaseVaccine entity = repository.findById(new DiseaseVaccineId(diseaseId, vaccineId))
                .orElseThrow(() -> new EntityNotFoundException("Mapping not found"));
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
            throw new EntityNotFoundException("Disease with id " + diseaseId + " not found");
        }

        // Gọi phương thức repository để lấy danh sách vaccine
        return repository.findVaccinesByDiseaseId(diseaseId).stream()
                .map(vaccineMapper::toDto)
                .collect(Collectors.toList());
    }
}
