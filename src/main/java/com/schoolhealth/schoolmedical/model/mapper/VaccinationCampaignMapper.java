package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.VaccinationCampagin;
import com.schoolhealth.schoolmedical.model.dto.request.VaccinationCampaignRequest;
import com.schoolhealth.schoolmedical.model.dto.response.VaccinationCampaignResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VaccinationCampaignMapper {
    VaccinationCampagin toVaccinationCampaign(VaccinationCampaignRequest request);

    @Mapping(target = "campaignId", ignore = true)
    @Mapping(target = "vaccine", ignore = true)
    @Mapping(target = "disease", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "vaccinationHistories", ignore = true)
    @Mapping(target = "consentForms", ignore = true)
    void updateCampaignFromRequest(VaccinationCampaignRequest request, @MappingTarget VaccinationCampagin campaign);
    @Mapping(source = "vaccine.name", target = "vaccineName")
    @Mapping(source = "disease.name", target = "diseaseName")
    @Mapping(source = "status", target = "status")
    VaccinationCampaignResponse toVaccinationCampaignResponse(VaccinationCampagin campaign);

}
