package com.schoolhealth.schoolmedical.model.dto.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserDeviceToken {
    private String userId;
    private String deviceToken;
}
