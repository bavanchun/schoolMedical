package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.model.dto.PupilDto;
<<<<<<< HEAD
=======
import java.util.ArrayList;
import java.util.List;
>>>>>>> 01e5dd6f011cadc9f05affa3a6420a6204ffd2be
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
<<<<<<< HEAD
    date = "2025-06-14T16:32:18+0700",
=======
    date = "2025-06-14T04:47:13+0700",
>>>>>>> 01e5dd6f011cadc9f05affa3a6420a6204ffd2be
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
<<<<<<< HEAD
=======
        pupilDto.setParentPhoneNumber( pupil.getParentPhoneNumber() );
>>>>>>> 01e5dd6f011cadc9f05affa3a6420a6204ffd2be
        pupilDto.setActive( pupil.isActive() );

        return pupilDto;
    }
<<<<<<< HEAD
=======

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
>>>>>>> 01e5dd6f011cadc9f05affa3a6420a6204ffd2be
}
