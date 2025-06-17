package com.schoolhealth.schoolmedical.model.dto.request;

import java.time.LocalDateTime;
import com.schoolhealth.schoolmedical.entity.enums.VaccinationSource;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VaccinationHistoryRequest {
    private String pupilId;
    private int vaccineId;
    private int diseaseId;
    private int campaignId;
    private VaccinationSource source;
    private LocalDateTime vaccinatedAt;
    private String notes;
}
