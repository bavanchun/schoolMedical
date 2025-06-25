package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.HealthCheckHistory;
import com.schoolhealth.schoolmedical.model.dto.request.HealthCheckHistoryReq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface HealthCheckHistoryMapper {

    @Mappings({
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "active", constant = "true"),
            @Mapping(target = "healthCheckConsentForm", ignore = true),
            @Mapping(target = "healthId", ignore = true)
    })
    HealthCheckHistory toHealthCheckHistory(HealthCheckHistoryReq historyReq);
}
