package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.entity.User;
import com.schoolhealth.schoolmedical.entity.UserNotification;
import com.schoolhealth.schoolmedical.model.dto.response.NotificationRes;
import com.schoolhealth.schoolmedical.model.dto.response.PupilSimpleRes;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-29T11:44:04+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Amazon.com Inc.)"
)
@Component
public class NotificationMapperImpl implements NotificationMapper {

    @Autowired
    private PupilMapper pupilMapper;

    @Override
    public NotificationRes toDto(UserNotification notificationRes) {
        if ( notificationRes == null ) {
            return null;
        }

        NotificationRes.NotificationResBuilder notificationRes1 = NotificationRes.builder();

        List<Pupil> pupils = notificationResUserPupils( notificationRes );
        notificationRes1.pupilRes( pupilListToPupilSimpleResList( pupils ) );
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

    private List<Pupil> notificationResUserPupils(UserNotification userNotification) {
        User user = userNotification.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getPupils();
    }

    protected List<PupilSimpleRes> pupilListToPupilSimpleResList(List<Pupil> list) {
        if ( list == null ) {
            return null;
        }

        List<PupilSimpleRes> list1 = new ArrayList<PupilSimpleRes>( list.size() );
        for ( Pupil pupil : list ) {
            list1.add( pupilMapper.toSimpleDto( pupil ) );
        }

        return list1;
    }
}
