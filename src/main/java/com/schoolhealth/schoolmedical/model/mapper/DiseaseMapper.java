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
            @Mapping(target = "isActive", constant = "true"),
            @Mapping(target = "campaigns", ignore = true),
            @Mapping(target = "diseaseVaccines", ignore = true),
            @Mapping(target = "isInjectedVaccination", source = "injectedVaccination"),
    })
    Disease toEntity(DiseaseRequest request);

    DiseaseResponse toDto(Disease disease);
}
