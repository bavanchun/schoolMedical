package com.schoolhealth.schoolmedical.service.sendMedication;

import com.schoolhealth.schoolmedical.model.dto.request.MedicationLogReq;
import com.schoolhealth.schoolmedical.model.dto.response.MedicationLogsRes;

import java.util.List;

public interface MedicationLogsService {
    MedicationLogsRes saveMedicationLogForPrescription(MedicationLogReq medicationLogReq);
}
