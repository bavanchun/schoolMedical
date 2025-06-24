package com.schoolhealth.schoolmedical.service.vaccinationCampaign;

import com.schoolhealth.schoolmedical.entity.enums.VaccinationCampaignStatus;
import com.schoolhealth.schoolmedical.model.dto.request.VaccinationCampaignRequest;
import com.schoolhealth.schoolmedical.model.dto.response.NewestCampaignResponse;
import com.schoolhealth.schoolmedical.model.dto.response.VaccinationCampaignResponse;

import java.util.List;

public interface VaccinationCampaignService {
    VaccinationCampaignResponse createCampaign(VaccinationCampaignRequest request);
    VaccinationCampaignResponse updateCampaign(Long campaignId, VaccinationCampaignRequest request);
    void publishCampaign(Long campaignId);
    void updateStatus(Long campaignId, VaccinationCampaignStatus newStatus);
    List<VaccinationCampaignResponse> getAllCampaigns();
    VaccinationCampaignResponse getCampaignById(Long campaignId);
    NewestCampaignResponse getNewestCampaign();
}
