package com.schoolhealth.schoolmedical.service.vaccinationHistory;

import com.schoolhealth.schoolmedical.entity.VaccinationHistory;
import com.schoolhealth.schoolmedical.model.dto.request.VaccinationHistoryRequest;
import com.schoolhealth.schoolmedical.model.dto.response.VaccinationHistoryResponse;
import com.schoolhealth.schoolmedical.model.mapper.VaccinationHistoryMapper;
import com.schoolhealth.schoolmedical.repository.VaccinationHistoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VaccinationHistoryServiceImpl implements VaccinationHistoryService {
    private final VaccinationHistoryRepository repository;
    private final VaccinationHistoryMapper mapper;

    @Override
    public VaccinationHistoryResponse create(VaccinationHistoryRequest request) {
        // Validate pupil, vaccine, disease, campaign existence as needed
        VaccinationHistory entity = mapper.toEntity(request);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public VaccinationHistoryResponse update(int historyId, VaccinationHistoryRequest request) {
        VaccinationHistory existing = repository.findByHistoryIdAndIsActiveTrue(historyId);
        if (existing == null) throw new EntityNotFoundException("VaccinationHistory not found");
        // Map fields (except historyId)
        existing.setPupilId(request.getPupilId());
        existing.setVaccineId(request.getVaccineId());
        existing.setCampaignId(request.getCampaignId());
        existing.setSource(request.getSource());
        existing.setVaccinatedAt(request.getVaccinatedAt());
        existing.setNotes(request.getNotes());
        return mapper.toDto(repository.save(existing));
    }

    @Override
    public void delete(int historyId) {
        VaccinationHistory existing = repository.findByHistoryIdAndIsActiveTrue(historyId);
        if (existing == null) throw new EntityNotFoundException("VaccinationHistory not found");
        existing.setActive(false);
        repository.save(existing);
    }

    @Override
    public VaccinationHistoryResponse getById(int historyId) {
        VaccinationHistory entity = repository.findByHistoryIdAndIsActiveTrue(historyId);
        if (entity == null) throw new EntityNotFoundException("VaccinationHistory not found");
        return mapper.toDto(entity);
    }

    @Override
    public List<VaccinationHistoryResponse> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VaccinationHistoryResponse> getByPupil(String pupilId) {
        return repository.findByPupilIdAndIsActiveTrue(pupilId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
