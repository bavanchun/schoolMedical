package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepo extends JpaRepository<UserNotification,Long> {
}
