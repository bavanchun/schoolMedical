package com.schoolhealth.schoolmedical.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.schoolhealth.schoolmedical.entity.enums.MedicalEventStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MedicalEventResponse {

    private Long medicalEventId;
    private String injuryDescription;
    private String treatmentDescription;
    private String detailedInformation;
    private MedicalEventStatus status;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dateTime;

    private Boolean isActive;

    // Pupil information
    private PupilRes pupil;

    // School nurse information
    private SchoolNurseInfo schoolNurse;

    // Equipment used in treatment
    private List<EquipmentResponse> equipmentUsed;

    // Medication used in treatment
    private List<MedicationResponse> medicationUsed;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SchoolNurseInfo {
        private String userId;
        private String firstName;
        private String lastName;
        private String phoneNumber;
    }
}
