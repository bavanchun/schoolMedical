package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.model.dto.PupilDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-20T18:45:27+0700",
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
        pupilDto.setParentPhoneNumber( pupil.getParentPhoneNumber() );
        pupilDto.setActive( pupil.isActive() );

        return pupilDto;
    }

    @Override
    public Pupil toEntity(PupilDto dto) {
        if ( dto == null ) {
            return null;
        }

        Pupil.PupilBuilder pupil = Pupil.builder();

        pupil.pupilId( dto.getPupilId() );
        pupil.lastName( dto.getLastName() );
        pupil.firstName( dto.getFirstName() );
        pupil.birthDate( dto.getBirthDate() );
        pupil.gender( dto.getGender() );
        pupil.parentPhoneNumber( dto.getParentPhoneNumber() );

        return pupil.build();
    }

    @Override
    public List<PupilDto> toDtoList(List<Pupil> pupils) {
        if ( pupils == null ) {
            return null;
        }

        List<PupilDto> list = new ArrayList<PupilDto>( pupils.size() );
        for ( Pupil pupil : pupils ) {
            list.add( toDto( pupil ) );
        }

        return list;
    }

    @Override
    public List<Pupil> toEntityList(List<PupilDto> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<Pupil> list = new ArrayList<Pupil>( dtos.size() );
        for ( PupilDto pupilDto : dtos ) {
            list.add( toEntity( pupilDto ) );
        }

        return list;
    }
}
