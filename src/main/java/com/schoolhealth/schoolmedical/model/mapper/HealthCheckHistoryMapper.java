package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.HealthCheckHistory;
import com.schoolhealth.schoolmedical.model.dto.request.HealthCheckHistoryReq;
import com.schoolhealth.schoolmedical.model.dto.request.UpdateHealthCheckHistoryReq;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckHistoryRes;
import org.hibernate.sql.Update;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {HealthCheckConsentMapper.class, DiseaseMapper.class})
public interface HealthCheckHistoryMapper {

    @Mappings({
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "active", constant = "true"),
            @Mapping(target = "healthCheckConsentForm", ignore = true),
            @Mapping(target = "healthId", ignore = true)
    })
    HealthCheckHistory toHealthCheckHistory(HealthCheckHistoryReq historyReq);


    @Mapping(target = "healthId", source = "healthId")
    HealthCheckHistoryRes toHealthCheckHistoryRes(HealthCheckHistory history);

    @Mapping(target = "stage", source = "stage")
    HealthCheckHistoryRes toHealthCheckHistoryResWithStage(HealthCheckHistory history, int stage);



    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "healthCheckConsentForm", ignore = true)
    @Mapping(target = "active", ignore = true)
    void updateEntityFromDto(UpdateHealthCheckHistoryReq dto, @MappingTarget HealthCheckHistory entity);

}
