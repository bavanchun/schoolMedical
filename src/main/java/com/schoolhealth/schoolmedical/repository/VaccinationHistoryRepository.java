package com.schoolhealth.schoolmedical.repository;

import com.schoolhealth.schoolmedical.entity.VaccinationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VaccinationHistoryRepository extends JpaRepository<VaccinationHistory, Integer> {

    // Lấy lịch sử tiêm chủng của 1 học sinh (pupil) còn active
    List<VaccinationHistory> findByPupilIdAndIsActiveTrue(String pupilId);

    // (Optionally) tìm 1 history theo id và active
    VaccinationHistory findByHistoryIdAndIsActiveTrue(int historyId);
}