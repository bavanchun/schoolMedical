package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.SendMedication;
import com.schoolhealth.schoolmedical.model.dto.request.SendMedicationReq;
import com.schoolhealth.schoolmedical.model.dto.response.SendMedicationRes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SendMedicationMapper {

    @Mapping(target = "sendMedicationId", ignore = true)
    @Mapping(target = "requestedDate", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "status", constant = "PENDING")
    SendMedication toEntity(SendMedicationReq sendMedication);

    @Mapping(target = "pupilId", source = "pupil.pupilId")
    SendMedicationRes toDto(SendMedication sendMedication);
}
