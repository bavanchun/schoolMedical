package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.Pupil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PupilRepo extends JpaRepository<Pupil, String> {
    /**
     * Tìm danh sách học sinh dựa trên số điện thoại phụ huynh trong mối quan hệ
     * @param phoneNumber Số điện thoại của phụ huynh
     * @return Danh sách học sinh có phụ huynh sở hữu số điện thoại được cung cấp
     */
    @Query("SELECT p FROM Pupil p JOIN p.parents parent WHERE parent.phoneNumber = :phoneNumber")
    List<Pupil> findByLinkedParentPhoneNumber(@Param("phoneNumber") String phoneNumber);

    /**
     * Tìm danh sách học sinh dựa trên số điện thoại phụ huynh được lưu trực tiếp trong entity Pupil
     * @param phoneNumber Số điện thoại của phụ huynh
     * @return Danh sách học sinh có số điện thoại phụ huynh trùng với tham số
     */
    List<Pupil> findByParentPhoneNumber(String phoneNumber);
}
