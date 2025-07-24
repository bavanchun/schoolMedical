package com.schoolhealth.schoolmedical;

import com.schoolhealth.schoolmedical.entity.*;
import com.schoolhealth.schoolmedical.entity.enums.MedicalEventStatus;
import com.schoolhealth.schoolmedical.entity.enums.Role;
import com.schoolhealth.schoolmedical.exception.NotFoundException;
import com.schoolhealth.schoolmedical.model.dto.request.CreateMedicalEventRequest;
import com.schoolhealth.schoolmedical.model.dto.response.MedicalEventResponse;
import com.schoolhealth.schoolmedical.model.mapper.MedicalEventMapper;
import com.schoolhealth.schoolmedical.repository.*;
import com.schoolhealth.schoolmedical.service.Notification.FCMService;
import com.schoolhealth.schoolmedical.service.Notification.UserNotificationService;
import com.schoolhealth.schoolmedical.service.medicalevent.MedicalEventServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class MedicalEventServiceTest {

    @InjectMocks
    private MedicalEventServiceImpl medicalEventService;

    @Mock
    private MedicalEventRepository medicalEventRepository;

    @Mock
    private PupilRepo pupilRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private MedicationRepository medicationRepository;

    @Mock
    private MedicalEventMapper medicalEventMapper;

    @Mock
    private UserNotificationService userNotificationService;

    @Mock
    private FCMService fcmService;

    private CreateMedicalEventRequest createRequest;
    private Pupil pupil;
    private User schoolNurse;
    private User parent;
    private MedicalEvent medicalEvent;
    private MedicalEventResponse medicalEventResponse;
    private Equipment equipment;
    private Medication medication;

    @BeforeEach
    void setUp() {
        // Setup pupil
        pupil = new Pupil();
        pupil.setPupilId("pupil123");
        pupil.setFirstName("John");
        pupil.setLastName("Doe");
        pupil.setParents(new ArrayList<>());

        // Setup school nurse
        schoolNurse = new User();
        schoolNurse.setUserId("nurse123");
        schoolNurse.setRole(Role.SCHOOL_NURSE);
        schoolNurse.setFirstName("Jane");
        schoolNurse.setLastName("Smith");

        // Setup parent
        parent = new User();
        parent.setUserId("parent123");
        parent.setRole(Role.PARENT);
        parent.setFirstName("Mike");
        parent.setLastName("Doe");
        pupil.getParents().add(parent);

        // Setup equipment - using correct property name
        equipment = new Equipment();
        equipment.setEquipmentId(1L);
        equipment.setName("Thermometer");

        // Setup medication - using correct property name
        medication = new Medication();
        medication.setMedicationId(1L);
        medication.setName("Paracetamol");

        // Setup create request - using correct MedicalEventStatus enum
        createRequest = new CreateMedicalEventRequest();
        createRequest.setPupilId("pupil123");
        createRequest.setInjuryDescription("Student has a headache");
        createRequest.setTreatmentDescription("Given paracetamol and rest");
        createRequest.setDetailedInformation("Student felt better after treatment");
        createRequest.setStatus(MedicalEventStatus.HIGH);
        createRequest.setEquipmentIds(List.of(1L));
        createRequest.setMedicationIds(List.of(1L));

        // Setup medical event
        medicalEvent = new MedicalEvent();
        medicalEvent.setMedicalEventId(1L);
        medicalEvent.setPupil(pupil);
        medicalEvent.setSchoolNurse(schoolNurse);
        medicalEvent.setInjuryDescription("Student has a headache");
        medicalEvent.setTreatmentDescription("Given paracetamol and rest");
        medicalEvent.setDetailedInformation("Student felt better after treatment");
        medicalEvent.setStatus(MedicalEventStatus.HIGH);
        medicalEvent.setDateTime(LocalDateTime.now());
        medicalEvent.setIsActive(true);
        medicalEvent.setEquipmentUsed(List.of(equipment));
        medicalEvent.setMedicationUsed(List.of(medication));

        // Setup response
        medicalEventResponse = new MedicalEventResponse();
        medicalEventResponse.setMedicalEventId(1L);
        medicalEventResponse.setInjuryDescription("Student has a headache");
        medicalEventResponse.setTreatmentDescription("Given paracetamol and rest");
        medicalEventResponse.setStatus(MedicalEventStatus.HIGH);
    }

    // ================== createMedicalEvent Tests ==================

    @Test
    void testCreateMedicalEvent_Success() {
        // Mock the repository calls that the service actually makes
        when(pupilRepository.findByPupilId("pupil123")).thenReturn(Optional.of(pupil));
        when(userRepository.findById("nurse123")).thenReturn(Optional.of(schoolNurse));
        when(equipmentRepository.findByEquipmentIdInAndIsActiveTrue(List.of(1L))).thenReturn(List.of(equipment));
        when(medicationRepository.findByMedicationIdInAndIsActiveTrue(List.of(1L))).thenReturn(List.of(medication));
        when(medicalEventMapper.toEntity(any(CreateMedicalEventRequest.class))).thenReturn(medicalEvent);
        when(medicalEventRepository.save(any(MedicalEvent.class))).thenReturn(medicalEvent);
        when(medicalEventMapper.toResponse(any(MedicalEvent.class))).thenReturn(medicalEventResponse);
        when(userNotificationService.saveAllUserNotifications(anyList())).thenReturn(anyList());

        MedicalEventResponse result = medicalEventService.createMedicalEvent(createRequest, "nurse123");

        assertNotNull(result);
        assertEquals(1L, result.getMedicalEventId());
        assertEquals(MedicalEventStatus.HIGH, result.getStatus());
        verify(medicalEventRepository, times(1)).save(any(MedicalEvent.class));
        verify(userNotificationService, times(1)).saveAllUserNotifications(anyList());
    }

    @Test
    void testCreateMedicalEvent_WithoutEquipmentAndMedication() {
        createRequest.setEquipmentIds(null);
        createRequest.setMedicationIds(null);
        medicalEvent.setEquipmentUsed(Collections.emptyList());
        medicalEvent.setMedicationUsed(Collections.emptyList());

        when(pupilRepository.findByPupilId("pupil123")).thenReturn(Optional.of(pupil));
        when(userRepository.findById("nurse123")).thenReturn(Optional.of(schoolNurse));
        when(medicalEventMapper.toEntity(any(CreateMedicalEventRequest.class))).thenReturn(medicalEvent);
        when(medicalEventRepository.save(any(MedicalEvent.class))).thenReturn(medicalEvent);
        when(medicalEventMapper.toResponse(any(MedicalEvent.class))).thenReturn(medicalEventResponse);
        when(userNotificationService.saveAllUserNotifications(anyList())).thenReturn(anyList());

        MedicalEventResponse result = medicalEventService.createMedicalEvent(createRequest, "nurse123");

        assertNotNull(result);
        verify(equipmentRepository, never()).findByEquipmentIdInAndIsActiveTrue(anyList());
        verify(medicationRepository, never()).findByMedicationIdInAndIsActiveTrue(anyList());
    }

    @Test
    void testCreateMedicalEvent_InvalidMedicationIds() {
        when(pupilRepository.findByPupilId("pupil123")).thenReturn(Optional.of(pupil));
        when(userRepository.findById("nurse123")).thenReturn(Optional.of(schoolNurse));
        when(equipmentRepository.findByEquipmentIdInAndIsActiveTrue(List.of(1L))).thenReturn(List.of(equipment));
        when(medicationRepository.findByMedicationIdInAndIsActiveTrue(List.of(1L))).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () ->
            medicalEventService.createMedicalEvent(createRequest, "nurse123"));
    }

    @Test
    void testCreateMedicalEvent_PupilNotFound() {
        when(pupilRepository.findByPupilId("pupil123")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
            medicalEventService.createMedicalEvent(createRequest, "nurse123"));
    }

    @Test
    void testCreateMedicalEvent_SchoolNurseNotFound() {
        when(pupilRepository.findByPupilId("pupil123")).thenReturn(Optional.of(pupil));
        when(userRepository.findById("nurse123")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
            medicalEventService.createMedicalEvent(createRequest, "nurse123"));
    }

    @Test
    void testCreateMedicalEvent_InvalidEquipmentIds() {
        when(pupilRepository.findByPupilId("pupil123")).thenReturn(Optional.of(pupil));
        when(userRepository.findById("nurse123")).thenReturn(Optional.of(schoolNurse));
        when(equipmentRepository.findByEquipmentIdInAndIsActiveTrue(List.of(1L))).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () ->
            medicalEventService.createMedicalEvent(createRequest, "nurse123"));
    }



    // ================== getMedicalEventsForParent Tests ==================

    @Test
    void testGetMedicalEventsForParentByYear_Success() {
        when(medicalEventRepository.findByParentIdAndYearWithRelationships("parent123", 2024))
            .thenReturn(List.of(medicalEvent));
        when(medicalEventMapper.toResponse(any(MedicalEvent.class))).thenReturn(medicalEventResponse);

        List<MedicalEventResponse> result = medicalEventService.getMedicalEventsForParentByYear("parent123", 2024);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getMedicalEventId());
    }

    @Test
    void testGetMedicalEventsForParentByYear_NoEventsFound() {
        when(medicalEventRepository.findByParentIdAndYearWithRelationships("parent123", 2024))
            .thenReturn(Collections.emptyList());

        List<MedicalEventResponse> result = medicalEventService.getMedicalEventsForParentByYear("parent123", 2024);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetMedicalEventsForParentByYear_WithNullEquipmentAndMedication() {
        medicalEvent.setEquipmentUsed(null);
        medicalEvent.setMedicationUsed(null);

        when(medicalEventRepository.findByParentIdAndYearWithRelationships("parent123", 2024))
            .thenReturn(List.of(medicalEvent));
        when(medicalEventMapper.toResponse(any(MedicalEvent.class))).thenReturn(medicalEventResponse);

        List<MedicalEventResponse> result = medicalEventService.getMedicalEventsForParentByYear("parent123", 2024);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    // ================== getMedicalEventsByPupilId Tests ==================

    @Test
    void testGetMedicalEventsByPupilId_Success_SchoolNurse() {
        schoolNurse.setRole(Role.SCHOOL_NURSE);
        when(userRepository.findById("nurse123")).thenReturn(Optional.of(schoolNurse));
        when(medicalEventRepository.findByPupilIdWithRelationships("pupil123"))
            .thenReturn(List.of(medicalEvent));
        when(medicalEventMapper.toResponse(any(MedicalEvent.class))).thenReturn(medicalEventResponse);

        List<MedicalEventResponse> result = medicalEventService.getMedicalEventsByPupilId("pupil123", "nurse123");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getMedicalEventId());
        verify(pupilRepository, never()).findById(anyString()); // Should not validate parent access for nurse
    }



    @Test
    void testGetMedicalEventsByPupilId_UserNotFound() {
        when(userRepository.findById("unknown")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
            medicalEventService.getMedicalEventsByPupilId("pupil123", "unknown"));
    }

    @Test
    void testGetMedicalEventsByPupilId_NoEventsFound() {
        when(userRepository.findById("nurse123")).thenReturn(Optional.of(schoolNurse));
        when(medicalEventRepository.findByPupilIdWithRelationships("pupil123"))
            .thenReturn(Collections.emptyList());

        List<MedicalEventResponse> result = medicalEventService.getMedicalEventsByPupilId("pupil123", "nurse123");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetMedicalEventsByPupilId_ParentAccessDenied() {
        User unauthorizedParent = new User();
        unauthorizedParent.setUserId("unauthorized123");
        unauthorizedParent.setRole(Role.PARENT);

        when(userRepository.findById("unauthorized123")).thenReturn(Optional.of(unauthorizedParent));
        when(pupilRepository.findById("pupil123")).thenReturn(Optional.of(pupil));

        assertThrows(NotFoundException.class, () ->
            medicalEventService.getMedicalEventsByPupilId("pupil123", "unauthorized123"));
    }

    @Test
    void testGetMedicalEventsByPupilId_PupilNotFoundForParentValidation() {
        when(userRepository.findById("parent123")).thenReturn(Optional.of(parent));
        when(pupilRepository.findById("pupil123")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
            medicalEventService.getMedicalEventsByPupilId("pupil123", "parent123"));
    }

    @Test
    void testGetMedicalEventsByPupilId_WithNullEquipmentAndMedication() {
        medicalEvent.setEquipmentUsed(null);
        medicalEvent.setMedicationUsed(null);

        when(userRepository.findById("nurse123")).thenReturn(Optional.of(schoolNurse));
        when(medicalEventRepository.findByPupilIdWithRelationships("pupil123"))
            .thenReturn(List.of(medicalEvent));
        when(medicalEventMapper.toResponse(any(MedicalEvent.class))).thenReturn(medicalEventResponse);

        List<MedicalEventResponse> result = medicalEventService.getMedicalEventsByPupilId("pupil123", "nurse123");

        assertNotNull(result);
        assertEquals(1, result.size());
    }



}
