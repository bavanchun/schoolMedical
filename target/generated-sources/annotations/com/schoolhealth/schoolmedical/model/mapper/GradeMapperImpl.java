package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.Grade;
import com.schoolhealth.schoolmedical.model.dto.GradeDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-16T09:53:08+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Amazon.com Inc.)"
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
        gradeDTO.gradeName( grade.getGradeName() );
        gradeDTO.startYear( grade.getStartYear() );
        gradeDTO.endYear( grade.getEndYear() );
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
        grade.gradeName( dto.getGradeName() );
        grade.startYear( dto.getStartYear() );
        grade.endYear( dto.getEndYear() );
        grade.gradeLevel( dto.getGradeLevel() );

        return grade.build();
    }
}
