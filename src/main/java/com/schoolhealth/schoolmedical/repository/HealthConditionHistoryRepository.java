package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.HealthConditionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HealthConditionHistoryRepository extends JpaRepository<HealthConditionHistory, Long> {
    // Lấy tất cả hồ sơ sức khỏe của 1 học sinh (pupilId) và còn hoạt động (isActive = true)
    List<HealthConditionHistory> findByPupil_PupilIdAndIsActiveTrue(String pupilId);

    // Có thể bổ sung các method khác nếu cần lọc nâng cao (theo type, theo tên, ...)
}
