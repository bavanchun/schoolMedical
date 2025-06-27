package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.model.dto.response.PupilRes;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-27T18:55:00+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.5 (Oracle Corporation)"
)
@Component
public class PupilMapperImpl implements PupilMapper {

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
        pupil.avatar( dto.getAvatar() );

        return pupil.build();
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

    @Override
    public PupilRes toDto(Pupil pupil) {
        if ( pupil == null ) {
            return null;
        }

        PupilRes.PupilResBuilder pupilRes = PupilRes.builder();

        pupilRes.pupilId( pupil.getPupilId() );
        pupilRes.lastName( pupil.getLastName() );
        pupilRes.firstName( pupil.getFirstName() );
        pupilRes.birthDate( pupil.getBirthDate() );
        pupilRes.gender( pupil.getGender() );
        pupilRes.avatar( pupil.getAvatar() );

        pupilRes.gradeId( pupil.getPupilGrade().getFirst().getPupilGradeId().getGradeId() );
        pupilRes.startYear( pupil.getPupilGrade().getFirst().getStartYear() );
        pupilRes.gradeLevel( pupil.getPupilGrade().getFirst().getGrade().getGradeLevel() );
        pupilRes.gradeName( pupil.getPupilGrade().getFirst().getGradeName() );

        return pupilRes.build();
    }

    @Override
    public List<PupilRes> toPupilGradeDtoList(List<Pupil> pupils) {
        if ( pupils == null ) {
            return null;
        }

        List<PupilRes> list = new ArrayList<PupilRes>( pupils.size() );
        for ( Pupil pupil : pupils ) {
            list.add( toDto( pupil ) );
        }

        return list;
    }
}
