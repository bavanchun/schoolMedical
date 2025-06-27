package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.UserNotification;
import com.schoolhealth.schoolmedical.model.dto.response.NotificationRes;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationRes toDto(UserNotification notificationRes);
    List<NotificationRes> toDto(List<UserNotification> notificationRes);
}
