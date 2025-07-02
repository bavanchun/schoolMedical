package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.MedicationItem;
import com.schoolhealth.schoolmedical.entity.MedicationLogs;
import com.schoolhealth.schoolmedical.entity.SendMedication;
import com.schoolhealth.schoolmedical.model.dto.request.MedicationItemReq;
import com.schoolhealth.schoolmedical.model.dto.request.SendMedicationReq;
import com.schoolhealth.schoolmedical.model.dto.response.MedicationItemRes;
import com.schoolhealth.schoolmedical.model.dto.response.MedicationLogsRes;
import com.schoolhealth.schoolmedical.model.dto.response.SendMedicationRes;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SendMedicationMapper {

    @Mapping(target = "sendMedicationId", ignore = true)
    @Mapping(target = "requestedDate", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "status", constant = "PENDING")
    SendMedication toEntity(SendMedicationReq sendMedication);

    @Mapping(target = "medicationId", ignore = true)
    @Mapping(target = "sendMedication", ignore = true)
    MedicationItem toEntity(MedicationItemReq medicationItem);
    List<MedicationItem> toEntity(List<MedicationItemReq> medicationItems);

    @Mapping(target = "pupilId", source = "pupil.pupilId")
    @Mapping(target = "medicationItems", source = "medicationItems")
    SendMedicationRes toDto(SendMedication sendMedication);

    MedicationItemRes toDto(MedicationItem medicationItem);
    List<MedicationItemRes> toMedicationItemDto(List<MedicationItem> medicationItems);

    MedicationLogsRes toMedicationLogsDto(MedicationLogs sendMedication);
    List<MedicationLogsRes> toMedicationLogsDto(List<MedicationLogs> sendMedications);

    @Named("toDtoWithMedicationLog")
    @Mapping(target = "pupilId", source = "pupil.pupilId")
    @Mapping(target = "medicationLogs", source = "medicationLogs")
    @Mapping(target = "medicationItems", source = "medicationItems")
    SendMedicationRes toDtoSendMedication(SendMedication sendMedication);

    @IterableMapping(qualifiedByName = "toDtoWithMedicationLog")
    List<SendMedicationRes> toDtoSendMedication(List<SendMedication> sendMeditions);

}
