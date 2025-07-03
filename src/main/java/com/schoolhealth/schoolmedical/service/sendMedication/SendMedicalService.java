package com.schoolhealth.schoolmedical.service.sendMedication;

import com.schoolhealth.schoolmedical.entity.enums.StatusSendMedication;
import com.schoolhealth.schoolmedical.model.dto.request.SendMedicationReq;
import com.schoolhealth.schoolmedical.model.dto.response.PupilRes;
import com.schoolhealth.schoolmedical.model.dto.response.QuantityPupilByGradeRes;
import com.schoolhealth.schoolmedical.model.dto.response.QuantityPupilForSessionRes;
import com.schoolhealth.schoolmedical.model.dto.response.SendMedicationRes;

import java.util.List;

public interface SendMedicalService {
    SendMedicationRes createSendMedication(SendMedicationReq sendMedicationReq, String parentId);
    List<SendMedicationRes> getAllSendMedication(String pupilId);
    void updateStatus(Long sendMedicationId, StatusSendMedication statusSendMedication);
    void deleteSendMedication(Long sendMedicationId);

    List<QuantityPupilForSessionRes> getQuantityPupilForSession();
    List<SendMedicationRes> getSendMedicationByPending();
    List<PupilRes> getAllPupilBySessionAndGrade(int session, Long gradeId);
    List<SendMedicationRes> getSendMedicationByPupil(String pupilId, int session);
}
