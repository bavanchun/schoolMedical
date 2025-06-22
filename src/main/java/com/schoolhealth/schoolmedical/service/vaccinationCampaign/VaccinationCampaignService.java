package com.schoolhealth.schoolmedical.service.vaccinationCampaign;

import com.schoolhealth.schoolmedical.model.dto.request.VaccinationCampaignRequest;
import com.schoolhealth.schoolmedical.model.dto.response.VaccinationCampaignResponse;

public interface VaccinationCampaignService {
    VaccinationCampaignResponse createCampaign(VaccinationCampaignRequest request);
    VaccinationCampaignResponse updateCampaign(Long campaignId, VaccinationCampaignRequest request);
}
