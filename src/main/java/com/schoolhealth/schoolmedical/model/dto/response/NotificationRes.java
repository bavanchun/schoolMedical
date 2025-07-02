package com.schoolhealth.schoolmedical.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.schoolhealth.schoolmedical.entity.enums.TypeNotification;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationRes {
    private Long notificationId;
    private String message;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdAt;
    private TypeNotification typeNotification;
    private Long sourceId;
    private List<PupilSimpleRes> pupilRes;
}
