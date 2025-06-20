package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.model.dto.response.PupilRes;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PupilMapper {

    PupilMapper INSTANCE = Mappers.getMapper(PupilMapper.class);
    PupilRes toDto(Pupil pupil);

    Pupil    toEntity(PupilRes dto);

    List<PupilRes> toDtoList(List<Pupil> pupils);
    List<Pupil>    toEntityList(List<PupilRes> dtos);
}
