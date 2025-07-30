package com.schoolhealth.schoolmedical.model.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TotalUser {
    private Long total;
    private List<TotalUserRole> totalUserRoles;
}
