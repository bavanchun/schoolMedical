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
            @Mapping(target = "disease", ignore = true),
            @Mapping(target = "vaccine", ignore = true),
            @Mapping(target = "status", source = "status", defaultValue = "PENDING")
    })
    VaccinationCampagin toEntity(VaccinationCampaignRequest request);

    @Mappings({
            @Mapping(source = "campaignId", target = "campaignId"),
            @Mapping(source = disease.diseaseName", target = "diseaseName"),
            @Mapping(source = "vaccine.vaccineName", target = "vaccineName"),
            @Mapping(source = "note", target = "notes")
    })
    VaccinationCampaignResponse toDto(VaccinationCampagin campaign);
}
