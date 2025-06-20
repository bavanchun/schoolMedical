package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.VaccinationHistory;
import com.schoolhealth.schoolmedical.model.dto.request.VaccinationHistoryRequest;
import com.schoolhealth.schoolmedical.model.dto.response.VaccinationHistoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface VaccinationHistoryMapper {
    @Mappings({
            @Mapping(target = "historyId", ignore = true),
            @Mapping(target = "isActive", constant = "true"),
            @Mapping(target = "vaccine", ignore = true),
            @Mapping(target = "pupil", ignore = true)
    })
    VaccinationHistory toEntity(VaccinationHistoryRequest request);

    @Mappings({
            @Mapping(target = "vaccineName", source = "vaccine.name")
    })
    VaccinationHistoryResponse toDto(VaccinationHistory entity);
}
