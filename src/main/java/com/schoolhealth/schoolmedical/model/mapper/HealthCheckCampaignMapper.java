package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.HealthCheckCampaign;
import com.schoolhealth.schoolmedical.model.dto.request.HealthCheckCampaginReq;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckCampaignRes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface HealthCheckCampaignMapper {
    @Mappings({
            @Mapping(target = "campaignId", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "isActive", constant = "true"),
            @Mapping(target = "statusHealthCampaign", constant = "PENDING"),
            @Mapping(target = "healthCheckConsentForms", ignore = true),
            @Mapping(target = "healthCheckDiseases", ignore = true)
    })
    HealthCheckCampaign toEntity(HealthCheckCampaginReq healthCheckCampaign);
    HealthCheckCampaignRes toDto(HealthCheckCampaign healthCheckCampaign);
}