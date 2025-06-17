package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.DiseaseVaccine;
import com.schoolhealth.schoolmedical.model.dto.request.DiseaseVaccineRequest;
import com.schoolhealth.schoolmedical.model.dto.response.DiseaseVaccineResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-17T19:06:13+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Amazon.com Inc.)"
)
@Component
public class DiseaseVaccineMapperImpl implements DiseaseVaccineMapper {

    @Override
    public DiseaseVaccine toEntity(DiseaseVaccineRequest request) {
        if ( request == null ) {
            return null;
        }

        DiseaseVaccine.DiseaseVaccineBuilder diseaseVaccine = DiseaseVaccine.builder();

        diseaseVaccine.diseaseId( request.getDiseaseId() );
        diseaseVaccine.vaccineId( request.getVaccineId() );

        diseaseVaccine.isActive( true );

        return diseaseVaccine.build();
    }

    @Override
    public DiseaseVaccineResponse toDto(DiseaseVaccine entity) {
        if ( entity == null ) {
            return null;
        }

        DiseaseVaccineResponse.DiseaseVaccineResponseBuilder diseaseVaccineResponse = DiseaseVaccineResponse.builder();

        diseaseVaccineResponse.diseaseId( entity.getDiseaseId() );
        diseaseVaccineResponse.vaccineId( entity.getVaccineId() );

        return diseaseVaccineResponse.build();
    }
}
