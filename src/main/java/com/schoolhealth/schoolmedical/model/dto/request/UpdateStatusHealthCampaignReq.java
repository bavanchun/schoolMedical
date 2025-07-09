package com.schoolhealth.schoolmedical.model.dto.request;

import com.schoolhealth.schoolmedical.entity.enums.StatusHealthCampaign;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateStatusHealthCampaignReq {
    @NotNull(message = "Status cannot be null")
    private StatusHealthCampaign statusHealthCampaign;
}
