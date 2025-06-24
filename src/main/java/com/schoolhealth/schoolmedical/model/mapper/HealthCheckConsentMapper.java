package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.HealthCheckConsentForm;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckConsentRes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PupilMapper.class, DiseaseMapper.class})
public interface HealthCheckConsentMapper {
    @Mapping(target = "pupilRes", source = "pupil")
    @Mapping(target = "disease", source = "healthCheckDiseases")
    HealthCheckConsentRes toDto(HealthCheckConsentForm healthCheckConsentForm);
    List<HealthCheckConsentRes> toDtoList(List<HealthCheckConsentForm> healthCheckConsentForms);
}
