package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.HealthCheckCampaign;
import com.schoolhealth.schoolmedical.model.dto.request.HealthCheckCampaignReq;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckCampaignRes;
import com.schoolhealth.schoolmedical.model.dto.response.LatestHealthCheckCampaignRes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = DiseaseMapper.class)
public interface HealthCheckCampaignMapper {
    @Mappings({
            @Mapping(target = "campaignId", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "active", constant = "true"),
            @Mapping(target = "statusHealthCampaign", constant = "PENDING"),
            @Mapping(target = "healthCheckConsentForms", ignore = true),
            @Mapping(target = "healthCheckDiseases", ignore = true),
    })
    HealthCheckCampaign toEntity(HealthCheckCampaignReq healthCheckCampaign);
    @Mapping(target = "diseases", source = "healthCheckDiseases")
    HealthCheckCampaignRes toDto(HealthCheckCampaign healthCheckCampaign);
    LatestHealthCheckCampaignRes toLatestDto(HealthCheckCampaign healthCheckCampaign);
    List<HealthCheckCampaignRes> toDto(List<HealthCheckCampaign> healthCheckCampaigns);

    @Mapping(target = "campaignId", ignore = true)
    @Mapping(target = "createdAt" , ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "healthCheckConsentForms", ignore = true)
    @Mapping(target = "healthCheckDiseases", ignore = true)
    void updateEntityFromDto(HealthCheckCampaignReq healthCheckCampaign, @MappingTarget HealthCheckCampaign healthCheckCampaignEntity);
}