package com.schoolhealth.schoolmedical.model.dto.request;

import com.schoolhealth.schoolmedical.entity.enums.HealthTypeHistory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * Request DTO used by parents to create or update a medical/health record for their child.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParentHealthRecordRequest {
    @NotBlank
    private String name;
    private String reactionOrNote;
    private String imageUrl;
    @NotNull
    private HealthTypeHistory typeHistory;
    @NotBlank
    private String pupilId;
    private Boolean isActive;
}