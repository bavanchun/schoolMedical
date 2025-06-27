package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    @Query("""
SELECT p FROM Pupil p
JOIN FETCH p.pupilGrade pg
JOIN FETCH pg.grade
JOIN p.parents parent
WHERE parent.userId = :parentId AND pg.startYear = (
    SELECT MAX(sub_pg.startYear)
    FROM PupilGrade sub_pg
    WHERE sub_pg.pupil.pupilId = p.pupilId
)
""")
    List<Pupil> getAllPupilsByParent(@Param("parentId") String parentId);


    @Query("SELECT p FROM Pupil p JOIN p.pupilGrade pg WHERE pg.grade.gradeId = :gradeId")
    List<Pupil> findByGrade_GradeId(@Param("gradeId") Long gradeId);

    @Query("SELECT p FROM Pupil p WHERE p.isActive = true AND " +
            "(SELECT COUNT(vh.historyId) FROM VaccinationHistory vh " +
            "WHERE vh.pupil = p AND vh.disease.diseaseId = :diseaseId AND vh.isActive = true) < :doseNumber")
    List<Pupil> findPupilsNeedingVaccination(@Param("diseaseId") Long diseaseId, @Param("doseNumber") int doseNumber);

    @Query("SELECT p FROM Pupil p JOIN p.parents parent WHERE parent = :parent")
    List<Pupil> findByParent(@Param("parent") User parent);
        @Query("""
    SELECT p FROM Pupil p
    JOIN FETCH p.pupilGrade pg
    JOIN FETCH pg.grade
    WHERE p.pupilId = :pupilId AND pg.startYear = (
        SELECT MAX(sub_pg.startYear)
        FROM PupilGrade sub_pg
        WHERE sub_pg.pupil.pupilId = p.pupilId
    )
    """)
    Pupil findPupilById(@Param("pupilId") String pupilId);
}
