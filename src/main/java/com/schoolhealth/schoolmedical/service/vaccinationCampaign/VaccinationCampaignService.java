package com.schoolhealth.schoolmedical.service.vaccinationCampaign;

import com.schoolhealth.schoolmedical.model.dto.request.VaccinationCampaignRequest;
import com.schoolhealth.schoolmedical.model.dto.response.VaccinationCampaignResponse;

import java.util.List;

public interface VaccinationCampaignService {
    VaccinationCampaignResponse createCampaign(VaccinationCampaignRequest request);

    VaccinationCampaignResponse getCampaignById(int id);

    List<VaccinationCampaignResponse> getAllCampaigns();

    VaccinationCampaignResponse updateCampaign(int id, VaccinationCampaignRequest request);

    void deleteCampaign(int id);
}
