package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.HealthCheckHistory;
import com.schoolhealth.schoolmedical.model.dto.request.HealthCheckHistoryReq;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-25T16:07:42+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Amazon.com Inc.)"
)
@Component
public class HealthCheckHistoryMapperImpl implements HealthCheckHistoryMapper {

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
}
