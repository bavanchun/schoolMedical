package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.UserNotification;
import com.schoolhealth.schoolmedical.model.dto.response.NotificationRes;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-27T22:30:09+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Amazon.com Inc.)"
)
@Component
public class NotificationMapperImpl implements NotificationMapper {

    @Override
    public NotificationRes toDto(UserNotification notificationRes) {
        if ( notificationRes == null ) {
            return null;
        }

        NotificationRes.NotificationResBuilder notificationRes1 = NotificationRes.builder();

        notificationRes1.notificationId( notificationRes.getNotificationId() );
        notificationRes1.message( notificationRes.getMessage() );
        notificationRes1.createdAt( notificationRes.getCreatedAt() );
        notificationRes1.typeNotification( notificationRes.getTypeNotification() );
        notificationRes1.sourceId( notificationRes.getSourceId() );

        return notificationRes1.build();
    }

    @Override
    public List<NotificationRes> toDto(List<UserNotification> notificationRes) {
        if ( notificationRes == null ) {
            return null;
        }

        List<NotificationRes> list = new ArrayList<NotificationRes>( notificationRes.size() );
        for ( UserNotification userNotification : notificationRes ) {
            list.add( toDto( userNotification ) );
        }

        return list;
    }
}
