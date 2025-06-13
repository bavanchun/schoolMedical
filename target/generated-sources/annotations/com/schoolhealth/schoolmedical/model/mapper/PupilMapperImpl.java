package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.model.dto.PupilDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-13T18:47:25+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Amazon.com Inc.)"
)
@Component
public class PupilMapperImpl implements PupilMapper {

    @Override
    public PupilDto toDto(Pupil pupil) {
        if ( pupil == null ) {
            return null;
        }

        PupilDto pupilDto = new PupilDto();

        pupilDto.setPupilId( pupil.getPupilId() );
        pupilDto.setLastName( pupil.getLastName() );
        pupilDto.setFirstName( pupil.getFirstName() );
        pupilDto.setBirthDate( pupil.getBirthDate() );
        pupilDto.setGender( pupil.getGender() );
        pupilDto.setActive( pupil.isActive() );

        return pupilDto;
    }
}
