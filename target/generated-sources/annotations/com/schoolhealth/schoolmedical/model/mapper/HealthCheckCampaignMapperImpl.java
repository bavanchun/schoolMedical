package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.HealthCheckCampaign;
import com.schoolhealth.schoolmedical.entity.enums.StatusHealthCampaign;
import com.schoolhealth.schoolmedical.model.dto.request.HealthCheckCampaginReq;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckCampaignResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-20T18:50:32+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Amazon.com Inc.)"
)
@Component
public class HealthCheckCampaignMapperImpl implements HealthCheckCampaignMapper {

    @Override
    public HealthCheckCampaign toEntity(HealthCheckCampaginReq healthCheckCampaign) {
        if ( healthCheckCampaign == null ) {
            return null;
        }

        HealthCheckCampaign.HealthCheckCampaignBuilder healthCheckCampaign1 = HealthCheckCampaign.builder();

        healthCheckCampaign1.address( healthCheckCampaign.getAddress() );
        healthCheckCampaign1.startExaminationDate( healthCheckCampaign.getStartExaminationDate() );
        healthCheckCampaign1.endExaminationDate( healthCheckCampaign.getEndExaminationDate() );
        healthCheckCampaign1.deadlineDate( healthCheckCampaign.getDeadlineDate() );
        healthCheckCampaign1.description( healthCheckCampaign.getDescription() );

        healthCheckCampaign1.isActive( true );
        healthCheckCampaign1.statusHealthCampaign( StatusHealthCampaign.PENDING );

        return healthCheckCampaign1.build();
    }

    @Override
    public HealthCheckCampaignResponse toDto(HealthCheckCampaign healthCheckCampaign) {
        if ( healthCheckCampaign == null ) {
            return null;
        }

        HealthCheckCampaignResponse.HealthCheckCampaignResponseBuilder healthCheckCampaignResponse = HealthCheckCampaignResponse.builder();

        healthCheckCampaignResponse.address( healthCheckCampaign.getAddress() );
        healthCheckCampaignResponse.description( healthCheckCampaign.getDescription() );
        healthCheckCampaignResponse.deadlineDate( healthCheckCampaign.getDeadlineDate() );
        healthCheckCampaignResponse.startExaminationDate( healthCheckCampaign.getStartExaminationDate() );
        healthCheckCampaignResponse.endExaminationDate( healthCheckCampaign.getEndExaminationDate() );
        healthCheckCampaignResponse.createdAt( healthCheckCampaign.getCreatedAt() );
        healthCheckCampaignResponse.statusHealthCampaign( healthCheckCampaign.getStatusHealthCampaign() );

        return healthCheckCampaignResponse.build();
    }
}
