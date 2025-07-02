package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.MedicalEvent;
import com.schoolhealth.schoolmedical.model.dto.request.CreateMedicalEventRequest;

import com.schoolhealth.schoolmedical.model.dto.response.MedicalEventResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PupilMapper.class, EquipmentMapper.class, MedicationMapper.class})
public interface MedicalEventMapper {

    @Mapping(target = "medicalEventId", ignore = true)
    @Mapping(target = "dateTime", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "pupil", ignore = true)
    @Mapping(target = "schoolNurse", ignore = true)
    @Mapping(target = "equipmentUsed", ignore = true)
    @Mapping(target = "medicationUsed", ignore = true)
    MedicalEvent toEntity(CreateMedicalEventRequest request);

    @Mapping(target = "pupil", source = "pupil")
    @Mapping(target = "schoolNurse", source = "schoolNurse")
    @Mapping(target = "equipmentUsed", source = "equipmentUsed")
    @Mapping(target = "medicationUsed", source = "medicationUsed")
    MedicalEventResponse toResponse(MedicalEvent medicalEvent);

    List<MedicalEventResponse> toResponseList(List<MedicalEvent> medicalEvents);



    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    MedicalEventResponse.SchoolNurseInfo toSchoolNurseInfo(com.schoolhealth.schoolmedical.entity.User user);
}
