package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.Vaccine;
import com.schoolhealth.schoolmedical.model.dto.request.VaccineRequest;
import com.schoolhealth.schoolmedical.model.dto.response.VaccineResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface VaccineMapper {
    @Mappings({
            @Mapping(target = "vaccineId", ignore = true),
            @Mapping(target = "isActive", constant = "true"),
            @Mapping(target = "campaigns", ignore = true),
            @Mapping(target = "vaccinationHistories", ignore = true),
            @Mapping(target = "consentForms", ignore = true),
            @Mapping(target = "diseaseVaccines", ignore = true)
    })
    Vaccine toEntity(VaccineRequest request);

    VaccineResponse toDto(Vaccine vaccine);
}
