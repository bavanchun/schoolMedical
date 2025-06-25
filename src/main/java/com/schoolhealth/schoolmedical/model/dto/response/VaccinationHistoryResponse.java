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
    private Long historyId;
    private String pupilId;
    private String pupilName;
    private String vaccineName;
    private String diseaseName;
    private String campaignName;
    private VaccinationSource source;
    private LocalDateTime vaccinatedAt;
    private String notes;
    private boolean isActive;
}
