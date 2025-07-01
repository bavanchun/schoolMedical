package com.schoolhealth.schoolmedical.service.sendMedication;

import com.schoolhealth.schoolmedical.entity.enums.StatusSendMedication;
import com.schoolhealth.schoolmedical.model.dto.request.SendMedicationReq;
import com.schoolhealth.schoolmedical.model.dto.response.SendMedicationRes;

import java.util.List;

public interface SendMedicalService {
    SendMedicationRes createSendMedication(SendMedicationReq sendMedicationReq, String userId);
    List<SendMedicationRes> getAllSendMedication(String userId);
    void updateStatus(Long sendMedicationId, StatusSendMedication statusSendMedication);
}
