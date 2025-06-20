package com.schoolhealth.schoolmedical.model.dto.response;

import java.time.LocalDateTime;
import com.schoolhealth.schoolmedical.entity.enums.VaccinationSource;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VaccinationHistoryResponse {
    private int historyId;
    private String pupilId;
    private int vaccineId;
    private String vaccineName;
    private int diseaseId;
    private String diseaseName;
    private int campaignId;
    private VaccinationSource source;
    private LocalDateTime vaccinatedAt;
    private String notes;
    private boolean isActive;
}
