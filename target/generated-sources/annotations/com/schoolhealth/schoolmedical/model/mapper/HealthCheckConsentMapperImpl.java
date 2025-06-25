package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.HealthCheckConsentForm;
import com.schoolhealth.schoolmedical.entity.HealthCheckDisease;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckConsentRes;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckDiseaseRes;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-25T02:51:40+0700",
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
        healthCheckConsentRes.disease( healthCheckDiseaseListToHealthCheckDiseaseResList( healthCheckConsentForm.getHealthCheckDiseases() ) );
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

    protected List<HealthCheckDiseaseRes> healthCheckDiseaseListToHealthCheckDiseaseResList(List<HealthCheckDisease> list) {
        if ( list == null ) {
            return null;
        }

        List<HealthCheckDiseaseRes> list1 = new ArrayList<HealthCheckDiseaseRes>( list.size() );
        for ( HealthCheckDisease healthCheckDisease : list ) {
            list1.add( diseaseMapper.toHealthCheckDiseaseDto( healthCheckDisease ) );
        }

        return list1;
    }
}
