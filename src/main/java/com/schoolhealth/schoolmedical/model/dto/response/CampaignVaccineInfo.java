package com.schoolhealth.schoolmedical.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampaignVaccineInfo {
    private Long vaccineId;
    private String name;
    private String manufacturer;
    private String recommendedAge;
    private String description;
}
