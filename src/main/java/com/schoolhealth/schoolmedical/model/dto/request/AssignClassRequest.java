package com.schoolhealth.schoolmedical.model.dto.request;

import lombok.Data;

@Data
public class AssignClassRequest {
    String pupilId;
    long gradeId;
}
