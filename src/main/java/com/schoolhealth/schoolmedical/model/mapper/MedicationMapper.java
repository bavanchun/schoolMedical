package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.Medication;
import com.schoolhealth.schoolmedical.model.dto.request.MedicationRequest;
import com.schoolhealth.schoolmedical.model.dto.response.MedicationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MedicationMapper {

    @Mapping(target = "medicationId", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "medicalEvents", ignore = true)
    Medication toEntity(MedicationRequest request);

    MedicationResponse toResponse(Medication medication);

    List<MedicationResponse> toResponseList(List<Medication> medications);

    @Mapping(target = "medicationId", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "medicalEvents", ignore = true)
    void updateEntityFromRequest(MedicationRequest request, @MappingTarget Medication medication);
}
