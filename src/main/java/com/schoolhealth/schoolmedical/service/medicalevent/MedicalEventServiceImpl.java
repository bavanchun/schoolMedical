package com.schoolhealth.schoolmedical.service.medicalevent;

import com.schoolhealth.schoolmedical.entity.*;
import com.schoolhealth.schoolmedical.entity.enums.MedicalEventStatus;
import com.schoolhealth.schoolmedical.entity.enums.Role;
import com.schoolhealth.schoolmedical.entity.enums.TypeNotification;
import com.schoolhealth.schoolmedical.exception.NotFoundException;
import com.schoolhealth.schoolmedical.model.dto.request.CreateMedicalEventRequest;
import com.schoolhealth.schoolmedical.model.dto.response.MedicalEventResponse;
import com.schoolhealth.schoolmedical.model.mapper.MedicalEventMapper;
import com.schoolhealth.schoolmedical.repository.*;
import com.schoolhealth.schoolmedical.service.Notification.UserNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MedicalEventServiceImpl implements MedicalEventService {

    private final MedicalEventRepository medicalEventRepository;
    private final PupilRepo pupilRepository;
    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;
    private final MedicationRepository medicationRepository;
    private final MedicalEventMapper medicalEventMapper;
    private final UserNotificationService userNotificationService;

    @Override
    public MedicalEventResponse createMedicalEvent(CreateMedicalEventRequest request, String createdBy) {
        log.info("Creating medical event for pupil {} by user {}", request.getPupilId(), createdBy);

        // Validate and get entities
        Pupil pupil = findPupilById(request.getPupilId());
        User schoolNurse = findUserById(createdBy);

        // Validate equipment and medication IDs
        List<Equipment> equipment = getValidatedEquipment(request.getEquipmentIds());
        List<Medication> medication = getValidatedMedication(request.getMedicationIds());

        // Create medical event entity
        MedicalEvent medicalEvent = medicalEventMapper.toEntity(request);
        medicalEvent.setPupil(pupil);
        medicalEvent.setSchoolNurse(schoolNurse);
        medicalEvent.setDateTime(LocalDateTime.now());
        medicalEvent.setStatus(MedicalEventStatus.LOW); // Default status
        medicalEvent.setIsActive(true);
        medicalEvent.setEquipmentUsed(equipment);
        medicalEvent.setMedicationUsed(medication);

        // Trim text fields
        trimTextFields(medicalEvent);

        MedicalEvent savedEvent = medicalEventRepository.save(medicalEvent);
        log.info("Created medical event with ID: {}", savedEvent.getMedicalEventId());

        // Send notifications to parents
        sendNotificationToParents(savedEvent);

        return mapToResponse(savedEvent);
    }



    @Override
    @Transactional(readOnly = true)
    public MedicalEventResponse getMedicalEventById(Long medicalEventId, String userId) {
        log.info("Getting medical event {} for user {}", medicalEventId, userId);

        MedicalEvent medicalEvent = medicalEventRepository.findByIdWithAllRelationships(medicalEventId)
                .orElseThrow(() -> new NotFoundException("Medical event not found with ID: " + medicalEventId));

        // Check access permissions
        validateUserAccessToMedicalEvent(userId, medicalEvent);

        return mapToResponse(medicalEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MedicalEventResponse> getAllMedicalEvents(Pageable pageable, String search) {
        log.info("Getting all medical events with search: {}", search);

        // Use existing repository methods
        Page<MedicalEvent> medicalEvents = medicalEventRepository.findAllWithBasicRelationships(pageable);

        return medicalEvents.map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalEventResponse> getMedicalEventsByPupilId(String pupilId, String requestingUserId) {
        log.info("Getting medical events for pupil {} requested by user {}", pupilId, requestingUserId);

        User requestingUser = findUserById(requestingUserId);

        // Check access permissions for this pupil
        if (requestingUser.getRole() == Role.PARENT) {
            validateParentAccessToPupil(requestingUserId, pupilId);
        }

        List<MedicalEvent> medicalEvents = medicalEventRepository.findByPupilIdWithRelationships(pupilId);
        return medicalEvents.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalEventResponse> getMedicalEventsByGradeLevel(String gradeLevel, String requestingUserId) {
        log.info("Getting medical events for grade {} requested by user {}", gradeLevel, requestingUserId);

        // Simple query by active status for now
        List<MedicalEvent> medicalEvents = medicalEventRepository.findAll().stream()
                .filter(me -> me.getIsActive())
                .collect(Collectors.toList());

        return medicalEvents.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalEventResponse> getMedicalEventsForParentByYear(String parentId, int year) {
        log.info("Getting medical events for parent {} in year {}", parentId, year);

        List<MedicalEvent> medicalEvents = medicalEventRepository.findByParentIdAndYearWithRelationships(parentId, year);
        return medicalEvents.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMedicalEvent(Long medicalEventId, String deletedBy) {
        log.info("Soft deleting medical event {} by user {}", medicalEventId, deletedBy);

        MedicalEvent medicalEvent = findMedicalEventById(medicalEventId);
        medicalEvent.setIsActive(false);
        medicalEventRepository.save(medicalEvent);

        log.info("Soft deleted medical event with ID: {}", medicalEventId);
    }

    @Override
    @Transactional(readOnly = true)
    public MedicalEventStatistics getMedicalEventStatistics() {
        log.info("Getting medical event statistics");

        // Simple statistics using basic repository methods
        long totalEvents = medicalEventRepository.count();
        long eventsThisMonth = totalEvents; // Simplified for now
        long pendingEvents = 0; // Simplified for now
        long completedEvents = 0; // Simplified for now

        return new MedicalEventStatistics(totalEvents, eventsThisMonth, pendingEvents, completedEvents);
    }

    // Helper Methods
    private Pupil findPupilById(String pupilId) {
        return pupilRepository.findPupilById(pupilId)
                .orElseThrow(() -> new NotFoundException("Pupil not found with ID: " + pupilId));
    }

    private User findUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));
    }

    private MedicalEvent findMedicalEventById(Long medicalEventId) {
        return medicalEventRepository.findById(medicalEventId)
                .filter(MedicalEvent::getIsActive)
                .orElseThrow(() -> new NotFoundException("Medical event not found with ID: " + medicalEventId));
    }

    private List<Equipment> getValidatedEquipment(List<Long> equipmentIds) {
        if (equipmentIds == null || equipmentIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<Equipment> equipment = equipmentRepository.findByEquipmentIdInAndIsActiveTrue(equipmentIds);
        if (equipment.size() != equipmentIds.size()) {
            Set<Long> foundIds = equipment.stream().map(Equipment::getEquipmentId).collect(Collectors.toSet());
            List<Long> missingIds = equipmentIds.stream().filter(id -> !foundIds.contains(id)).collect(Collectors.toList());
            throw new NotFoundException("Equipment not found with IDs: " + missingIds);
        }

        return equipment;
    }

    private List<Medication> getValidatedMedication(List<Long> medicationIds) {
        if (medicationIds == null || medicationIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<Medication> medication = medicationRepository.findByMedicationIdInAndIsActiveTrue(medicationIds);
        if (medication.size() != medicationIds.size()) {
            Set<Long> foundIds = medication.stream().map(Medication::getMedicationId).collect(Collectors.toSet());
            List<Long> missingIds = medicationIds.stream().filter(id -> !foundIds.contains(id)).collect(Collectors.toList());
            throw new NotFoundException("Medication not found with IDs: " + missingIds);
        }

        return medication;
    }

    private void trimTextFields(MedicalEvent medicalEvent) {
        if (medicalEvent.getInjuryDescription() != null) {
            medicalEvent.setInjuryDescription(medicalEvent.getInjuryDescription().trim());
        }
        if (medicalEvent.getTreatmentDescription() != null) {
            medicalEvent.setTreatmentDescription(medicalEvent.getTreatmentDescription().trim());
        }
        if (medicalEvent.getDetailedInformation() != null) {
            medicalEvent.setDetailedInformation(medicalEvent.getDetailedInformation().trim());
        }
    }

    private void validateUserAccessToMedicalEvent(String userId, MedicalEvent medicalEvent) {
        User user = findUserById(userId);

        switch (user.getRole()) {
            case MANAGER, ADMIN, SCHOOL_NURSE -> {
                // Full access
            }
            case PARENT -> {
                // Check if user is parent of the pupil involved in this medical event
                validateParentAccessToPupil(userId, medicalEvent.getPupil().getPupilId());
            }
            default -> throw new NotFoundException("Access denied to medical event");
        }
    }

    private void validateParentAccessToPupil(String parentId, String pupilId) {
        List<Pupil> parentPupils = pupilRepository.findByParent(findUserById(parentId));
        boolean hasAccess = parentPupils.stream()
                .anyMatch(pupil -> pupil.getPupilId().equals(pupilId));

        if (!hasAccess) {
            throw new NotFoundException("Parent does not have access to this pupil's medical events");
        }
    }

    private MedicalEventResponse mapToResponse(MedicalEvent medicalEvent) {
        MedicalEventResponse response = medicalEventMapper.toResponse(medicalEvent);

        // Map school nurse info
        if (medicalEvent.getSchoolNurse() != null) {
            response.setSchoolNurse(medicalEventMapper.toSchoolNurseInfo(medicalEvent.getSchoolNurse()));
        }

        return response;
    }

    private void sendNotificationToParents(MedicalEvent medicalEvent) {
        try {
            String message = String.format(
                    "Medical event recorded for %s %s. Injury: %s. Please check your child's medical records.",
                    medicalEvent.getPupil().getFirstName(),
                    medicalEvent.getPupil().getLastName(),
                    medicalEvent.getInjuryDescription()
            );

            // Create notifications for parents
            List<User> parents = medicalEvent.getPupil().getParents();
            List<UserNotification> notifications = new ArrayList<>();

            for (User parent : parents) {
                UserNotification notification = UserNotification.builder()
                        .message(message)
                        .sourceId(medicalEvent.getMedicalEventId())
                        .typeNotification(TypeNotification.MED_EVENT)
                        .user(parent)
                        .build();
                notifications.add(notification);
            }

            userNotificationService.saveAllUserNotifications(notifications);

            log.info("Sent medical event notification to parents for pupil: {}", medicalEvent.getPupil().getPupilId());
        } catch (Exception e) {
            log.error("Failed to send notification to parents for medical event: {}", medicalEvent.getMedicalEventId(), e);
            // Don't fail the entire operation if notification fails
        }
    }
}
