package com.schoolhealth.schoolmedical;

import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.entity.SendMedication;
import com.schoolhealth.schoolmedical.entity.User;
import com.schoolhealth.schoolmedical.entity.enums.Role;
import com.schoolhealth.schoolmedical.entity.enums.StatusSendMedication;
import com.schoolhealth.schoolmedical.exception.NotFoundException;
import com.schoolhealth.schoolmedical.exception.UpdateNotAllowedException;
import com.schoolhealth.schoolmedical.model.dto.request.MedicationItemReq;
import com.schoolhealth.schoolmedical.model.dto.request.SendMedicationReq;
import com.schoolhealth.schoolmedical.model.dto.response.QuantityPupilByGradeRes;
import com.schoolhealth.schoolmedical.model.dto.response.SendMedicationRes;
import com.schoolhealth.schoolmedical.model.mapper.PupilMapper;
import com.schoolhealth.schoolmedical.model.mapper.SendMedicationMapper;
import com.schoolhealth.schoolmedical.repository.SendMedicationRepo;
import com.schoolhealth.schoolmedical.service.Notification.UserNotificationService;
import com.schoolhealth.schoolmedical.service.pupil.PupilService;
import com.schoolhealth.schoolmedical.service.sendMedication.SendMedicalImpl;
import com.schoolhealth.schoolmedical.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SendMedicalServiceTest {

    @InjectMocks
    private SendMedicalImpl sendMedicalService;

    @Mock
    private SendMedicationRepo sendMedicationRepo;

    @Mock
    private UserService userService;

    @Mock
    private SendMedicationMapper sendMedicationMapper;

    @Mock
    private PupilService pupilService;

    @Mock
    private UserNotificationService userNotificationService;

    @Mock
    private PupilMapper pupilMapper;

    private SendMedicationReq sendMedicationReq;
    private User parent;
    private Pupil pupil;
    private SendMedication sendMedication;
    private SendMedicationRes sendMedicationRes;

    @BeforeEach
    void setUp() {
        parent = new User();
        parent.setUserId("parent123");
        parent.setFirstName("John");
        parent.setLastName("Doe");

        pupil = new Pupil();
        pupil.setPupilId("pupil123");

        sendMedicationReq = new SendMedicationReq();
        sendMedicationReq.setPupilId("pupil123");
        sendMedicationReq.setMedicationItems(Collections.singletonList(new MedicationItemReq()));

        sendMedication = new SendMedication();
        sendMedication.setSendMedicationId(1L);
        sendMedication.setPupil(pupil);
        sendMedication.setStatus(StatusSendMedication.PENDING);
        sendMedication.setActive(true);

        sendMedicationRes = new SendMedicationRes();
        sendMedicationRes.setSendMedicationId(1L);
    }

    @Test
    void testCreateSendMedication_Success() {
        when(userService.findById("parent123")).thenReturn(parent);
        when(pupilService.findPupilById("pupil123")).thenReturn(pupil);
        when(sendMedicationMapper.toEntity(any(SendMedicationReq.class))).thenReturn(sendMedication);
        when(sendMedicationRepo.save(any(SendMedication.class))).thenReturn(sendMedication);
        when(userService.findAllByRole(Role.SCHOOL_NURSE)).thenReturn(Collections.singletonList(new User()));
        when(sendMedicationMapper.toDto(any(SendMedication.class))).thenReturn(sendMedicationRes);

        SendMedicationRes result = sendMedicalService.createSendMedication(sendMedicationReq, "parent123");

        assertNotNull(result);
        assertEquals(1L, result.getSendMedicationId());
        verify(sendMedicationRepo, times(1)).save(any(SendMedication.class));
        verify(userNotificationService, times(1)).saveAllUserNotifications(any());
    }

    @Test
    void testGetAllSendMedication_ByPupilId_Success() {
        when(sendMedicationRepo.findByPupilId("pupil123")).thenReturn(Collections.singletonList(sendMedication));
        when(sendMedicationMapper.toDtoSendMedication(anyList())).thenReturn(Collections.singletonList(sendMedicationRes));

        List<SendMedicationRes> results = sendMedicalService.getAllSendMedication("pupil123");

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
    }

    @Test
    void testGetAllSendMedication_ByPupilId_NotFound() {
        when(sendMedicationRepo.findByPupilId("pupil123")).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> sendMedicalService.getAllSendMedication("pupil123"));
    }

    @Test
    void testUpdateStatus_Approved_Success() {
        sendMedication.setStatus(StatusSendMedication.PENDING);
        when(sendMedicationRepo.findById(1L)).thenReturn(Optional.of(sendMedication));
        when(userService.findAllByRole(Role.PARENT)).thenReturn(Collections.singletonList(parent));

        sendMedicalService.updateStatus(1L, StatusSendMedication.APPROVED);

        assertEquals(StatusSendMedication.APPROVED, sendMedication.getStatus());
        verify(sendMedicationRepo, times(1)).save(sendMedication);
        verify(userNotificationService, times(1)).saveAllUserNotifications(anyList());
    }

    @Test
    void testUpdateStatus_Rejected_Success() {
        sendMedication.setStatus(StatusSendMedication.PENDING);
        when(sendMedicationRepo.findById(1L)).thenReturn(Optional.of(sendMedication));
        when(userService.findAllByRole(Role.PARENT)).thenReturn(Collections.singletonList(parent));

        sendMedicalService.updateStatus(1L, StatusSendMedication.REJECTED);

        assertEquals(StatusSendMedication.REJECTED, sendMedication.getStatus());
        verify(sendMedicationRepo, times(1)).save(sendMedication);
        verify(userNotificationService, times(1)).saveAllUserNotifications(anyList());
    }

    @Test
    void testUpdateStatus_Rejected_NotAllowed() {
        sendMedication.setStatus(StatusSendMedication.APPROVED); // Not PENDING
        when(sendMedicationRepo.findById(1L)).thenReturn(Optional.of(sendMedication));

        assertThrows(UpdateNotAllowedException.class, () -> sendMedicalService.updateStatus(1L, StatusSendMedication.REJECTED));
    }

    @Test
    void testUpdateStatus_NotFound() {
        when(sendMedicationRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sendMedicalService.updateStatus(1L, StatusSendMedication.APPROVED));
    }

    @Test
    void testDeleteSendMedication_Success() {
        when(sendMedicationRepo.findById(1L)).thenReturn(Optional.of(sendMedication));

        sendMedicalService.deleteSendMedication(1L);

        assertFalse(sendMedication.isActive());
        verify(sendMedicationRepo, times(1)).save(sendMedication);
    }

    @Test
    void testDeleteSendMedication_NotAllowed() {
        sendMedication.setStatus(StatusSendMedication.APPROVED);
        when(sendMedicationRepo.findById(1L)).thenReturn(Optional.of(sendMedication));

        assertThrows(UpdateNotAllowedException.class, () -> sendMedicalService.deleteSendMedication(1L));
    }

    @Test
    void testDeleteSendMedication_NotFound() {
        when(sendMedicationRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sendMedicalService.deleteSendMedication(1L));
    }

    @Test
    void testGetQuantityPupilForSession_Success() {
        List<QuantityPupilByGradeRes> mockData = Collections.singletonList(new QuantityPupilByGradeRes(1L, 10L));
        when(sendMedicationRepo.getQuantityPupilByGradeAndAfterBreakfast()).thenReturn(mockData);
        when(sendMedicationRepo.getQuantityPupilByGradeAndBeforeLunch()).thenReturn(mockData);
        when(sendMedicationRepo.getQuantityPupilByGradeAndAfterLunch()).thenReturn(mockData);

        var result = sendMedicalService.getQuantityPupilForSession();

        assertEquals(3, result.size());
        assertEquals("After Breakfast", result.get(0).getSession());
    }

    @Test
    void testGetQuantityPupilForSession_NotFound() {
        when(sendMedicationRepo.getQuantityPupilByGradeAndAfterBreakfast()).thenReturn(Collections.emptyList());
        when(sendMedicationRepo.getQuantityPupilByGradeAndBeforeLunch()).thenReturn(Collections.emptyList());
        when(sendMedicationRepo.getQuantityPupilByGradeAndAfterLunch()).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> sendMedicalService.getQuantityPupilForSession());
    }

    @Test
    void testGetSendMedicationByPending_Success() {
        when(sendMedicationRepo.findAllByStatus(StatusSendMedication.PENDING)).thenReturn(Collections.singletonList(sendMedication));
        when(sendMedicationMapper.toDtoSendMedication(anyList())).thenReturn(Collections.singletonList(sendMedicationRes));

        var results = sendMedicalService.getSendMedicationByPending();

        assertFalse(results.isEmpty());
    }

    @Test
    void testGetSendMedicationByPending_NotFound() {
        when(sendMedicationRepo.findAllByStatus(StatusSendMedication.PENDING)).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> sendMedicalService.getSendMedicationByPending());
    }

    @Test
    void testGetSendMedicationByPupil_Success() {
        when(sendMedicationRepo.findByPupilIdAndStatus("pupil123", StatusSendMedication.APPROVED)).thenReturn(Collections.singletonList(sendMedication));
        when(sendMedicationMapper.toDtoWithAfterBreakfast(anyList())).thenReturn(Collections.singletonList(sendMedicationRes));

        var results = sendMedicalService.getSendMedicationByPupil("pupil123", 1);

        assertFalse(results.isEmpty());
    }

    @Test
    void testGetSendMedicationByPupil_Success_Session2() {
        when(sendMedicationRepo.findByPupilIdAndStatus("pupil123", StatusSendMedication.APPROVED)).thenReturn(Collections.singletonList(sendMedication));
        when(sendMedicationMapper.toDtoWithBeforeLunch(anyList())).thenReturn(Collections.singletonList(sendMedicationRes));

        var results = sendMedicalService.getSendMedicationByPupil("pupil123", 2);

        assertFalse(results.isEmpty());
        verify(sendMedicationMapper, times(1)).toDtoWithBeforeLunch(anyList());
    }

    @Test
    void testGetSendMedicationByPupil_Success_Session3() {
        when(sendMedicationRepo.findByPupilIdAndStatus("pupil123", StatusSendMedication.APPROVED)).thenReturn(Collections.singletonList(sendMedication));
        when(sendMedicationMapper.toDtoWithAfterLunch(anyList())).thenReturn(Collections.singletonList(sendMedicationRes));

        var results = sendMedicalService.getSendMedicationByPupil("pupil123", 3);

        assertFalse(results.isEmpty());
        verify(sendMedicationMapper, times(1)).toDtoWithAfterLunch(anyList());
    }

    @Test
    void testGetSendMedicationByPupil_NotFound() {
        when(sendMedicationRepo.findByPupilIdAndStatus("pupil123", StatusSendMedication.APPROVED)).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> sendMedicalService.getSendMedicationByPupil("pupil123", 1));
    }

    @Test
    void testGetSendMedicationByPupil_InvalidSession() {
        when(sendMedicationRepo.findByPupilIdAndStatus("pupil123", StatusSendMedication.APPROVED)).thenReturn(Collections.singletonList(sendMedication));
        assertThrows(NotFoundException.class, () -> sendMedicalService.getSendMedicationByPupil("pupil123", 4));
    }

    @Test
    void testGetSendMedicationByApproved_Success() {
        when(sendMedicationRepo.findAllByStatus(StatusSendMedication.APPROVED)).thenReturn(Collections.singletonList(sendMedication));
        when(sendMedicationMapper.toDto(anyList())).thenReturn(Collections.singletonList(sendMedicationRes));

        var results = sendMedicalService.getSendMedicationByApproved();

        assertFalse(results.isEmpty());
    }

    @Test
    void testGetSendMedicationByApproved_ReturnsEmpty() {
        when(sendMedicationRepo.findAllByStatus(StatusSendMedication.APPROVED)).thenReturn(Collections.emptyList());
        when(sendMedicationMapper.toDto(anyList())).thenReturn(new ArrayList<>());

        var results = sendMedicalService.getSendMedicationByApproved();

        assertTrue(results.isEmpty());
    }

    @Test
    void testGetAllByInProgress_Success() {
        when(sendMedicationRepo.findAllByInProgress(any())).thenReturn(Collections.singletonList(sendMedication));
        when(sendMedicationMapper.toDto(anyList())).thenReturn(Collections.singletonList(sendMedicationRes));

        var results = sendMedicalService.getAllByInProgress();

        assertFalse(results.isEmpty());
    }

    @Test
    void testGetAllByInProgress_ReturnsEmpty() {
        when(sendMedicationRepo.findAllByInProgress(any())).thenReturn(Collections.emptyList());
        when(sendMedicationMapper.toDto(anyList())).thenReturn(new ArrayList<>());

        var results = sendMedicalService.getAllByInProgress();

        assertTrue(results.isEmpty());
    }

    @Test
    void testGetAllByComplete_Success() {
        when(sendMedicationRepo.findAllByStatus(StatusSendMedication.COMPLETED)).thenReturn(Collections.singletonList(sendMedication));
        when(sendMedicationMapper.toDto(anyList())).thenReturn(Collections.singletonList(sendMedicationRes));

        var results = sendMedicalService.getAllByComplete();

        assertFalse(results.isEmpty());
    }

    @Test
    void testGetAllByComplete_ReturnsEmpty() {
        when(sendMedicationRepo.findAllByStatus(StatusSendMedication.COMPLETED)).thenReturn(Collections.emptyList());
        when(sendMedicationMapper.toDto(anyList())).thenReturn(new ArrayList<>());

        var results = sendMedicalService.getAllByComplete();

        assertTrue(results.isEmpty());
    }
}
