package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.model.dto.PupilDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PupilMapper {
    PupilDto toDto(Pupil pupil);
}
