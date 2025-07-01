package com.schoolhealth.schoolmedical.service.sendMedical;

import com.schoolhealth.schoolmedical.model.dto.request.SendMedicationReq;
import com.schoolhealth.schoolmedical.model.dto.response.SendMedicationRes;

public interface SendMedicalService {
    SendMedicationRes createSendMedication(SendMedicationReq sendMedicationReq, String userId);
}
