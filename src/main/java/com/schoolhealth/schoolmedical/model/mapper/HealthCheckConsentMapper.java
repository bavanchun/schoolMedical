package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.HealthCheckConsentForm;
import com.schoolhealth.schoolmedical.entity.HealthCheckDisease;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckConsentRes;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckConsentSimpleRes;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {PupilMapper.class, DiseaseMapper.class, HealthCheckHistoryMapper.class})
public interface HealthCheckConsentMapper {
    @Named("toDto")
    @Mapping(target = "pupilRes", source = "pupil")
    @Mapping(target = "disease", source = "consentDiseases")
    @Mapping(target = "consentFormId" , source = "consentFormId")
    @Mapping(target = "healthCheckHistoryRes", source = "healthCheckHistory")
    @Mapping(target = "active", source = "active")
    HealthCheckConsentRes toDto(HealthCheckConsentForm healthCheckConsentForm);
    @IterableMapping(qualifiedByName = "toDto")
    List<HealthCheckConsentRes> toDtoList(List<HealthCheckConsentForm> healthCheckConsentForms);

    @Mapping(target = "disease", source = "consentDiseases")
    HealthCheckConsentSimpleRes toSimpleRes(HealthCheckConsentForm healthCheckConsentForm);
    List<HealthCheckConsentSimpleRes> toSimpleResList(List<HealthCheckConsentForm> healthCheckConsentForms);


    @Mapping(target = "disease", source = "consentDiseases")
    @Mapping(target = "consentFormId" , source = "consentFormId")
    @Mapping(target = "healthCheckHistoryRes", source = "healthCheckHistory")
    @Mapping(target = "active", source = "active")
    HealthCheckConsentRes toConsentDtoByPupil(HealthCheckConsentForm healthCheckConsentForm);

}
