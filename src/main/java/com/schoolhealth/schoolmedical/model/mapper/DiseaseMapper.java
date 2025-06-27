package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.Disease;
import com.schoolhealth.schoolmedical.entity.HealthCheckDisease;
import com.schoolhealth.schoolmedical.entity.Vaccine;
import com.schoolhealth.schoolmedical.model.dto.request.DiseaseRequest;
import com.schoolhealth.schoolmedical.model.dto.response.DiseaseHealthCheckRes;
import com.schoolhealth.schoolmedical.model.dto.response.DiseaseResponse;
import com.schoolhealth.schoolmedical.model.dto.response.DiseaseVaccineResponse;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckDiseaseRes;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DiseaseMapper {

    @Mappings({
            @Mapping(target = "diseaseName", source="disease.name")
            , @Mapping(target = "note", source="note")
    })
    HealthCheckDiseaseRes toHealthCheckDiseaseDto(HealthCheckDisease healthCheckDisease);
    List<HealthCheckDiseaseRes> toHealthCheckDiseaseDtoList(List<HealthCheckDisease> healthCheckDiseases);
    @Mappings({
            @Mapping(target = "diseaseId", ignore = true),
            @Mapping(target = "vaccines", ignore = true),
            @Mapping(target = "campaigns", ignore = true),
            @Mapping(target = "isActive", constant = "true")
    })
    Disease toEntity(DiseaseRequest request);

    DiseaseHealthCheckRes toDtoWithoutVaccines(Disease disease);
    @Mappings({
            @Mapping(source = "diseaseId", target = "diseaseId"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source = "isInjectedVaccination", target = "isInjectedVaccination"),
            @Mapping(source = "doseQuantity", target = "doseQuantity"),
    })
    DiseaseResponse toDto(Disease disease);

    /**
     * Creates a DiseaseVaccineResponse object with information from both Disease and Vaccine entities
     *
     * @param disease The disease entity
     * @param vaccine The vaccine entity
     * @param success Whether the operation was successful
     * @param message A message describing the result
     * @return A DiseaseVaccineResponse with the combined information
     */
    @Mappings({
        @Mapping(source = "disease.diseaseId", target = "diseaseId"),
        @Mapping(source = "disease.name", target = "diseaseName"),
        @Mapping(source = "vaccine.vaccineId", target = "vaccineId"),
        @Mapping(source = "vaccine.name", target = "vaccineName"),
        @Mapping(source = "success", target = "success"),
        @Mapping(source = "message", target = "message")
    })
    DiseaseVaccineResponse toDiseaseVaccineResponse(Disease disease, Vaccine vaccine, boolean success, String message);

    /**
     * Creates an error response when either disease or vaccine is not found
     */
    @AfterMapping
    default void handleNullEntities(Disease disease, Vaccine vaccine, boolean success,
                                   String message, @MappingTarget DiseaseVaccineResponse.DiseaseVaccineResponseBuilder builder) {
        if (disease == null || vaccine == null) {
            builder.success(false)
                   .message("Disease or vaccine not found")
                   .diseaseId(disease != null ? disease.getDiseaseId() : null)
                   .diseaseName(disease != null ? disease.getName() : null)
                   .vaccineId(vaccine != null ? vaccine.getVaccineId() : null)
                   .vaccineName(vaccine != null ? vaccine.getName() : null);
        }
    }
}
