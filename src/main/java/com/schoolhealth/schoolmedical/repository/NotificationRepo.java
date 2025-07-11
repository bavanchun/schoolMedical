package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepo extends JpaRepository<UserNotification,Long> {

    @Query("SELECT n FROM UserNotification n " +
            "JOIN FETCH n.user u " +
            "JOIN FETCH u.pupils p " +
            "WHERE u.userId = :parentId")
    List<UserNotification> findAllByUserId(@Param("parentId") String parentId);
}
