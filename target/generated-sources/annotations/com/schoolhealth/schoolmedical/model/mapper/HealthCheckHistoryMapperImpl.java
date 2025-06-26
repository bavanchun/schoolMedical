package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.HealthCheckHistory;
import com.schoolhealth.schoolmedical.model.dto.request.HealthCheckHistoryReq;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckHistoryRes;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-27T00:08:57+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Amazon.com Inc.)"
)
@Component
public class HealthCheckHistoryMapperImpl implements HealthCheckHistoryMapper {

    @Autowired
    private HealthCheckConsentMapper healthCheckConsentMapper;

    @Override
    public HealthCheckHistory toHealthCheckHistory(HealthCheckHistoryReq historyReq) {
        if ( historyReq == null ) {
            return null;
        }

        HealthCheckHistory.HealthCheckHistoryBuilder healthCheckHistory = HealthCheckHistory.builder();

        healthCheckHistory.height( historyReq.getHeight() );
        healthCheckHistory.weight( historyReq.getWeight() );
        healthCheckHistory.rightEyeVision( historyReq.getRightEyeVision() );
        healthCheckHistory.leftEyeVision( historyReq.getLeftEyeVision() );
        healthCheckHistory.bloodPressure( historyReq.getBloodPressure() );
        healthCheckHistory.heartRate( historyReq.getHeartRate() );
        healthCheckHistory.dentalCheck( historyReq.getDentalCheck() );
        healthCheckHistory.earCondition( historyReq.getEarCondition() );
        healthCheckHistory.noseCondition( historyReq.getNoseCondition() );
        healthCheckHistory.throatCondition( historyReq.getThroatCondition() );
        healthCheckHistory.skinAndMucosa( historyReq.getSkinAndMucosa() );
        healthCheckHistory.hearAnuscultaion( historyReq.getHearAnuscultaion() );
        healthCheckHistory.chestShape( historyReq.getChestShape() );
        healthCheckHistory.lungs( historyReq.getLungs() );
        healthCheckHistory.digestiveSystem( historyReq.getDigestiveSystem() );
        healthCheckHistory.urinarySystem( historyReq.getUrinarySystem() );
        healthCheckHistory.musculoskeletalSystem( historyReq.getMusculoskeletalSystem() );
        healthCheckHistory.neurologyAndPsychiatry( historyReq.getNeurologyAndPsychiatry() );
        healthCheckHistory.genitalExamination( historyReq.getGenitalExamination() );
        healthCheckHistory.additionalNotes( historyReq.getAdditionalNotes() );
        healthCheckHistory.unusualSigns( historyReq.getUnusualSigns() );

        healthCheckHistory.active( true );

        return healthCheckHistory.build();
    }

    @Override
    public HealthCheckHistoryRes toHealthCheckHistoryRes(HealthCheckHistory history) {
        if ( history == null ) {
            return null;
        }

        HealthCheckHistoryRes.HealthCheckHistoryResBuilder healthCheckHistoryRes = HealthCheckHistoryRes.builder();

        healthCheckHistoryRes.healthCheckConsentSimpleRes( healthCheckConsentMapper.toSimpleRes( history.getHealthCheckConsentForm() ) );
        healthCheckHistoryRes.height( history.getHeight() );
        healthCheckHistoryRes.weight( history.getWeight() );
        healthCheckHistoryRes.rightEyeVision( history.getRightEyeVision() );
        healthCheckHistoryRes.leftEyeVision( history.getLeftEyeVision() );
        healthCheckHistoryRes.bloodPressure( history.getBloodPressure() );
        healthCheckHistoryRes.heartRate( history.getHeartRate() );
        healthCheckHistoryRes.dentalCheck( history.getDentalCheck() );
        healthCheckHistoryRes.earCondition( history.getEarCondition() );
        healthCheckHistoryRes.noseCondition( history.getNoseCondition() );
        healthCheckHistoryRes.throatCondition( history.getThroatCondition() );
        healthCheckHistoryRes.skinAndMucosa( history.getSkinAndMucosa() );
        healthCheckHistoryRes.hearAnuscultaion( history.getHearAnuscultaion() );
        healthCheckHistoryRes.chestShape( history.getChestShape() );
        healthCheckHistoryRes.lungs( history.getLungs() );
        healthCheckHistoryRes.digestiveSystem( history.getDigestiveSystem() );
        healthCheckHistoryRes.urinarySystem( history.getUrinarySystem() );
        healthCheckHistoryRes.musculoskeletalSystem( history.getMusculoskeletalSystem() );
        healthCheckHistoryRes.neurologyAndPsychiatry( history.getNeurologyAndPsychiatry() );
        healthCheckHistoryRes.genitalExamination( history.getGenitalExamination() );
        healthCheckHistoryRes.additionalNotes( history.getAdditionalNotes() );
        healthCheckHistoryRes.unusualSigns( history.getUnusualSigns() );
        healthCheckHistoryRes.createdAt( history.getCreatedAt() );

        return healthCheckHistoryRes.build();
    }
}
