package com.schoolhealth.schoolmedical.service.vaccinationCampaign;

import com.schoolhealth.schoolmedical.entity.VaccinationCampagin;
import com.schoolhealth.schoolmedical.entity.enums.VaccinationCampaignStatus;
import com.schoolhealth.schoolmedical.exception.EntityNotFoundException;
import com.schoolhealth.schoolmedical.model.dto.request.VaccinationCampaignRequest;
import com.schoolhealth.schoolmedical.model.dto.response.VaccinationCampaignResponse;
import com.schoolhealth.schoolmedical.model.mapper.VaccinationCampaignMapper;
import com.schoolhealth.schoolmedical.repository.DiseaseRepo;
import com.schoolhealth.schoolmedical.repository.VaccinationCampaignRepo;
import com.schoolhealth.schoolmedical.repository.VaccineRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VaccinationCampaignServiceImpl implements VaccinationCampaignService {

    private final VaccinationCampaignRepo vaccinationCampaignRepo;
    private final VaccineRepo vaccineRepo;
    private final DiseaseRepo diseaseRepo;
    private final VaccinationCampaignMapper vaccinationCampaignMapper;


    @Override
    @Transactional
    public VaccinationCampaignResponse createCampaign(VaccinationCampaignRequest request) {
        // validate existence of vaccine and disease
        diseaseRepo.findById(request.getDiseaseId())
                .orElseThrow(() -> new EntityNotFoundException("Disease", "id", request.getDiseaseId()));
        vaccineRepo.findById(request.getVaccineId())
                .orElseThrow(() -> new EntityNotFoundException("Vaccine", "id", request.getVaccineId()));

        // map request to entity
        VaccinationCampagin campaign = vaccinationCampaignMapper.toEntity(request);
        campaign.setStatus(VaccinationCampaignStatus.PENDING); // set default status
        // save campaign
        VaccinationCampagin savedCampaign = vaccinationCampaignRepo.save(campaign);

        // map entity to response DTO
        return vaccinationCampaignMapper.toDto(savedCampaign);
    }

    @Override
    @Transactional(readOnly = true)
    public VaccinationCampaignResponse getCampaignById(int id) {
        return vaccinationCampaignRepo.findById(id)
                .map(vaccinationCampaignMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Campaign", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<VaccinationCampaignResponse> getAllCampaigns() {
        return vaccinationCampaignRepo.findAll().stream()
                .map(vaccinationCampaignMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public VaccinationCampaignResponse updateCampaign(int id, VaccinationCampaignRequest request) {
        VaccinationCampagin campaign = vaccinationCampaignRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Campaign", "id", id));

        if (campaign.getStatus() != VaccinationCampaignStatus.PENDING) {
            throw new IllegalStateException("Only campaigns with PENDING status can be updated.");
        }

        // validate existence of vaccine and disease
        diseaseRepo.findById(request.getDiseaseId())
                .orElseThrow(() -> new EntityNotFoundException("Disease", "id", request.getDiseaseId()));
        vaccineRepo.findById(request.getVaccineId())
                .orElseThrow(() -> new EntityNotFoundException("Vaccine", "id", request.getVaccineId()));

        // Manually update fields from the request
        campaign.setTitleCampaign(request.getTitleCampaign());
        campaign.setDiseaseId(request.getDiseaseId());
        campaign.setVaccineId(request.getVaccineId());
        campaign.setDoseNumber(request.getDoseNumber());
        campaign.setStartDate(request.getStartDate());
        campaign.setEndDate(request.getEndDate());
        campaign.setFormDeadline(request.getFormDeadline());
        campaign.setNotes(request.getNotes());

        VaccinationCampagin updatedCampaign = vaccinationCampaignRepo.save(campaign);
        return vaccinationCampaignMapper.toDto(updatedCampaign);
    }

    @Override
    @Transactional
    public void deleteCampaign(int id) {
        VaccinationCampagin campaign = vaccinationCampaignRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Campaign", "id", id));

        if (campaign.getStatus() != VaccinationCampaignStatus.PENDING) {
            throw new IllegalStateException("Only campaigns with PENDING status can be deleted.");
        }

        // Soft delete by setting isActive to false
        campaign.setActive(false);
        vaccinationCampaignRepo.save(campaign);
    }
}
