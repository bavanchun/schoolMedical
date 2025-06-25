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
    date = "2025-06-25T02:51:40+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Amazon.com Inc.)"
)
@Component
public class VaccinationCampaignMapperImpl implements VaccinationCampaignMapper {

    @Override
    public VaccinationCampagin toVaccinationCampaign(VaccinationCampaignRequest request) {
        if ( request == null ) {
            return null;
        }

        VaccinationCampagin.VaccinationCampaginBuilder vaccinationCampagin = VaccinationCampagin.builder();

        vaccinationCampagin.titleCampaign( request.getTitleCampaign() );
        vaccinationCampagin.startDate( request.getStartDate() );
        vaccinationCampagin.endDate( request.getEndDate() );
        vaccinationCampagin.formDeadline( request.getFormDeadline() );
        vaccinationCampagin.notes( request.getNotes() );

        return vaccinationCampagin.build();
    }

    @Override
    public void updateCampaignFromRequest(VaccinationCampaignRequest request, VaccinationCampagin campaign) {
        if ( request == null ) {
            return;
        }

        campaign.setTitleCampaign( request.getTitleCampaign() );
        campaign.setStartDate( request.getStartDate() );
        campaign.setEndDate( request.getEndDate() );
        campaign.setFormDeadline( request.getFormDeadline() );
        campaign.setNotes( request.getNotes() );
    }

    @Override
    public VaccinationCampaignResponse toVaccinationCampaignResponse(VaccinationCampagin campaign) {
        if ( campaign == null ) {
            return null;
        }

        VaccinationCampaignResponse.VaccinationCampaignResponseBuilder vaccinationCampaignResponse = VaccinationCampaignResponse.builder();

        vaccinationCampaignResponse.vaccineName( campaignVaccineName( campaign ) );
        vaccinationCampaignResponse.diseaseName( campaignDiseaseName( campaign ) );
        if ( campaign.getStatus() != null ) {
            vaccinationCampaignResponse.status( campaign.getStatus().name() );
        }
        if ( campaign.getCampaignId() != null ) {
            vaccinationCampaignResponse.campaignId( campaign.getCampaignId().intValue() );
        }
        vaccinationCampaignResponse.titleCampaign( campaign.getTitleCampaign() );
        vaccinationCampaignResponse.startDate( campaign.getStartDate() );
        vaccinationCampaignResponse.endDate( campaign.getEndDate() );
        vaccinationCampaignResponse.formDeadline( campaign.getFormDeadline() );
        vaccinationCampaignResponse.notes( campaign.getNotes() );

        return vaccinationCampaignResponse.build();
    }

    private String campaignVaccineName(VaccinationCampagin vaccinationCampagin) {
        Vaccine vaccine = vaccinationCampagin.getVaccine();
        if ( vaccine == null ) {
            return null;
        }
        return vaccine.getName();
    }

    private String campaignDiseaseName(VaccinationCampagin vaccinationCampagin) {
        Disease disease = vaccinationCampagin.getDisease();
        if ( disease == null ) {
            return null;
        }
        return disease.getName();
    }
}
