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
import com.schoolhealth.schoolmedical.service.Notification.FCMService;
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
    private final FCMService fcmService;

    @Override
    public MedicalEventResponse createMedicalEvent(CreateMedicalEventRequest request, String createdBy) {
        log.info("Creating medical event for pupil {} by user {} with status {}",
                request.getPupilId(), createdBy, request.getStatus());

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
        medicalEvent.setStatus(request.getStatus()); // ✅ Use status from request
        medicalEvent.setIsActive(true);
        medicalEvent.setEquipmentUsed(equipment);
        medicalEvent.setMedicationUsed(medication);

        // Trim text fields
        trimTextFields(medicalEvent);

        MedicalEvent savedEvent = medicalEventRepository.save(medicalEvent);
        log.info("Created medical event with ID: {} and status: {}",
                savedEvent.getMedicalEventId(), savedEvent.getStatus());

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

        // Force lazy loading of equipment and medication collections
//        medicalEvent.getEquipmentUsed().size();
//        medicalEvent.getMedicationUsed().size();
        if (medicalEvent.getEquipmentUsed() != null) {
            medicalEvent.getEquipmentUsed().size();
        }
        if (medicalEvent.getMedicationUsed() != null) {
            medicalEvent.getMedicationUsed().size();
        }

        // Check access permissions
        validateUserAccessToMedicalEvent(userId, medicalEvent);

        return mapToResponse(medicalEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalEventResponse> getAllMedicalEvents(String search) {
        log.info("Getting all medical events with search: {}", search);

        List<MedicalEvent> medicalEvents;

        if (StringUtils.hasText(search)) {
            // Search by pupil name, symptoms, treatment, or other relevant fields
            medicalEvents = medicalEventRepository.findBySearchCriteria(search);
        } else {
            // Get all active medical events
            medicalEvents = medicalEventRepository.findAllActiveWithRelationships();
        }

        // Force lazy loading of collections to avoid N+1 issues
        for (MedicalEvent event : medicalEvents) {
//            event.getEquipmentUsed().size();
//            event.getMedicationUsed().size();
//        }
            if (event.getEquipmentUsed() != null) {
                event.getEquipmentUsed().size();
            }
            if (event.getMedicationUsed() != null) {
                event.getMedicationUsed().size();
            }
        }

        return medicalEvents.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
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

        // Load medical events with basic relationships (avoids MultipleBagFetchException)
        List<MedicalEvent> medicalEvents = medicalEventRepository.findByPupilIdWithRelationships(pupilId);

        // For each medical event, explicitly load equipment and medication to avoid N+1
        for (MedicalEvent event : medicalEvents) {
            // Force lazy loading of collections
//            event.getEquipmentUsed().size(); // Trigger lazy loading
//            event.getMedicationUsed().size(); // Trigger lazy loading
            if (event.getEquipmentUsed() != null) {
                event.getEquipmentUsed().size(); // Trigger lazy loading
            }
            if (event.getMedicationUsed() != null) {
                event.getMedicationUsed().size(); // Trigger lazy loading
            }
        }

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

        // For each medical event, explicitly load equipment and medication to avoid N+1
        for (MedicalEvent event : medicalEvents) {
//            event.getEquipmentUsed().size(); // Trigger lazy loading
//            event.getMedicationUsed().size(); // Trigger lazy loading
            if (event.getEquipmentUsed() != null) {
                event.getEquipmentUsed().size(); // Trigger lazy loading
            }
            if (event.getMedicationUsed() != null) {
                event.getMedicationUsed().size(); // Trigger lazy loading
            }
        }

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
        log.debug("Finding pupil with ID: {}", pupilId);
        return pupilRepository.findByPupilId(pupilId)
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
            // Create severity-based message
            String severityText = getSeverityText(medicalEvent.getStatus());
            String title = String.format("Medical Alert - %s Priority", severityText);
            String message = String.format(
                    "Medical event (%s priority) recorded for %s %s.\nInjury: %s\nPlease check your child's medical records for details.",
                    severityText,
                    medicalEvent.getPupil().getFirstName(),
                    medicalEvent.getPupil().getLastName(),
                    medicalEvent.getInjuryDescription()
            );

            // Get parents and create notifications
            List<User> parents = medicalEvent.getPupil().getParents();
            List<UserNotification> notifications = new ArrayList<>();

            for (User parent : parents) {
                UserNotification notification = UserNotification.builder()
                        .message(message)
                        .sourceId(medicalEvent.getMedicalEventId())
                        .typeNotification(TypeNotification.MED_EVENT)
                        .user(parent)
                        .active(false) // Unread by default
                        .build();
                notifications.add(notification);
            }

            // Save notifications to database
            userNotificationService.saveAllUserNotifications(notifications);

            // Send real-time FCM push notifications
            sendRealTimePushNotifications(parents, medicalEvent, title, message);

            log.info("Sent medical event notification to {} parents for pupil: {}",
                    parents.size(), medicalEvent.getPupil().getPupilId());
        } catch (Exception e) {
            log.error("Failed to send notification to parents for medical event: {}",
                    medicalEvent.getMedicalEventId(), e);
            // Don't fail the entire operation if notification fails
        }
    }

    private void sendRealTimePushNotifications(List<User> parents, MedicalEvent medicalEvent, String title, String message) {
        try {
            // Get device tokens from parents
            List<String> tokens = parents.stream()
                    .map(User::getDeviceToken)
                    .filter(token -> token != null && !token.isEmpty())
                    .collect(Collectors.toList());

            if (!tokens.isEmpty()) {
                // Create notification for FCM multicast
                UserNotification fcmNotification = UserNotification.builder()
                        .message(title)
                        .sourceId(medicalEvent.getMedicalEventId())
                        .typeNotification(TypeNotification.MED_EVENT)
                        .build();

                // Send multicast notification
                fcmService.sendMulticastNotification(tokens, fcmNotification);

                log.info("Sent FCM push notifications to {} devices for medical event: {}",
                        tokens.size(), medicalEvent.getMedicalEventId());
            } else {
                log.warn("No valid device tokens found for parents of pupil: {}",
                        medicalEvent.getPupil().getPupilId());
            }
        } catch (Exception e) {
            log.error("Failed to send FCM push notifications for medical event: {}",
                    medicalEvent.getMedicalEventId(), e);
        }
    }

    private String getSeverityText(MedicalEventStatus status) {
        return switch (status) {
            case LOW -> "Low";
            case MEDIUM -> "Medium";
            case HIGH -> "High";
        };
    }
    @Override
    @Transactional(readOnly = true)
    public List<MedicalEventResponse> getMedicalEventsForParent(String parentId) {
        log.info("Getting all medical events for parent {}", parentId);

        List<MedicalEvent> medicalEvents = medicalEventRepository.findByParentIdWithRelationships(parentId);

        // For each medical event, explicitly load equipment and medication to avoid N+1
        for (MedicalEvent event : medicalEvents) {
            if (event.getEquipmentUsed() != null) {
                event.getEquipmentUsed().size(); // Trigger lazy loading
            }
            if (event.getMedicationUsed() != null) {
                event.getMedicationUsed().size(); // Trigger lazy loading
            }
        }

        return medicalEvents.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}
