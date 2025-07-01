package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.Equipment;
import com.schoolhealth.schoolmedical.model.dto.request.EquipmentRequest;
import com.schoolhealth.schoolmedical.model.dto.response.EquipmentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EquipmentMapper {

    @Mapping(target = "equipmentId", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "medicalEvents", ignore = true)
    Equipment toEntity(EquipmentRequest request);

    EquipmentResponse toResponse(Equipment equipment);

    List<EquipmentResponse> toResponseList(List<Equipment> equipments);

    @Mapping(target = "equipmentId", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "medicalEvents", ignore = true)
    void updateEntityFromRequest(EquipmentRequest request, @MappingTarget Equipment equipment);
}
