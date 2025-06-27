package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.Grade;
import com.schoolhealth.schoolmedical.model.dto.GradeDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-27T22:18:28+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.1 (Oracle Corporation)"
)
@Component
public class GradeMapperImpl implements GradeMapper {

    @Override
    public GradeDTO toDto(Grade grade) {
        if ( grade == null ) {
            return null;
        }

        GradeDTO.GradeDTOBuilder gradeDTO = GradeDTO.builder();

        gradeDTO.gradeId( grade.getGradeId() );
        gradeDTO.gradeLevel( grade.getGradeLevel() );

        return gradeDTO.build();
    }

    @Override
    public Grade toEntity(GradeDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Grade.GradeBuilder grade = Grade.builder();

        grade.gradeId( dto.getGradeId() );
        grade.gradeLevel( dto.getGradeLevel() );

        return grade.build();
    }
}
