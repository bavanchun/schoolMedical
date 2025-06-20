package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.Disease;
import com.schoolhealth.schoolmedical.entity.VaccinationCampagin;
import com.schoolhealth.schoolmedical.entity.Vaccine;
import com.schoolhealth.schoolmedical.model.dto.request.VaccinationCampaignRequest;
import com.schoolhealth.schoolmedical.model.dto.response.VaccinationCampaignResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-20T00:12:34+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Amazon.com Inc.)"
)
@Component
public class VaccinationCampaignMapperImpl implements VaccinationCampaignMapper {

    @Override
    public VaccinationCampagin toEntity(VaccinationCampaignRequest request) {
        if ( request == null ) {
            return null;
        }

        VaccinationCampagin.VaccinationCampaginBuilder vaccinationCampagin = VaccinationCampagin.builder();

        vaccinationCampagin.notes( request.getNotes() );
        vaccinationCampagin.titleCampaign( request.getTitleCampaign() );
        if ( request.getVaccineId() != null ) {
            vaccinationCampagin.vaccineId( request.getVaccineId() );
        }
        if ( request.getDiseaseId() != null ) {
            vaccinationCampagin.diseaseId( request.getDiseaseId() );
        }
        if ( request.getDoseNumber() != null ) {
            vaccinationCampagin.doseNumber( request.getDoseNumber() );
        }
        vaccinationCampagin.startDate( request.getStartDate() );
        vaccinationCampagin.endDate( request.getEndDate() );
        vaccinationCampagin.formDeadline( request.getFormDeadline() );

        return vaccinationCampagin.build();
    }

    @Override
    public VaccinationCampaignResponse toDto(VaccinationCampagin campaign) {
        if ( campaign == null ) {
            return null;
        }

        VaccinationCampaignResponse.VaccinationCampaignResponseBuilder vaccinationCampaignResponse = VaccinationCampaignResponse.builder();

        vaccinationCampaignResponse.campaignId( campaign.getCampaignId() );
        vaccinationCampaignResponse.diseaseName( campaignDiseaseName( campaign ) );
        vaccinationCampaignResponse.vaccineName( campaignVaccineName( campaign ) );
        vaccinationCampaignResponse.notes( campaign.getNotes() );
        vaccinationCampaignResponse.titleCampaign( campaign.getTitleCampaign() );
        vaccinationCampaignResponse.doseNumber( campaign.getDoseNumber() );
        vaccinationCampaignResponse.startDate( campaign.getStartDate() );
        vaccinationCampaignResponse.endDate( campaign.getEndDate() );
        vaccinationCampaignResponse.formDeadline( campaign.getFormDeadline() );
        if ( campaign.getStatus() != null ) {
            vaccinationCampaignResponse.status( campaign.getStatus().name() );
        }

        return vaccinationCampaignResponse.build();
    }

    private String campaignDiseaseName(VaccinationCampagin vaccinationCampagin) {
        Disease disease = vaccinationCampagin.getDisease();
        if ( disease == null ) {
            return null;
        }
        return disease.getName();
    }

    private String campaignVaccineName(VaccinationCampagin vaccinationCampagin) {
        Vaccine vaccine = vaccinationCampagin.getVaccine();
        if ( vaccine == null ) {
            return null;
        }
        return vaccine.getName();
    }
}
