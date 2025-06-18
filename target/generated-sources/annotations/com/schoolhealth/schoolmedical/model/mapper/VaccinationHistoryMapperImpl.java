package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.VaccinationHistory;
import com.schoolhealth.schoolmedical.entity.Vaccine;
import com.schoolhealth.schoolmedical.model.dto.request.VaccinationHistoryRequest;
import com.schoolhealth.schoolmedical.model.dto.response.VaccinationHistoryResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-18T13:40:34+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Amazon.com Inc.)"
)
@Component
public class VaccinationHistoryMapperImpl implements VaccinationHistoryMapper {

    @Override
    public VaccinationHistory toEntity(VaccinationHistoryRequest request) {
        if ( request == null ) {
            return null;
        }

        VaccinationHistory.VaccinationHistoryBuilder vaccinationHistory = VaccinationHistory.builder();

        vaccinationHistory.vaccineId( request.getVaccineId() );
        vaccinationHistory.pupilId( request.getPupilId() );
        vaccinationHistory.campaignId( request.getCampaignId() );
        vaccinationHistory.source( request.getSource() );
        vaccinationHistory.vaccinatedAt( request.getVaccinatedAt() );
        vaccinationHistory.notes( request.getNotes() );

        vaccinationHistory.isActive( true );

        return vaccinationHistory.build();
    }

    @Override
    public VaccinationHistoryResponse toDto(VaccinationHistory entity) {
        if ( entity == null ) {
            return null;
        }

        VaccinationHistoryResponse.VaccinationHistoryResponseBuilder vaccinationHistoryResponse = VaccinationHistoryResponse.builder();

        vaccinationHistoryResponse.vaccineName( entityVaccineName( entity ) );
        vaccinationHistoryResponse.historyId( entity.getHistoryId() );
        vaccinationHistoryResponse.pupilId( entity.getPupilId() );
        vaccinationHistoryResponse.vaccineId( entity.getVaccineId() );
        vaccinationHistoryResponse.campaignId( entity.getCampaignId() );
        vaccinationHistoryResponse.source( entity.getSource() );
        vaccinationHistoryResponse.vaccinatedAt( entity.getVaccinatedAt() );
        vaccinationHistoryResponse.notes( entity.getNotes() );

        return vaccinationHistoryResponse.build();
    }

    private String entityVaccineName(VaccinationHistory vaccinationHistory) {
        Vaccine vaccine = vaccinationHistory.getVaccine();
        if ( vaccine == null ) {
            return null;
        }
        return vaccine.getName();
    }
}
