package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.HealthCheckConsentForm;
import com.schoolhealth.schoolmedical.entity.HealthCheckDisease;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckConsentRes;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckConsentSimpleRes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {PupilMapper.class, DiseaseMapper.class, HealthCheckHistoryMapper.class})
public interface HealthCheckConsentMapper {
    @Mapping(target = "pupilRes", source = "pupil")
    @Mapping(target = "disease", source = "consentDiseases")
    @Mapping(target = "consentFormId" , source = "consentFormId")
    @Mapping(target = "healthCheckHistoryRes", source = "healthCheckHistory")
    HealthCheckConsentRes toDto(HealthCheckConsentForm healthCheckConsentForm);
    List<HealthCheckConsentRes> toDtoList(List<HealthCheckConsentForm> healthCheckConsentForms);

    @Mapping(target = "disease", source = "consentDiseases")
    HealthCheckConsentSimpleRes toSimpleRes(HealthCheckConsentForm healthCheckConsentForm);
    List<HealthCheckConsentSimpleRes> toSimpleResList(List<HealthCheckConsentForm> healthCheckConsentForms);

}
