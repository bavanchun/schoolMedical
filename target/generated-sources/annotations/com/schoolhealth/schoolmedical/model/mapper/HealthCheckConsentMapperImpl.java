package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.HealthCheckConsentForm;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckConsentRes;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckConsentSimpleRes;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-27T17:27:49+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Amazon.com Inc.)"
)
@Component
public class HealthCheckConsentMapperImpl implements HealthCheckConsentMapper {

    @Autowired
    private PupilMapper pupilMapper;
    @Autowired
    private DiseaseMapper diseaseMapper;

    @Override
    public HealthCheckConsentRes toDto(HealthCheckConsentForm healthCheckConsentForm) {
        if ( healthCheckConsentForm == null ) {
            return null;
        }

        HealthCheckConsentRes.HealthCheckConsentResBuilder healthCheckConsentRes = HealthCheckConsentRes.builder();

        healthCheckConsentRes.pupilRes( pupilMapper.toDto( healthCheckConsentForm.getPupil() ) );
        healthCheckConsentRes.disease( diseaseMapper.toHealthCheckDiseaseDtoList( healthCheckConsentForm.getHealthCheckDiseases() ) );
        healthCheckConsentRes.consentFormId( healthCheckConsentForm.getConsentFormId() );
        healthCheckConsentRes.schoolYear( healthCheckConsentForm.getSchoolYear() );

        return healthCheckConsentRes.build();
    }

    @Override
    public List<HealthCheckConsentRes> toDtoList(List<HealthCheckConsentForm> healthCheckConsentForms) {
        if ( healthCheckConsentForms == null ) {
            return null;
        }

        List<HealthCheckConsentRes> list = new ArrayList<HealthCheckConsentRes>( healthCheckConsentForms.size() );
        for ( HealthCheckConsentForm healthCheckConsentForm : healthCheckConsentForms ) {
            list.add( toDto( healthCheckConsentForm ) );
        }

        return list;
    }

    @Override
    public HealthCheckConsentSimpleRes toSimpleRes(HealthCheckConsentForm healthCheckConsentForm) {
        if ( healthCheckConsentForm == null ) {
            return null;
        }

        HealthCheckConsentSimpleRes.HealthCheckConsentSimpleResBuilder healthCheckConsentSimpleRes = HealthCheckConsentSimpleRes.builder();

        healthCheckConsentSimpleRes.disease( diseaseMapper.toHealthCheckDiseaseDtoList( filterApprovedDiseases( healthCheckConsentForm.getHealthCheckDiseases() ) ) );
        healthCheckConsentSimpleRes.schoolYear( healthCheckConsentForm.getSchoolYear() );

        return healthCheckConsentSimpleRes.build();
    }

    @Override
    public List<HealthCheckConsentSimpleRes> toSimpleResList(List<HealthCheckConsentForm> healthCheckConsentForms) {
        if ( healthCheckConsentForms == null ) {
            return null;
        }

        List<HealthCheckConsentSimpleRes> list = new ArrayList<HealthCheckConsentSimpleRes>( healthCheckConsentForms.size() );
        for ( HealthCheckConsentForm healthCheckConsentForm : healthCheckConsentForms ) {
            list.add( toSimpleRes( healthCheckConsentForm ) );
        }

        return list;
    }
}
