package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.HealthCheckConsentForm;
import com.schoolhealth.schoolmedical.entity.HealthCheckDisease;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckConsentRes;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckConsentSimpleRes;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckDiseaseRes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {PupilMapper.class, DiseaseMapper.class})
public interface HealthCheckConsentMapper {
    @Mapping(target = "pupilRes", source = "pupil")
    @Mapping(target = "disease", source = "healthCheckDiseases")
    @Mapping(target = "consentFormId" , source = "consentFormId")
    HealthCheckConsentRes toDto(HealthCheckConsentForm healthCheckConsentForm);
    List<HealthCheckConsentRes> toDtoList(List<HealthCheckConsentForm> healthCheckConsentForms);

    @Mapping(target = "disease", source = "healthCheckDiseases", qualifiedByName = "filterApprovedDiseases")
    HealthCheckConsentSimpleRes toSimpleRes(HealthCheckConsentForm healthCheckConsentForm);
    List<HealthCheckConsentSimpleRes> toSimpleResList(List<HealthCheckConsentForm> healthCheckConsentForms);

    @Named("filterApprovedDiseases")
    default List<HealthCheckDisease> filterApprovedDiseases(List<HealthCheckDisease> diseases) {
        if (diseases == null) return Collections.emptyList();
        return diseases.stream()
                .filter(d -> "APPROVED".equalsIgnoreCase(d.getStatus().name()))
                .collect(Collectors.toList());
    }
}
