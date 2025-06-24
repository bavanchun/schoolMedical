package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, String> {

    @Query("SELECT p from User p " +
            "join fetch p.pupils " +
            "where p.role = com.schoolhealth.schoolmedical.entity.enums.Role.PARENT")
    List<User> findByPupil();
}
