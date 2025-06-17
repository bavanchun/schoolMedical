package com.schoolhealth.schoolmedical.service;

import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.model.dto.PupilDto;
import com.schoolhealth.schoolmedical.model.dto.request.AssignClassRequest;

import java.util.List;
import java.util.Optional;

public interface PupilService {
    PupilDto createPupil(PupilDto dto);
    List<PupilDto> getAllPupils();
    PupilDto getPupilById(String id);
    PupilDto updatePupil(String id, PupilDto dto);
    void deletePupil(String id);

    Pupil assignPupilClass(AssignClassRequest assignClassRequest);

    /**
     * Tìm danh sách học sinh theo số điện thoại phụ huynh
     * @param phoneNumber Số điện thoại của phụ huynh
     * @return Danh sách học sinh có số điện thoại phụ huynh trùng khớp
     */
    List<Pupil> findByParentPhoneNumber(String phoneNumber);
    List<Pupil> getAll();
}
