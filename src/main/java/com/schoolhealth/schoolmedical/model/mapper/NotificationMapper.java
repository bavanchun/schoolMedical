package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.UserNotification;
import com.schoolhealth.schoolmedical.model.dto.response.NotificationRes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PupilMapper.class})
public interface NotificationMapper {
    @Mappings({
            @Mapping(target = "pupilRes" , source = "user.pupils"),
    })
    NotificationRes toDto(UserNotification notificationRes);
    List<NotificationRes> toDto(List<UserNotification> notificationRes);
}
