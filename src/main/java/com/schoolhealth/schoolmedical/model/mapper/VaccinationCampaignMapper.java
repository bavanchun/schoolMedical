package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.VaccinationCampagin;
import com.schoolhealth.schoolmedical.model.dto.request.VaccinationCampaignRequest;
import com.schoolhealth.schoolmedical.model.dto.response.VaccinationCampaignResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface VaccinationCampaignMapper {
    @Mappings({
            @Mapping(target = "campaignId", ignore = true),
            @Mapping(target = "vaccinationHistories", ignore = true),
            @Mapping(target = "consentForms", ignore = true),
            @Mapping(target = "disease", ignore = true),
            @Mapping(target = "vaccine", ignore = true),
            @Mapping(target = "isActive", constant = "true"),
            @Mapping(target = "status", source = "status", defaultValue = "PENDING")
    })
    VaccinationCampagin toEntity(VaccinationCampaignRequest request);

    VaccinationCampaignResponse toDto(VaccinationCampagin campaign);
}
