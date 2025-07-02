package com.schoolhealth.schoolmedical.service.sendMedication;

import com.schoolhealth.schoolmedical.entity.*;
import com.schoolhealth.schoolmedical.entity.enums.Role;
import com.schoolhealth.schoolmedical.entity.enums.StatusSendMedication;
import com.schoolhealth.schoolmedical.entity.enums.TypeNotification;
import com.schoolhealth.schoolmedical.exception.NotFoundException;
import com.schoolhealth.schoolmedical.exception.UpdateNotAllowedException;
import com.schoolhealth.schoolmedical.model.dto.request.SendMedicationReq;
import com.schoolhealth.schoolmedical.model.dto.response.QuantityPupilByGradeRes;
import com.schoolhealth.schoolmedical.model.dto.response.QuantityPupilForSessionRes;
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
    @Autowired
    private MedicationItemService medicationItemService;
    @Override
    public SendMedicationRes createSendMedication(SendMedicationReq sendMedicationReq) {
        Pupil pupil = pupilService.findPupilById(sendMedicationReq.getPupilId());
        SendMedication sendMedication = sendMedicationMapper.toEntity(sendMedicationReq);
        List<MedicationItem> medicationItems = sendMedicationMapper.toEntity(sendMedicationReq.getMedicationItems());
        sendMedication.setPupil(pupil);
        sendMedication.setMedicationItems(medicationItems);
        for(MedicationItem medicationItem : medicationItems){
            medicationItem.setSendMedication(sendMedication);
        }
        SendMedication savedSendMedication = sendMedicationRepo.save(sendMedication);
        // Create notifications for all school nurses
        List<User> schoolNurses = userService.findAllByRole(Role.SCHOOL_NURSE);
        List<UserNotification> listNotification = new ArrayList<>();
        for( User schoolNurse : schoolNurses) {
            UserNotification notification = UserNotification.builder()
                    .message("Have a new prescription. Please check it.")
                    .sourceId(savedSendMedication.getSendMedicationId())
                    .typeNotification(TypeNotification.SEND_MEDICAL)
                    .user(schoolNurse)
                    .build();
            listNotification.add(notification);
        }
        userNotificationService.saveAllUserNotifications(listNotification);
        return sendMedicationMapper.toDto(savedSendMedication);
    }

    @Override
    public List<SendMedicationRes> getAllSendMedication(String pupilId) {
        List<SendMedication> list = sendMedicationRepo.findByPupilId(pupilId);
        if (list.isEmpty()) {
            throw new NotFoundException("No prescriptions found for pupil with id: " + pupilId);
        }
        return sendMedicationMapper.toDtoSendMedication(list);
    }

    @Override
    public void updateStatus(Long sendMedicationId, StatusSendMedication statusSendMedication) {
        SendMedication sendMedication = sendMedicationRepo.findById(sendMedicationId)
                .orElseThrow(() -> new NotFoundException("Prescription not found with id:" + sendMedicationId));
        sendMedication.setStatus(statusSendMedication);
        sendMedicationRepo.save(sendMedication);
    }

    @Override
    public void deleteSendMedication(Long sendMedicationId) {
        SendMedication sendMedication = sendMedicationRepo.findById(sendMedicationId)
                .orElseThrow(() -> new NotFoundException("Prescription not found with id:" + sendMedicationId));
        if(sendMedication.getStatus() == StatusSendMedication.PENDING){
            throw new UpdateNotAllowedException("Prescription can't deleted");
        }
        sendMedication.setActive(false);
        sendMedicationRepo.save(sendMedication);
    }

    @Override
    public List<QuantityPupilForSessionRes> getQuantityPupilForSession() {
        List<QuantityPupilByGradeRes> afterBreakfast = sendMedicationRepo.getQuantityPupilByGradeAndAfterBreakfast();
        List<QuantityPupilByGradeRes> beforeLunch = sendMedicationRepo.getQuantityPupilByGradeAndBeforeLunch();
        List<QuantityPupilByGradeRes> afterLunch = sendMedicationRepo.getQuantityPupilByGradeAndAfterLunch();
        if( afterBreakfast.isEmpty() && beforeLunch.isEmpty() && afterLunch.isEmpty()) {
            throw new NotFoundException("No data found for quantity of pupils by grade and session");
        }
        List<QuantityPupilForSessionRes> sessionRes = new ArrayList<>();
        QuantityPupilForSessionRes session1 = QuantityPupilForSessionRes.builder()
                .session("After Breakfast")
                .quantityPupilByGrade(afterBreakfast)
                .build();
        QuantityPupilForSessionRes session2 = QuantityPupilForSessionRes.builder()
                .session("Before Lunch")
                .quantityPupilByGrade(beforeLunch)
                .build();
        QuantityPupilForSessionRes session3 = QuantityPupilForSessionRes.builder()
                .session("After Lunch")
                .quantityPupilByGrade(afterLunch)
                .build();
        sessionRes.add(session1);
        sessionRes.add(session2);
        sessionRes.add(session3);
        return sessionRes;
    }

    @Override
    public List<SendMedicationRes> getSendMedicationByPending() {
        List<SendMedication> sendMedications = sendMedicationRepo.findAllByStatus(StatusSendMedication.PENDING);
        if(sendMedications.isEmpty()) {
            throw new NotFoundException("No pending prescriptions found");
        }
        return sendMedicationMapper.toDtoSendMedication(sendMedications);
    }

}
