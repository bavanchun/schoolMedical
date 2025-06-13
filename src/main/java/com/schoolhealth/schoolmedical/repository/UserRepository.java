package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByPhoneNumber(int phoneNumber);
}
