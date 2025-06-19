package com.schoolhealth.schoolmedical.service.vaccinationCampaign;

import com.schoolhealth.schoolmedical.model.dto.request.VaccinationCampaignRequest;
import com.schoolhealth.schoolmedical.model.dto.response.VaccinationCampaignResponse;

import java.util.List;

public interface VaccinationCampaignService {
    VaccinationCampaignResponse create(VaccinationCampaignRequest request);
    VaccinationCampaignResponse update(int id, VaccinationCampaignRequest request);
    VaccinationCampaignResponse getById(int id);
    List<VaccinationCampaignResponse> getAll();
    void delete(int id);
}
