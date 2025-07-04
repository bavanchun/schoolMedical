package com.schoolhealth.schoolmedical.service.medicalevent;

import com.schoolhealth.schoolmedical.model.dto.request.CreateMedicalEventRequest;
import com.schoolhealth.schoolmedical.model.dto.response.MedicalEventResponse;

import java.util.List;

public interface MedicalEventService {

    /**
     * Create a new medical event
     * Only SCHOOL_NURSE, MANAGER, ADMIN can create
     */
    MedicalEventResponse createMedicalEvent(CreateMedicalEventRequest request, String createdBy);



    /**
     * Get medical event by ID
     * Access control based on user role
     */
    MedicalEventResponse getMedicalEventById(Long medicalEventId, String userId);

    /**
     * Get all medical events (MANAGER/ADMIN only)
     * With optional filtering
     */
    List<MedicalEventResponse> getAllMedicalEvents(String search);

    /**
     * Get medical events by pupil ID (MANAGER/ADMIN/PARENT access)
     * Parents can only access their children's events
     */
    List<MedicalEventResponse> getMedicalEventsByPupilId(String pupilId, String requestingUserId);

    /**
     * Get medical events by grade level (MANAGER/ADMIN only)
     */
    List<MedicalEventResponse> getMedicalEventsByGradeLevel(String gradeLevel, String requestingUserId);

    /**
     * Get medical events for parent's children by year (PARENT only)
     */
    List<MedicalEventResponse> getMedicalEventsForParentByYear(String parentId, int year);

    /**
     * Soft delete a medical event (MANAGER/ADMIN only)
     */
    void deleteMedicalEvent(Long medicalEventId, String deletedBy);

    /**
     * Get medical event statistics (MANAGER/ADMIN only)
     */
    MedicalEventStatistics getMedicalEventStatistics();

    // Inner class for statistics
    class MedicalEventStatistics {
        private long totalEvents;
        private long eventsThisMonth;
        private long pendingEvents;
        private long completedEvents;

        // Constructor, getters, setters
        public MedicalEventStatistics(long totalEvents, long eventsThisMonth, long pendingEvents, long completedEvents) {
            this.totalEvents = totalEvents;
            this.eventsThisMonth = eventsThisMonth;
            this.pendingEvents = pendingEvents;
            this.completedEvents = completedEvents;
        }

        // Getters
        public long getTotalEvents() { return totalEvents; }
        public long getEventsThisMonth() { return eventsThisMonth; }
        public long getPendingEvents() { return pendingEvents; }
        public long getCompletedEvents() { return completedEvents; }
    }
}
