package com.schoolhealth.schoolmedical.service.sendMedication;

import com.schoolhealth.schoolmedical.entity.*;
import com.schoolhealth.schoolmedical.entity.enums.Role;
import com.schoolhealth.schoolmedical.entity.enums.StatusSendMedication;
import com.schoolhealth.schoolmedical.entity.enums.TypeNotification;
import com.schoolhealth.schoolmedical.exception.NotFoundException;
import com.schoolhealth.schoolmedical.exception.UpdateNotAllowedException;
import com.schoolhealth.schoolmedical.model.dto.request.MedicationLogReq;
import com.schoolhealth.schoolmedical.model.dto.request.SendMedicationReq;
import com.schoolhealth.schoolmedical.model.dto.response.*;
import com.schoolhealth.schoolmedical.model.mapper.PupilMapper;
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
    @Autowired
    private PupilMapper pupilMapper;

    @Override
    public SendMedicationRes createSendMedication(SendMedicationReq sendMedicationReq, String parentId) {
        User parent = userService.findById(parentId);
        Pupil pupil = pupilService.findPupilById(sendMedicationReq.getPupilId());
        SendMedication sendMedication = sendMedicationMapper.toEntity(sendMedicationReq);
        List<MedicationItem> medicationItems = sendMedicationMapper.toEntity(sendMedicationReq.getMedicationItems());
        sendMedication.setPupil(pupil);
        sendMedication.setMedicationItems(medicationItems);
        sendMedication.setSenderName(parent.getLastName() + parent.getFirstName());
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
        if(statusSendMedication == StatusSendMedication.REJECTED && sendMedication.getStatus() != StatusSendMedication.PENDING) {
                throw new UpdateNotAllowedException("Prescription can't be cancelled. Cancelled when prescription is PENDING.");
        }
        sendMedication.setStatus(statusSendMedication);
        sendMedicationRepo.save(sendMedication);
    }

    @Override
    public void deleteSendMedication(Long sendMedicationId) {
        SendMedication sendMedication = sendMedicationRepo.findById(sendMedicationId)
                .orElseThrow(() -> new NotFoundException("Prescription not found with id:" + sendMedicationId));
        if(sendMedication.getStatus() != StatusSendMedication.PENDING){
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

    @Override
    public List<PupilRes> getAllPupilBySessionAndGrade(int session, Long gradeId) {
        List<Pupil> pupils = new ArrayList<>();
        String sessionName = null;
        if(session == 1){
            sessionName = "After breakfast: 9h00-9h30";
            pupils = sendMedicationRepo.findAllPupilBySessionAndGrade(sessionName, gradeId, StatusSendMedication.APPROVED);
        } else if(session == 2) {
            sessionName = "Before lunch: 10h30-11h00";
            pupils = sendMedicationRepo.findAllPupilBySessionAndGrade(sessionName, gradeId, StatusSendMedication.APPROVED);
        } else if(session == 3) {
            sessionName = "After lunch: 11h30-12h00";
            pupils = sendMedicationRepo.findAllPupilBySessionAndGrade(sessionName, gradeId, StatusSendMedication.APPROVED);
        } else {
            throw new NotFoundException("Session not found");
        }
        if (pupils.isEmpty()) {
            throw new NotFoundException("No pupils found for grade id: " + gradeId + " and session: " + session + "and year: " + java.time.Year.now().getValue());
        }
        return pupilMapper.toPupilResWithoutParent(pupils);
    }

    @Override
    public List<SendMedicationRes> getSendMedicationByPupil(String pupilId,int session) {
        List<SendMedication> sendMedications = sendMedicationRepo.findByPupilIdAndStatus(pupilId,StatusSendMedication.APPROVED);;
        List<SendMedicationRes> sendMedicationRes = new ArrayList<>();
        if (sendMedications.isEmpty()) {
            throw new NotFoundException("No SendMedication found");
        }
        if(session == 1){
            sendMedicationRes = sendMedicationMapper.toDtoWithAfterBreakfast(sendMedications);
        }else if(session == 2) {
            sendMedicationRes = sendMedicationMapper.toDtoWithBeforeLunch(sendMedications);
        } else if(session == 3) {
            sendMedicationRes = sendMedicationMapper.toDtoWithAfterLunch(sendMedications);
        } else {
            throw new NotFoundException("Session not found");
        }
        return sendMedicationRes;
    }

    @Override
    public SendMedication findById(Long sendMedicationId) {
        return sendMedicationRepo.findById(sendMedicationId)
                .orElseThrow(() -> new NotFoundException("SendMedication not found"));
    }


}
