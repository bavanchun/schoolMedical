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
        @Mapping(target = "diseases", ignore = true),
        @Mapping(target = "campaigns", ignore = true),
        @Mapping(target = "vaccinationHistories", ignore = true),
        @Mapping(target = "consentForms", ignore = true),
        @Mapping(target = "isActive", constant = "true")
    })
    Vaccine toEntity(VaccineRequest request);

    @Mappings({
        @Mapping(source = "vaccineId", target = "vaccineId"),
        @Mapping(source = "name", target = "name"),
        @Mapping(source = "manufacturer", target = "manufacturer"),
        @Mapping(source = "recommendedAge", target = "recommendedAge"),
        @Mapping(source = "description", target = "description"),
        @Mapping(source = "doseNumber", target = "doseNumber")
    })
    VaccineResponse toDto(Vaccine vaccine);
}
