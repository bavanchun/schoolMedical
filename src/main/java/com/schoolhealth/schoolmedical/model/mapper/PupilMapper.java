package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.model.dto.response.PupilRes;
import com.schoolhealth.schoolmedical.model.dto.response.PupilSimpleRes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PupilMapper {

    //PupilMapper INSTANCE = Mappers.getMapper(PupilMapper.class);
   // PupilRes toDto(Pupil pupil);

    Pupil toEntity(PupilRes dto);

    List<Pupil>    toEntityList(List<PupilRes> dtos);



    @Mapping(target = "gradeId", expression = "java(pupil.getPupilGrade().getFirst().getPupilGradeId().getGradeId())")
    @Mapping(target = "startYear", expression = "java(pupil.getPupilGrade().getFirst().getStartYear())")
    @Mapping(target = "gradeLevel", expression = "java(pupil.getPupilGrade().getFirst().getGrade().getGradeLevel())")
    @Mapping(target = "gradeName", expression = "java(pupil.getPupilGrade().getFirst().getGradeName())")
    PupilRes toDto(Pupil pupil);

    List<PupilRes> toPupilGradeDtoList(List<Pupil> pupils);

    PupilSimpleRes toSimpleDto(Pupil pupil);

}
