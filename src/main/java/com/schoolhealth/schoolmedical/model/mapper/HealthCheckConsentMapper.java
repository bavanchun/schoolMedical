package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.HealthCheckConsentForm;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckConsentRes;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckConsentSimpleRes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PupilMapper.class, DiseaseMapper.class})
public interface HealthCheckConsentMapper {
    @Mapping(target = "pupilRes", source = "pupil")
    @Mapping(target = "disease", source = "healthCheckDiseases")
    @Mapping(target = "consentFormId" , source = "consentFormId")
    HealthCheckConsentRes toDto(HealthCheckConsentForm healthCheckConsentForm);
    List<HealthCheckConsentRes> toDtoList(List<HealthCheckConsentForm> healthCheckConsentForms);

    @Mapping(target = "disease", source = "healthCheckDiseases")
    HealthCheckConsentSimpleRes toSimpleRes(HealthCheckConsentForm healthCheckConsentForm);
    List<HealthCheckConsentSimpleRes> toSimpleResList(List<HealthCheckConsentForm> healthCheckConsentForms);

}
