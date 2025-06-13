package com.schoolhealth.schoolmedical.entity;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PupilParentId implements Serializable {
    private String pupilId;
    private String parentId;
}
