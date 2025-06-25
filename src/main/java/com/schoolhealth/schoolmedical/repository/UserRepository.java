package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.User;
import com.schoolhealth.schoolmedical.entity.enums.Role;
import com.schoolhealth.schoolmedical.model.dto.request.UserDeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
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

    List<User> findAllByRole(Role role);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.deviceToken = :deviceToken WHERE u.userId = :userId")
    int updateDeviceToken(@Param("userId") String userId, @Param("deviceToken") String deviceToken);

    @Query("SELECT p from User p " +
            "join fetch p.pupils " +
            "where p.role = com.schoolhealth.schoolmedical.entity.enums.Role.PARENT")
    List<User> findAllWithPupilsByParent();
}
