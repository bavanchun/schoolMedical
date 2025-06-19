package com.schoolhealth.schoolmedical.service.vaccinationCampaign;

import com.schoolhealth.schoolmedical.model.dto.request.VaccinationCampaignRequest;
import com.schoolhealth.schoolmedical.model.dto.response.VaccinationCampaignResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VaccinationCampaignServiceImpl implements VaccinationCampaignService {
    @Override
    public VaccinationCampaignResponse create(VaccinationCampaignRequest request) {
        return null;
    }

    @Override
    public VaccinationCampaignResponse update(int id, VaccinationCampaignRequest request) {
        return null;
    }

    @Override
    public VaccinationCampaignResponse getById(int id) {
        return null;
    }

    @Override
    public List<VaccinationCampaignResponse> getAll() {
        return List.of();
    }

    @Override
    public void delete(int id) {

    }
}
