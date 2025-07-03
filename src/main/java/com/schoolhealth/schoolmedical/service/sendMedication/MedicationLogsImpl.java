package com.schoolhealth.schoolmedical.service.sendMedication;

import com.schoolhealth.schoolmedical.entity.MedicationLogs;
import com.schoolhealth.schoolmedical.entity.SendMedication;
import com.schoolhealth.schoolmedical.exception.NotFoundException;
import com.schoolhealth.schoolmedical.model.dto.request.MedicationLogReq;
import com.schoolhealth.schoolmedical.model.dto.response.MedicationLogsRes;
import com.schoolhealth.schoolmedical.model.mapper.SendMedicationMapper;
import com.schoolhealth.schoolmedical.repository.MedicationLogsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicationLogsImpl implements MedicationLogsService {
    @Autowired
    private SendMedicalService sendMedicalService;
    @Autowired
    private MedicationLogsRepo  medicationLogsRepo;
    @Autowired
    private SendMedicationMapper  sendMedicationMapper;
    @Override
    public MedicationLogsRes saveMedicationLogForPrescription(MedicationLogReq medicationLogReq) {
        SendMedication sendMedication = sendMedicalService.findById(medicationLogReq.getSendMedicationId());
        MedicationLogs  medicationLogs = sendMedicationMapper.toMedicationLogsEntity(medicationLogReq);
        medicationLogs.setSendMedication(sendMedication);
        medicationLogs.setStatus(medicationLogReq.getStatus());
        medicationLogsRepo.save(medicationLogs);
        return sendMedicationMapper.toMedicationLogsDto(medicationLogs);
    }
}
