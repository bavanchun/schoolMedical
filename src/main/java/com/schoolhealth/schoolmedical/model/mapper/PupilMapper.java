package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.model.dto.response.PupilRes;
import com.schoolhealth.schoolmedical.model.dto.response.PupilSimpleRes;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface PupilMapper {

    //PupilMapper INSTANCE = Mappers.getMapper(PupilMapper.class);
   // PupilRes toDto(Pupil pupil);

    Pupil toEntity(PupilRes dto);

    List<Pupil>    toEntityList(List<PupilRes> dtos);



    @Mapping(target = "gradeId", expression = "java(pupil.getPupilGrade().getFirst().getPupilGradeId().getGradeId())")
    @Mapping(target = "startYear", expression = "java(pupil.getPupilGrade().getFirst().getStartYear())")
    @Mapping(target = "gradeLevel", expression = "java(pupil.getPupilGrade().getFirst().getGrade().getGradeLevel())")
    @Mapping(target = "gradeName", expression = "java(pupil.getPupilGrade().getFirst().getGradeName())")
    @Mapping(target = "parents", source = "pupil.parents")
    @Mapping(target = "sendMedications", ignore = true)
    PupilRes toDto(Pupil pupil);


    List<PupilRes> toPupilGradeDtoList(List<Pupil> pupils);

    PupilSimpleRes toSimpleDto(Pupil pupil);


    @Named("toPupilResWithoutParent")
    @Mapping(target = "gradeId", expression = "java(pupil.getPupilGrade().getFirst().getPupilGradeId().getGradeId())")
    @Mapping(target = "startYear", expression = "java(pupil.getPupilGrade().getFirst().getStartYear())")
    @Mapping(target = "gradeLevel", expression = "java(pupil.getPupilGrade().getFirst().getGrade().getGradeLevel())")
    @Mapping(target = "gradeName", expression = "java(pupil.getPupilGrade().getFirst().getGradeName())")
    @Mapping(target = "parents", ignore = true)
    PupilRes toPupilResWithoutParent(Pupil pupil);

    @IterableMapping(qualifiedByName = "toPupilResWithoutParent")
    List<PupilRes> toPupilResWithoutParent(List<Pupil> pupils);
}
