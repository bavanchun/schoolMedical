package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.Vaccine;
import com.schoolhealth.schoolmedical.model.dto.request.VaccineRequest;
import com.schoolhealth.schoolmedical.model.dto.response.VaccineResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-22T15:55:55+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Amazon.com Inc.)"
)
@Component
public class VaccineMapperImpl implements VaccineMapper {

    @Override
    public Vaccine toEntity(VaccineRequest request) {
        if ( request == null ) {
            return null;
        }

        Vaccine.VaccineBuilder vaccine = Vaccine.builder();

        vaccine.name( request.getName() );
        vaccine.manufacturer( request.getManufacturer() );
        vaccine.recommendedAge( request.getRecommendedAge() );
        vaccine.description( request.getDescription() );
        vaccine.doseNumber( request.getDoseNumber() );

        return vaccine.build();
    }

    @Override
    public VaccineResponse toDto(Vaccine vaccine) {
        if ( vaccine == null ) {
            return null;
        }

        VaccineResponse.VaccineResponseBuilder vaccineResponse = VaccineResponse.builder();

        vaccineResponse.vaccineId( vaccine.getVaccineId() );
        vaccineResponse.name( vaccine.getName() );
        vaccineResponse.manufacturer( vaccine.getManufacturer() );
        vaccineResponse.recommendedAge( vaccine.getRecommendedAge() );
        vaccineResponse.description( vaccine.getDescription() );
        vaccineResponse.doseNumber( vaccine.getDoseNumber() );

        return vaccineResponse.build();
    }
}
