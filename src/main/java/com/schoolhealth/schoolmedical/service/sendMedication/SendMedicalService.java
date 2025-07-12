package com.schoolhealth.schoolmedical.service.sendMedication;

import com.schoolhealth.schoolmedical.entity.SendMedication;
import com.schoolhealth.schoolmedical.entity.enums.StatusSendMedication;
import com.schoolhealth.schoolmedical.model.dto.request.MedicationLogReq;
import com.schoolhealth.schoolmedical.model.dto.request.SendMedicationPageReq;
import com.schoolhealth.schoolmedical.model.dto.request.SendMedicationReq;
import com.schoolhealth.schoolmedical.model.dto.response.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SendMedicalService {
    SendMedicationRes createSendMedication(SendMedicationReq sendMedicationReq, String parentId);
    List<SendMedicationRes> getAllSendMedication(String pupilId);
    void updateStatus(Long sendMedicationId, StatusSendMedication statusSendMedication);
    void deleteSendMedication(Long sendMedicationId);
    List<QuantityPupilForSessionRes> getQuantityPupilForSession();
    List<SendMedicationRes> getSendMedicationByPending();
    List<PupilRes> getAllPupilBySessionAndGrade(int session, Long gradeId);
    List<SendMedicationRes> getSendMedicationByPupil(String pupilId, int session);
    SendMedication findById(Long sendMedicationId);
    SendMedication findByMedicationLogId(Long medicationLogId);
    List<SendMedicationRes> getAllSendMedication();
    List<SendMedicationRes> getAllByComplete();
    List<SendMedicationRes> getAllByInProgress();
    List<SendMedicationSimpleRes> getSendMedicationByGradeAndSession(Long gradeId, int session, LocalDate date);

}
