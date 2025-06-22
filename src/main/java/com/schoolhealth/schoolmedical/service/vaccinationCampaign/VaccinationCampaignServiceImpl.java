package com.schoolhealth.schoolmedical.service.vaccinationCampaign;

import com.schoolhealth.schoolmedical.entity.Disease;
import com.schoolhealth.schoolmedical.entity.VaccinationCampagin;
import com.schoolhealth.schoolmedical.entity.Vaccine;
import com.schoolhealth.schoolmedical.entity.enums.VaccinationCampaignStatus;
import com.schoolhealth.schoolmedical.exception.NotFoundException;
import com.schoolhealth.schoolmedical.model.dto.request.VaccinationCampaignRequest;
import com.schoolhealth.schoolmedical.model.dto.response.VaccinationCampaignResponse;
import com.schoolhealth.schoolmedical.model.mapper.VaccinationCampaignMapper;
import com.schoolhealth.schoolmedical.repository.DiseaseRepo;
import com.schoolhealth.schoolmedical.repository.VaccinationCampaignRepo;
import com.schoolhealth.schoolmedical.repository.VaccineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VaccinationCampaignServiceImpl implements  VaccinationCampaignService {
    private final VaccinationCampaignRepo vaccinationCampaignRepo;
    private final VaccineRepository vaccineRepository;
    private final DiseaseRepo diseaseRepo;
    private final VaccinationCampaignMapper mapper;
    @Override
    public VaccinationCampaignResponse createCampaign(VaccinationCampaignRequest request) {
        // Find Vaccine and Disease entities
        Vaccine vaccine = vaccineRepository.findById(request.getVaccineId().longValue())
                .orElseThrow(() -> new NotFoundException("Vaccine not found with id: " + request.getVaccineId()));

        Disease disease = diseaseRepo.findById(request.getDiseaseId().longValue())
                .orElseThrow(() -> new NotFoundException("Disease not found with id: " + request.getDiseaseId()));

        // Map request DTO to entity
        VaccinationCampagin campaign = mapper.toVaccinationCampaign(request);

        // Set initial status and entities
        campaign.setStatus(VaccinationCampaignStatus.PENDING);
        campaign.setVaccine(vaccine);
        campaign.setDisease(disease);

        // Save the new campaign
        VaccinationCampagin savedCampaign = vaccinationCampaignRepo.save(campaign);

        // Map entity to response DTO and return
        return mapper.toVaccinationCampaignResponse(savedCampaign);
    }


    @Override
    public VaccinationCampaignResponse updateCampaign(Long campaignId, VaccinationCampaignRequest request) {
        // Find the campaign by id
        VaccinationCampagin campaign = vaccinationCampaignRepo.findById(campaignId)
                .orElseThrow(() -> new NotFoundException("Campaign not found with id: " + campaignId));

        // Check if the campaign is in PENDING status
        if (campaign.getStatus() != VaccinationCampaignStatus.PENDING) {
            throw new IllegalStateException("Campaign can only be updated when in PENDING status.");
        }

        // Find Vaccine and Disease entities
        Vaccine vaccine = vaccineRepository.findById(request.getVaccineId().longValue())
                .orElseThrow(() -> new NotFoundException("Vaccine not found with id: " + request.getVaccineId()));

        Disease disease = diseaseRepo.findById(request.getDiseaseId().longValue())
                .orElseThrow(() -> new NotFoundException("Disease not found with id: " + request.getDiseaseId()));

        // Update the campaign entity from the request DTO
        mapper.updateCampaignFromRequest(request, campaign);
        campaign.setVaccine(vaccine);
        campaign.setDisease(disease);

        // Save the updated campaign
        VaccinationCampagin updatedCampaign = vaccinationCampaignRepo.save(campaign);

        // Map the updated entity to a response DTO and return
        return mapper.toVaccinationCampaignResponse(updatedCampaign);
    }
}
