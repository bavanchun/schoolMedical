package com.schoolhealth.schoolmedical.model.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PupilSimpleRes {
    private String pupilId;
    private String lastName;
    private String firstName;
}
