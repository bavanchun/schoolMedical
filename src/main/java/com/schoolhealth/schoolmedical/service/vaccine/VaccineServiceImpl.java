package com.schoolhealth.schoolmedical.service.vaccine;

import com.schoolhealth.schoolmedical.entity.Vaccine;
import com.schoolhealth.schoolmedical.model.dto.request.VaccineRequest;
import com.schoolhealth.schoolmedical.model.dto.response.VaccineResponse;
import com.schoolhealth.schoolmedical.model.mapper.VaccineMapper;
import com.schoolhealth.schoolmedical.repository.VaccineRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VaccineServiceImpl implements VaccineService{

    private final VaccineRepo vaccineRepository;
    private final VaccineMapper vaccineMapper;
    @Override
    public VaccineResponse createVaccine(VaccineRequest request) {
        Vaccine vaccine = vaccineMapper.toEntity(request);
        return vaccineMapper.toDto(vaccineRepository.save(vaccine));
    }

    @Override
    public VaccineResponse updateVaccine(int id, VaccineRequest request) {
        Vaccine existing = vaccineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vaccine not found"));
        existing.setName(request.getName());
        existing.setManufacturer(request.getManufacturer());
        existing.setRecommendedAge(request.getRecommendedAge());
        existing.setDescription(request.getDescription());
        existing.setDoseNumber(request.getDoseNumber());
        return vaccineMapper.toDto(vaccineRepository.save(existing));
    }

    @Override
    public void deleteVaccine(int id) {
        Vaccine existing = vaccineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vaccine not found"));
        existing.setActive(false);
        vaccineRepository.save(existing);
    }

    @Override
    public VaccineResponse getVaccineById(int id) {
        Vaccine vaccine = vaccineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vaccine not found"));
        return vaccineMapper.toDto(vaccine);
    }

    @Override
    public List<VaccineResponse> getAllVaccines() {
        return vaccineRepository.findByIsActiveTrue()
                .stream()
                .map(vaccineMapper::toDto)
                .collect(Collectors.toList());
    }
}
