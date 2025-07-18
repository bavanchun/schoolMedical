package com.schoolhealth.schoolmedical.service.vaccine;

import com.schoolhealth.schoolmedical.entity.Vaccine;
import com.schoolhealth.schoolmedical.exception.EntityNotFoundException;
import com.schoolhealth.schoolmedical.exception.VaccineAlreadyExistsException;
import com.schoolhealth.schoolmedical.model.dto.request.VaccineRequest;
import com.schoolhealth.schoolmedical.model.dto.response.VaccineResponse;
import com.schoolhealth.schoolmedical.model.mapper.VaccineMapper;
import com.schoolhealth.schoolmedical.repository.VaccineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VaccineServiceImpl implements  VaccineService {

    private final VaccineRepository vaccineRepository;
    private final VaccineMapper vaccineMapper;

    @Override
    public VaccineResponse createVaccine(VaccineRequest request) {
        Vaccine vaccine = vaccineMapper.toEntity(request);
        return vaccineMapper.toDto(vaccineRepository.save(vaccine));
    }

    @Override
    public VaccineResponse updateVaccine(Long id, VaccineRequest request) {
        Vaccine existing = vaccineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vaccine", "id", id));
        existing.setName(request.getName());
        existing.setManufacturer(request.getManufacturer());
        existing.setRecommendedAge(request.getRecommendedAge());
        existing.setDescription(request.getDescription());
        existing.setDoseNumber(request.getDoseNumber());
        return vaccineMapper.toDto(vaccineRepository.save(existing));
    }

    @Override
    public void deleteVaccine(Long id) {
        Vaccine existing = vaccineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vaccine", "id", id));
        existing.setActive(false);
        vaccineRepository.save(existing);
    }

    @Override
    public VaccineResponse getVaccineById(Long id) {
        Vaccine vaccine = vaccineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vaccine", "id", id));
        return vaccineMapper.toDto(vaccine);
    }

    @Override
    public Page<VaccineResponse> getAllVaccines(int pageNo, int pageSize, boolean isActive) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        Page<Vaccine> vaccinePage = vaccineRepository.findByIsActiveTrue(isActive, pageable);
        return vaccinePage.map(vaccineMapper::toDto);
    }
}
