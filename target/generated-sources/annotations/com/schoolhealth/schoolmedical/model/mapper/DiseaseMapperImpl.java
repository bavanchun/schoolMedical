package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.Disease;
import com.schoolhealth.schoolmedical.model.dto.request.DiseaseRequest;
import com.schoolhealth.schoolmedical.model.dto.response.DiseaseResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-20T18:50:32+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Amazon.com Inc.)"
)
@Component
public class DiseaseMapperImpl implements DiseaseMapper {

    @Override
    public Disease toEntity(DiseaseRequest request) {
        if ( request == null ) {
            return null;
        }

        Disease.DiseaseBuilder disease = Disease.builder();

        disease.isInjectedVaccination( request.isInjectedVaccination() );
        disease.name( request.getName() );
        disease.description( request.getDescription() );
        disease.doseQuantity( request.getDoseQuantity() );

        disease.isActive( true );

        return disease.build();
    }

    @Override
    public DiseaseResponse toDto(Disease disease) {
        if ( disease == null ) {
            return null;
        }

        DiseaseResponse.DiseaseResponseBuilder diseaseResponse = DiseaseResponse.builder();

        if ( disease.getDiseaseId() != null ) {
            diseaseResponse.diseaseId( disease.getDiseaseId().intValue() );
        }
        diseaseResponse.name( disease.getName() );
        diseaseResponse.description( disease.getDescription() );
        if ( disease.getIsInjectedVaccination() != null ) {
            diseaseResponse.isInjectedVaccination( disease.getIsInjectedVaccination() );
        }
        diseaseResponse.doseQuantity( disease.getDoseQuantity() );

        return diseaseResponse.build();
    }
}
