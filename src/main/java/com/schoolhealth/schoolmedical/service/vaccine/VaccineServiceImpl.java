package com.schoolhealth.schoolmedical.service.vaccine;

import com.schoolhealth.schoolmedical.entity.Vaccine;
import com.schoolhealth.schoolmedical.exception.VaccineAlreadyExistsException;
import com.schoolhealth.schoolmedical.model.dto.request.VaccineRequest;
import com.schoolhealth.schoolmedical.model.dto.response.VaccineResponse;
import com.schoolhealth.schoolmedical.model.mapper.VaccineMapper;
import com.schoolhealth.schoolmedical.repository.VaccineRepo;
import com.schoolhealth.schoolmedical.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        if (vaccineRepository.findByNameIgnoreCase(request.getName()).isPresent()) {
            throw new VaccineAlreadyExistsException("Vaccine with name " + request.getName() + " already exists");
        }
        Vaccine vaccine = vaccineMapper.toEntity(request);
        return vaccineMapper.toDto(vaccineRepository.save(vaccine));
    }

    @Override
    public VaccineResponse updateVaccine(int id, VaccineRequest request) {
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
    public void deleteVaccine(int id) {
        Vaccine existing = vaccineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vaccine", "id", id));
        existing.setActive(false);
        vaccineRepository.save(existing);
    }

    @Override
    public VaccineResponse getVaccineById(int id) {
        Vaccine vaccine = vaccineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vaccine", "id", id));
        return vaccineMapper.toDto(vaccine);
    }

//    @Override
//    public List<VaccineResponse> getAllVaccines() {
//        return vaccineRepository.findByIsActiveTrue()
//                .stream()
//                .map(vaccineMapper::toDto)
//                .collect(Collectors.toList());
//    }
    @Override
    public Page<VaccineResponse> getAllVaccines(int pageNo, int pageSize, boolean isActive) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        Page<Vaccine> vaccinePage = vaccineRepository.findByIsActiveTrue(isActive, pageable);
        return vaccinePage.map(vaccineMapper::toDto);
    }
}
