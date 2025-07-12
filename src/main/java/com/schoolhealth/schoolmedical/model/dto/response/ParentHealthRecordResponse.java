package com.schoolhealth.schoolmedical.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.schoolhealth.schoolmedical.entity.enums.HealthTypeHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response DTO representing a child\'s medical/health record.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParentHealthRecordResponse {
    private Long conditionId;
    private String name;
    private String reactionOrNote;
    private String imageUrl;

    @JsonProperty("isActive")
    private Boolean active;

    private HealthTypeHistory typeHistory;
    private String pupilId;
}
