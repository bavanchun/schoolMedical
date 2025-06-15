package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.Grade;
import com.schoolhealth.schoolmedical.model.dto.GradeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface GradeMapper {
    GradeMapper INSTANCE = Mappers.getMapper(GradeMapper.class);

    GradeDTO toDto(Grade grade);

    Grade toEntity(GradeDTO dto);
}
