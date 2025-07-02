package com.schoolhealth.schoolmedical.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserDeviceTokenResponse {
    private String deviceToken;
}
