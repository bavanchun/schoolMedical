package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.model.dto.response.PupilRes;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-20T13:46:26+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Amazon.com Inc.)"
)
@Component
public class PupilMapperImpl implements PupilMapper {

    @Override
    public PupilRes toDto(Pupil pupil) {
        if ( pupil == null ) {
            return null;
        }

        PupilRes pupilRes = new PupilRes();

        pupilRes.setPupilId( pupil.getPupilId() );
        pupilRes.setLastName( pupil.getLastName() );
        pupilRes.setFirstName( pupil.getFirstName() );
        pupilRes.setBirthDate( pupil.getBirthDate() );
        pupilRes.setGender( pupil.getGender() );

        return pupilRes;
    }

    @Override
    public Pupil toEntity(PupilRes dto) {
        if ( dto == null ) {
            return null;
        }

        Pupil.PupilBuilder pupil = Pupil.builder();

        pupil.pupilId( dto.getPupilId() );
        pupil.lastName( dto.getLastName() );
        pupil.firstName( dto.getFirstName() );
        pupil.birthDate( dto.getBirthDate() );
        pupil.gender( dto.getGender() );

        return pupil.build();
    }

    @Override
    public List<PupilRes> toDtoList(List<Pupil> pupils) {
        if ( pupils == null ) {
            return null;
        }

        List<PupilRes> list = new ArrayList<PupilRes>( pupils.size() );
        for ( Pupil pupil : pupils ) {
            list.add( toDto( pupil ) );
        }

        return list;
    }

    @Override
    public List<Pupil> toEntityList(List<PupilRes> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<Pupil> list = new ArrayList<Pupil>( dtos.size() );
        for ( PupilRes pupilRes : dtos ) {
            list.add( toEntity( pupilRes ) );
        }

        return list;
    }
}
