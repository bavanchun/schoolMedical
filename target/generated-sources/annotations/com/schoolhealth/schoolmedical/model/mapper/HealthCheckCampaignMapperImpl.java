package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.HealthCheckCampaign;
import com.schoolhealth.schoolmedical.entity.enums.StatusHealthCampaign;
import com.schoolhealth.schoolmedical.model.dto.request.HealthCheckCampaginReq;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckCampaignRes;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-20T19:03:10+0700",
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
    public HealthCheckCampaignRes toDto(HealthCheckCampaign healthCheckCampaign) {
        if ( healthCheckCampaign == null ) {
            return null;
        }

        HealthCheckCampaignRes.HealthCheckCampaignResBuilder healthCheckCampaignRes = HealthCheckCampaignRes.builder();

        healthCheckCampaignRes.campaignId( healthCheckCampaign.getCampaignId() );
        healthCheckCampaignRes.address( healthCheckCampaign.getAddress() );
        healthCheckCampaignRes.description( healthCheckCampaign.getDescription() );
        healthCheckCampaignRes.deadlineDate( healthCheckCampaign.getDeadlineDate() );
        healthCheckCampaignRes.startExaminationDate( healthCheckCampaign.getStartExaminationDate() );
        healthCheckCampaignRes.endExaminationDate( healthCheckCampaign.getEndExaminationDate() );
        healthCheckCampaignRes.createdAt( healthCheckCampaign.getCreatedAt() );
        healthCheckCampaignRes.statusHealthCampaign( healthCheckCampaign.getStatusHealthCampaign() );

        return healthCheckCampaignRes.build();
    }
}
