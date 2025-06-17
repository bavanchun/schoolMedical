package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.DiseaseVaccine;
import com.schoolhealth.schoolmedical.model.dto.request.DiseaseVaccineRequest;
import com.schoolhealth.schoolmedical.model.dto.response.DiseaseVaccineResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface DiseaseVaccineMapper {

    @Mappings({
            @Mapping(target = "isActive", constant = "true"),
            @Mapping(target = "disease", ignore = true),
            @Mapping(target = "vaccine", ignore = true)
    })
    DiseaseVaccine toEntity(DiseaseVaccineRequest request);

    DiseaseVaccineResponse toDto(DiseaseVaccine entity);
}
