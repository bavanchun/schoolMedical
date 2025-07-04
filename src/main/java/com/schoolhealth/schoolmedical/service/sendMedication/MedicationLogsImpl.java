package com.schoolhealth.schoolmedical.service.sendMedication;

import com.schoolhealth.schoolmedical.entity.MedicationLogs;
import com.schoolhealth.schoolmedical.entity.SendMedication;
import com.schoolhealth.schoolmedical.entity.User;
import com.schoolhealth.schoolmedical.entity.UserNotification;
import com.schoolhealth.schoolmedical.entity.enums.StatusMedLogs;
import com.schoolhealth.schoolmedical.entity.enums.TypeNotification;
import com.schoolhealth.schoolmedical.exception.NotFoundException;
import com.schoolhealth.schoolmedical.model.dto.request.MedicationLogReq;
import com.schoolhealth.schoolmedical.model.dto.response.MedicationLogsRes;
import com.schoolhealth.schoolmedical.model.mapper.SendMedicationMapper;
import com.schoolhealth.schoolmedical.repository.MedicationLogsRepo;
import com.schoolhealth.schoolmedical.service.Notification.UserNotificationService;
import com.schoolhealth.schoolmedical.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MedicationLogsImpl implements MedicationLogsService {
    @Autowired
    private SendMedicalService sendMedicalService;
    @Autowired
    private MedicationLogsRepo  medicationLogsRepo;
    @Autowired
    private SendMedicationMapper  sendMedicationMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private UserNotificationService userNotificationService;
    @Override
    public MedicationLogsRes saveMedicationLogForPrescription(MedicationLogReq medicationLogReq) {
        SendMedication sendMedication = sendMedicalService.findById(medicationLogReq.getSendMedicationId());
        MedicationLogs  medicationLogs = sendMedicationMapper.toMedicationLogsEntity(medicationLogReq);
        StatusMedLogs status = medicationLogs.getStatus();
        if(status==null) status = StatusMedLogs.NOTGIVEN;
        medicationLogs.setSendMedication(sendMedication);
        medicationLogs.setStatus(status);
        MedicationLogs medicationLogs1 = medicationLogsRepo.save(medicationLogs);
        List<User> parents = userService.findAllWithPupilByParent();
        //save notification for parents
        List<UserNotification> listNotification = new ArrayList<>();
        for (User parent : parents) {
            UserNotification notification = UserNotification.builder()
                    .message("Your child has received medication")
                    .sourceId(medicationLogs1.getLogId())
                    .typeNotification(TypeNotification.SEND_MEDICAL)
                    .user(parent)
                    .build();
            listNotification.add(notification);
        }
        userNotificationService.saveAllUserNotifications(listNotification);
        return sendMedicationMapper.toMedicationLogsDto(medicationLogs);
    }
}
