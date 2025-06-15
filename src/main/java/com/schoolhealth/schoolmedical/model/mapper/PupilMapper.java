package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.model.dto.PupilDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PupilMapper {

    PupilMapper INSTANCE = Mappers.getMapper(PupilMapper.class);
    PupilDto toDto(Pupil pupil);

    Pupil    toEntity(PupilDto dto);

    List<PupilDto> toDtoList(List<Pupil> pupils);
    List<Pupil>    toEntityList(List<PupilDto> dtos);
}
