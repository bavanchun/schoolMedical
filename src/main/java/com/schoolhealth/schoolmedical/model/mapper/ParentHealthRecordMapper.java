package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.HealthConditionHistory;
import com.schoolhealth.schoolmedical.model.dto.request.ParentHealthRecordRequest;
import com.schoolhealth.schoolmedical.model.dto.response.ParentHealthRecordResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * MapStruct mapper for converting between HealthConditionHistory entity and ParentHealthRecord DTOs.
 */
@Mapper(componentModel = "spring")
public interface ParentHealthRecordMapper {

    @Mapping(target = "conditionId", ignore = true)
    @Mapping(target = "pupil", ignore = true)
    HealthConditionHistory toEntity(ParentHealthRecordRequest request);

    @Mapping(source = "pupil.pupilId", target = "pupilId")
    ParentHealthRecordResponse toDto(HealthConditionHistory entity);

    List<ParentHealthRecordResponse> toDtoList(List<HealthConditionHistory> entities);
}
