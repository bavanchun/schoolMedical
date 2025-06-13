package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByPhoneNumber(String phoneNumber);

    /**
     * Tìm User theo số điện thoại và tải đầy đủ danh sách học sinh (pupils)
     * Sử dụng fetch join để tải toàn bộ học sinh liên quan
     *
     * @param phoneNumber Số điện thoại của User
     * @return Optional<User> với danh sách pupils được tải đầy đủ
     */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.pupils WHERE u.phoneNumber = :phoneNumber")
    Optional<User> findByPhoneNumberWithPupils(@Param("phoneNumber") String phoneNumber);
}
