package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.HealthCheckConsentForm;
import com.schoolhealth.schoolmedical.entity.HealthCheckDisease;
import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckConsentRes;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckDiseaseRes;
import com.schoolhealth.schoolmedical.model.dto.response.PupilRes;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-24T17:30:08+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Amazon.com Inc.)"
)
@Component
public class HealthCheckConsentMapperImpl implements HealthCheckConsentMapper {

    @Override
    public HealthCheckConsentRes toDto(HealthCheckConsentForm healthCheckConsentForm) {
        if ( healthCheckConsentForm == null ) {
            return null;
        }

        HealthCheckConsentRes.HealthCheckConsentResBuilder healthCheckConsentRes = HealthCheckConsentRes.builder();

        healthCheckConsentRes.pupilRes( pupilToPupilRes( healthCheckConsentForm.getPupil() ) );
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

    protected PupilRes pupilToPupilRes(Pupil pupil) {
        if ( pupil == null ) {
            return null;
        }

        PupilRes.PupilResBuilder pupilRes = PupilRes.builder();

        pupilRes.pupilId( pupil.getPupilId() );
        pupilRes.lastName( pupil.getLastName() );
        pupilRes.firstName( pupil.getFirstName() );
        pupilRes.birthDate( pupil.getBirthDate() );
        pupilRes.gender( pupil.getGender() );
        pupilRes.avatar( pupil.getAvatar() );

        return pupilRes.build();
    }

    protected HealthCheckDiseaseRes healthCheckDiseaseToHealthCheckDiseaseRes(HealthCheckDisease healthCheckDisease) {
        if ( healthCheckDisease == null ) {
            return null;
        }

        HealthCheckDiseaseRes.HealthCheckDiseaseResBuilder healthCheckDiseaseRes = HealthCheckDiseaseRes.builder();

        healthCheckDiseaseRes.healthCheckDiseaseId( healthCheckDisease.getHealthCheckDiseaseId() );

        return healthCheckDiseaseRes.build();
    }

    protected List<HealthCheckDiseaseRes> healthCheckDiseaseListToHealthCheckDiseaseResList(List<HealthCheckDisease> list) {
        if ( list == null ) {
            return null;
        }

        List<HealthCheckDiseaseRes> list1 = new ArrayList<HealthCheckDiseaseRes>( list.size() );
        for ( HealthCheckDisease healthCheckDisease : list ) {
            list1.add( healthCheckDiseaseToHealthCheckDiseaseRes( healthCheckDisease ) );
        }

        return list1;
    }
}
