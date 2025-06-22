package com.schoolhealth.schoolmedical.service.pupil;

import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.model.dto.response.PupilRes;
import com.schoolhealth.schoolmedical.model.dto.request.AssignClassRequest;

import java.util.List;

public interface PupilService {
    PupilRes createPupil(PupilRes dto);
    List<PupilRes> getAllPupils();
    PupilRes getPupilById(String id);
    PupilRes updatePupil(String id, PupilRes dto);
    void deletePupil(String id);

    Pupil assignPupilClass(AssignClassRequest assignClassRequest);

    /**
     * Tìm danh sách học sinh theo số điện thoại phụ huynh
     * @param phoneNumber Số điện thoại của phụ huynh
     * @return Danh sách học sinh có số điện thoại phụ huynh trùng khớp
     */
    List<Pupil> findByParentPhoneNumber(String phoneNumber);
    List<Pupil> getAll();
    List<PupilRes> getAllPupilsByParent(String parentId);
}
