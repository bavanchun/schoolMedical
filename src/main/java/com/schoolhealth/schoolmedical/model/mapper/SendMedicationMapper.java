package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.MedicationItem;
import com.schoolhealth.schoolmedical.entity.MedicationLogs;
import com.schoolhealth.schoolmedical.entity.SendMedication;
import com.schoolhealth.schoolmedical.model.dto.request.MedicationItemReq;
import com.schoolhealth.schoolmedical.model.dto.request.MedicationLogReq;
import com.schoolhealth.schoolmedical.model.dto.request.SendMedicationReq;
import com.schoolhealth.schoolmedical.model.dto.response.MedicationItemRes;
import com.schoolhealth.schoolmedical.model.dto.response.MedicationLogNotifcationRes;
import com.schoolhealth.schoolmedical.model.dto.response.MedicationLogsRes;
import com.schoolhealth.schoolmedical.model.dto.response.SendMedicationRes;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SendMedicationMapper {
    //TO ENTITY
    @Mapping(target = "sendMedicationId", ignore = true)
    @Mapping(target = "requestedDate", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "status", constant = "PENDING")
    SendMedication toEntity(SendMedicationReq sendMedication);

    @Mapping(target = "medicationId", ignore = true)
    @Mapping(target = "sendMedication", ignore = true)
    MedicationItem toEntity(MedicationItemReq medicationItem);
    List<MedicationItem> toEntity(List<MedicationItemReq> medicationItems);

    MedicationLogs toMedicationLogsEntity(MedicationLogReq  medicationLogReq);

    //TO DTO

    @Mapping(target = "pupilId", source = "pupil.pupilId")
    @Mapping(target = "medicationItems", source = "medicationItems")
    SendMedicationRes toDto(SendMedication sendMedication);

    MedicationItemRes toMedicationItemDto(MedicationItem medicationItem);
    List<MedicationItemRes> toMedicationItemDtoList(List<MedicationItem> medicationItems);

    MedicationLogsRes toMedicationLogsDto(MedicationLogs medicationLog);
    List<MedicationLogsRes> toMedicationLogsDto(List<MedicationLogs> medicationLogs);

    @Named("toDtoSendMedication")
    @Mapping(target = "pupilId", source = "pupil.pupilId")
    @Mapping(target = "medicationLogs", source = "medicationLogs")
    @Mapping(target = "medicationItems", source = "medicationItems")
    SendMedicationRes toDtoSendMedication(SendMedication sendMedication);

    @IterableMapping(qualifiedByName = "toDtoSendMedication")
    List<SendMedicationRes> toDtoSendMedication(List<SendMedication> sendMeditions);

    @Named("toDtoWithAfterBreakfast")
    @Mapping(target = "pupilId", source = "pupil.pupilId")
    @Mapping(target = "medicationLogs", source = "medicationLogs")
    @Mapping(target = "medicationItems", expression = "java(sendMedication.getMedicationItems().stream().filter(item -> \"After breakfast: 9h00-9h30\".equals(item.getMedicationSchedule())).map(this::toMedicationItemDto).toList())")
    SendMedicationRes toDtoWithAfterBreakfast(SendMedication sendMedication);
    @IterableMapping(qualifiedByName = "toDtoWithAfterBreakfast")
    List<SendMedicationRes> toDtoWithAfterBreakfast(List<SendMedication> sendMedications);

    @Named("toDtoWithBeforeLunch")
    @Mapping(target = "pupilId", source = "pupil.pupilId")
    @Mapping(target = "medicationLogs", source = "medicationLogs")
    @Mapping(target = "medicationItems", expression = "java(sendMedication.getMedicationItems().stream().filter(item -> \"Before lunch: 10h30-11h00\".equals(item.getMedicationSchedule())).map(this::toMedicationItemDto).toList())")
    SendMedicationRes toDtoWithBeforeLunch(SendMedication sendMedication);
    @IterableMapping(qualifiedByName = "toDtoWithBeforeLunch")
    List<SendMedicationRes> toDtoWithBeforeLunch(List<SendMedication> sendMedications);

    @Named("toDtoWithAfterLunch")
    @Mapping(target = "pupilId", source = "pupil.pupilId")
    @Mapping(target = "medicationLogs", source = "medicationLogs")
    @Mapping(target = "medicationItems", expression = "java(sendMedication.getMedicationItems().stream().filter(item -> \"After lunch: 11h30-12h00\".equals(item.getMedicationSchedule())).map(this::toMedicationItemDto).toList())")
    SendMedicationRes toDtoWithAfterLunch(SendMedication sendMedication);
    @IterableMapping(qualifiedByName = "toDtoWithAfterLunch")
    List<SendMedicationRes> toDtoWithAfterLunch(List<SendMedication> sendMedications);

}
