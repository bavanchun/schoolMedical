package com.schoolhealth.schoolmedical.service.sendMedication;

import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.entity.SendMedication;
import com.schoolhealth.schoolmedical.entity.User;
import com.schoolhealth.schoolmedical.entity.UserNotification;
import com.schoolhealth.schoolmedical.entity.enums.Role;
import com.schoolhealth.schoolmedical.entity.enums.StatusSendMedication;
import com.schoolhealth.schoolmedical.entity.enums.TypeNotification;
import com.schoolhealth.schoolmedical.exception.NotFoundException;
import com.schoolhealth.schoolmedical.model.dto.request.SendMedicationReq;
import com.schoolhealth.schoolmedical.model.dto.response.SendMedicationRes;
import com.schoolhealth.schoolmedical.model.mapper.SendMedicationMapper;
import com.schoolhealth.schoolmedical.repository.SendMedicationRepo;
import com.schoolhealth.schoolmedical.service.Notification.UserNotificationService;
import com.schoolhealth.schoolmedical.service.pupil.PupilService;
import com.schoolhealth.schoolmedical.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SendMedicalImpl implements SendMedicalService{
    @Autowired
    private SendMedicationRepo sendMedicationRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private SendMedicationMapper sendMedicationMapper;
    @Autowired
    private PupilService pupilService;
    @Autowired
    private UserNotificationService userNotificationService;
    @Override
    public SendMedicationRes createSendMedication(SendMedicationReq sendMedicationReq) {
        Pupil pupil = pupilService.findPupilById(sendMedicationReq.getPupilId());
        List<User> parent = pupil.getParents();
        List<SendMedication> sendMedicationList = new ArrayList<>();
        for(User user : parent){
            SendMedication sendMedication = sendMedicationMapper.toEntity(sendMedicationReq);
            sendMedication.setPupil(pupil);
            sendMedication.setUser(user);
            sendMedicationList.add(sendMedication);
        }

        List<SendMedication> savedSendMedication = sendMedicationRepo.saveAll(sendMedicationList);
        // Create notifications for all school nurses
        List<User> schoolNurses = userService.findAllByRole(Role.SCHOOL_NURSE);
        List<UserNotification> listNotification = new ArrayList<>();
        for( User schoolNurse : schoolNurses) {
            UserNotification notification = UserNotification.builder()
                    .message("Have a new prescription. Please check it.")
                    .sourceId(savedSendMedication.getFirst().getSendMedicationId())
                    .typeNotification(TypeNotification.SEND_MEDICAL)
                    .user(schoolNurse)
                    .build();
            listNotification.add(notification);
        }
        userNotificationService.saveAllUserNotifications(listNotification);
        return sendMedicationMapper.toDto(savedSendMedication.getFirst());
    }

    @Override
    public List<SendMedicationRes> getAllSendMedication(String userId) {
        List<SendMedication> list = sendMedicationRepo.findByUserId(userId);
        return List.of();
    }

    @Override
    public void updateStatus(Long sendMedicationId, StatusSendMedication statusSendMedication) {
        SendMedication sendMedication = sendMedicationRepo.findById(sendMedicationId)
                .orElseThrow(() -> new NotFoundException("Prescription not found with id:" + sendMedicationId));
        sendMedication.setStatus(statusSendMedication);
        sendMedicationRepo.save(sendMedication);
    }
}
