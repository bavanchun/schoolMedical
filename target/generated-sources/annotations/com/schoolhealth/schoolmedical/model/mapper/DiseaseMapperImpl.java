package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.Disease;
import com.schoolhealth.schoolmedical.entity.Vaccine;
import com.schoolhealth.schoolmedical.model.dto.request.DiseaseRequest;
import com.schoolhealth.schoolmedical.model.dto.response.DiseaseResponse;
import com.schoolhealth.schoolmedical.model.dto.response.DiseaseVaccineResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-23T02:46:22+0700",
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

        disease.name( request.getName() );
        disease.description( request.getDescription() );
        disease.isInjectedVaccination( request.getIsInjectedVaccination() );
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

        diseaseResponse.diseaseId( disease.getDiseaseId() );
        diseaseResponse.name( disease.getName() );
        diseaseResponse.description( disease.getDescription() );
        diseaseResponse.isInjectedVaccination( disease.getIsInjectedVaccination() );
        diseaseResponse.doseQuantity( disease.getDoseQuantity() );

        return diseaseResponse.build();
    }

    @Override
    public DiseaseVaccineResponse toDiseaseVaccineResponse(Disease disease, Vaccine vaccine, boolean success, String message) {
        if ( disease == null && vaccine == null && message == null ) {
            return null;
        }

        DiseaseVaccineResponse.DiseaseVaccineResponseBuilder diseaseVaccineResponse = DiseaseVaccineResponse.builder();

        if ( disease != null ) {
            diseaseVaccineResponse.diseaseId( disease.getDiseaseId() );
            diseaseVaccineResponse.diseaseName( disease.getName() );
        }
        if ( vaccine != null ) {
            diseaseVaccineResponse.vaccineId( vaccine.getVaccineId() );
            diseaseVaccineResponse.vaccineName( vaccine.getName() );
        }
        diseaseVaccineResponse.success( success );
        diseaseVaccineResponse.message( message );

        handleNullEntities( disease, vaccine, success, message, diseaseVaccineResponse );

        return diseaseVaccineResponse.build();
    }
}
