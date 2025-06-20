package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.Disease;
import com.schoolhealth.schoolmedical.model.dto.request.DiseaseRequest;
import com.schoolhealth.schoolmedical.model.dto.response.DiseaseResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface DiseaseMapper {

    @Mappings({
            @Mapping(target = "diseaseId", ignore = true),
            @Mapping(target = "vaccines", ignore = true),
            @Mapping(target = "campaigns", ignore = true),
            @Mapping(target = "isActive", constant = "true")
    })
    Disease toEntity(DiseaseRequest request);

    @Mappings({
            @Mapping(source = "diseaseId", target = "diseaseId"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source = "isInjectedVaccination", target = "isInjectedVaccination"),
            @Mapping(source = "doseQuantity", target = "doseQuantity"),
            @Mapping(source = "isActive", target = "active")
    })
    DiseaseResponse toDto(Disease disease);
}
