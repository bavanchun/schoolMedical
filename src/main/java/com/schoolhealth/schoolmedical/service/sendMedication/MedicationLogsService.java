package com.schoolhealth.schoolmedical.service.sendMedication;

import com.schoolhealth.schoolmedical.model.dto.request.MedicationLogReq;
import com.schoolhealth.schoolmedical.model.dto.response.MedicationLogsRes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MedicationLogsService {
    MedicationLogsRes saveMedicationLogForPrescription(MedicationLogReq medicationLogReq);
    List<MedicationLogsRes> getMedicationLogsBySendMedicationId(Long sendMedicationId, Optional<LocalDate> givenTime);
}
