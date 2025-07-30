package com.schoolhealth.schoolmedical.service.vaccinationCampaign;

import com.schoolhealth.schoolmedical.entity.Disease;
import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.entity.User;
import com.schoolhealth.schoolmedical.entity.UserNotification;
import com.schoolhealth.schoolmedical.entity.VaccinationCampagin;
import com.schoolhealth.schoolmedical.entity.VaccinationConsentForm;
import com.schoolhealth.schoolmedical.entity.Vaccine;
import com.schoolhealth.schoolmedical.entity.enums.ConsentFormStatus;
import com.schoolhealth.schoolmedical.entity.enums.TypeNotification;
import com.schoolhealth.schoolmedical.entity.enums.VaccinationCampaignStatus;
import com.schoolhealth.schoolmedical.exception.NotFoundException;
import com.schoolhealth.schoolmedical.model.dto.request.VaccinationCampaignRequest;
import com.schoolhealth.schoolmedical.model.dto.response.AllCampaignInfo;
import com.schoolhealth.schoolmedical.model.dto.response.AllCampaignsResponse;
import com.schoolhealth.schoolmedical.model.dto.response.CampaignDiseaseInfo;
import com.schoolhealth.schoolmedical.model.dto.response.CampaignVaccineInfo;
import com.schoolhealth.schoolmedical.model.dto.response.NewestCampaignInfo;
import com.schoolhealth.schoolmedical.model.dto.response.NewestCampaignResponse;
import com.schoolhealth.schoolmedical.model.dto.response.NewestCampaignWrapper;
import com.schoolhealth.schoolmedical.model.dto.response.VaccinationCampaignResponse;
import com.schoolhealth.schoolmedical.model.mapper.VaccinationCampaignMapper;
import com.schoolhealth.schoolmedical.repository.DiseaseRepo;
import com.schoolhealth.schoolmedical.repository.NotificationRepo;
import com.schoolhealth.schoolmedical.repository.PupilRepo;
import com.schoolhealth.schoolmedical.repository.VaccinationCampaignRepo;
import com.schoolhealth.schoolmedical.repository.VaccinationConsentFormRepo;
import com.schoolhealth.schoolmedical.repository.VaccinationHistoryRepo;
import com.schoolhealth.schoolmedical.repository.VaccineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VaccinationCampaignServiceImpl implements  VaccinationCampaignService {
    private final VaccinationCampaignRepo vaccinationCampaignRepo;
    private final VaccineRepository vaccineRepository;
    private final DiseaseRepo diseaseRepo;
    private final VaccinationCampaignMapper mapper;
    private final PupilRepo pupilRepo;
    private final VaccinationConsentFormRepo consentFormRepo;
    private final VaccinationHistoryRepo vaccinationHistoryRepo;
    private final NotificationRepo notificationRepo;
    @Override
    public VaccinationCampaignResponse createCampaign(VaccinationCampaignRequest request) {
        // Find Vaccine and Disease entities
        Vaccine vaccine = vaccineRepository.findById(request.getVaccineId().longValue())
                .orElseThrow(() -> new NotFoundException("Vaccine not found with id: " + request.getVaccineId()));

        Disease disease = diseaseRepo.findById(request.getDiseaseId().longValue())
                .orElseThrow(() -> new NotFoundException("Disease not found with id: " + request.getDiseaseId()));

        // Map request DTO to entity
        VaccinationCampagin campaign = mapper.toVaccinationCampaign(request);

        // Set initial status and entities
        campaign.setStatus(VaccinationCampaignStatus.PENDING);
        campaign.setActive(true);
        campaign.setVaccine(vaccine);
        campaign.setDisease(disease);

        // Save the new campaign
        VaccinationCampagin savedCampaign = vaccinationCampaignRepo.save(campaign);

        // Map entity to response DTO and return
        return mapper.toVaccinationCampaignResponse(savedCampaign);
    }


    @Override
    public VaccinationCampaignResponse updateCampaign(Long campaignId, VaccinationCampaignRequest request) {
        // Find the campaign by id
        VaccinationCampagin campaign = vaccinationCampaignRepo.findByIdAndIsActiveTrue(campaignId)
                .orElseThrow(() -> new NotFoundException("Active campaign not found with id: " + campaignId));

        // Check if the campaign is in PENDING status
        if (campaign.getStatus() != VaccinationCampaignStatus.PENDING) {
            throw new IllegalStateException("Campaign can only be updated when in PENDING status.");
        }

        // Find Vaccine and Disease entities
        Vaccine vaccine = vaccineRepository.findById(request.getVaccineId().longValue())
                .orElseThrow(() -> new NotFoundException("Vaccine not found with id: " + request.getVaccineId()));

        Disease disease = diseaseRepo.findById(request.getDiseaseId().longValue())
                .orElseThrow(() -> new NotFoundException("Disease not found with id: " + request.getDiseaseId()));

        // Update the campaign entity from the request DTO
        mapper.updateCampaignFromRequest(request, campaign);
        campaign.setVaccine(vaccine);
        campaign.setDisease(disease);

        // Save the updated campaign
        VaccinationCampagin updatedCampaign = vaccinationCampaignRepo.save(campaign);

        // Map the updated entity to a response DTO and return
        return mapper.toVaccinationCampaignResponse(updatedCampaign);
    }

    @Override
    @Transactional
    public void publishCampaign(Long campaignId) {
        // 1. Find the campaign and validate its status
        VaccinationCampagin campaign = vaccinationCampaignRepo.findByIdAndIsActiveTrue(campaignId)
                .orElseThrow(() -> new NotFoundException("Active campaign not found with id: " + campaignId));

        if (campaign.getStatus() != VaccinationCampaignStatus.PENDING) {
            throw new IllegalStateException("Campaign can only be published when in PENDING status.");
        }

        // 2. Find eligible pupils - check pupils who haven't completed all doses for this disease
        List<Pupil> eligiblePupils = pupilRepo.findPupilsNeedingVaccinationCurrentYear(
                campaign.getDisease().getDiseaseId(),
                campaign.getDisease().getDoseQuantity()  // Use disease's max dose count, not campaign's
        );

        List<VaccinationConsentForm> newConsentForms = new ArrayList<>();
        List<UserNotification> newNotifications = new ArrayList<>();

        // 3. Create Consent Forms and Notifications for each pupil
        for (Pupil pupil : eligiblePupils) {
            // Calculate next dose number for this pupil and disease
            int currentDoses = vaccinationHistoryRepo.countByPupilAndDiseaseAndIsActiveTrue(pupil, campaign.getDisease());
            int nextDoseNumber = currentDoses + 1;

            // Create Consent Form with default REJECTED status
            // Parents must actively approve to participate in vaccination
            VaccinationConsentForm consentForm = VaccinationConsentForm.builder()
                    .campaign(campaign)
                    .pupil(pupil)
                    .vaccine(campaign.getVaccine())
                    .status(ConsentFormStatus.REJECTED)
                    .isActive(true)
                    .build();
            newConsentForms.add(consentForm);

            // Create Notifications for parents
            for (User parent : pupil.getParents()) {
                String message = String.format(
                        "Notification regarding the vaccination campaign '%s' for student %s %s.",
                        campaign.getTitleCampaign(),
                        pupil.getLastName(),
                        pupil.getFirstName()
                );

                UserNotification notification = UserNotification.builder()
                        .user(parent)
                        .message(message)
                        .typeNotification(TypeNotification.VACCINATION_CAMPAIGN)
                        .sourceId(campaignId)
                        .active(false)
                        .build();
                newNotifications.add(notification);
                // Here you would also call a real-time notification service like FCM
            }
        }

        // 4. Save new entities to the database
        consentFormRepo.saveAll(newConsentForms);
        notificationRepo.saveAll(newNotifications);

        // 5. Update campaign status
        campaign.setStatus(VaccinationCampaignStatus.PUBLISHED);
        vaccinationCampaignRepo.save(campaign);
    }

    @Override
    @Transactional
    public void updateStatus(Long campaignId, VaccinationCampaignStatus newStatus) {
        // 1. Find the campaign
        VaccinationCampagin campaign = vaccinationCampaignRepo.findByIdAndIsActiveTrue(campaignId)
                .orElseThrow(() -> new NotFoundException("Active campaign not found with id: " + campaignId));

        VaccinationCampaignStatus currentStatus = campaign.getStatus();

        // 2. Validate status transition
        if (!isValidStatusTransition(currentStatus, newStatus)) {
            throw new IllegalStateException(
                    String.format("Invalid status transition from %s to %s", currentStatus, newStatus)
            );
        }

        // 3. Update campaign status
        campaign.setStatus(newStatus);
        vaccinationCampaignRepo.save(campaign);

        // 4. Handle completion logic
        if (newStatus == VaccinationCampaignStatus.COMPLETED) {
            notifyParentsOnCompletion(campaign);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<VaccinationCampaignResponse> getAllCampaigns() {
        // Use optimized query to prevent N+1 problem
        List<VaccinationCampagin> campaigns = vaccinationCampaignRepo.findAllWithVaccineAndDisease();
        return campaigns.stream()
                .map(mapper::toVaccinationCampaignResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public VaccinationCampaignResponse getCampaignById(Long campaignId) {
        // Use optimized query to prevent N+1 problem
        VaccinationCampagin campaign = vaccinationCampaignRepo.findByIdWithVaccineAndDisease(campaignId)
                .orElseThrow(() -> new NotFoundException("Campaign not found with id: " + campaignId));

        return mapper.toVaccinationCampaignResponse(campaign);
    }

    @Override
    @Transactional(readOnly = true)
    public NewestCampaignResponse getNewestCampaign() {
        // Find the newest published campaign with optimized query
        VaccinationCampagin newestCampaign = vaccinationCampaignRepo
                .findTopByStatusWithVaccineAndDisease(VaccinationCampaignStatus.PUBLISHED)
                .orElse(null);

        if (newestCampaign == null) {
            // Return empty response if no published campaign found
            return NewestCampaignResponse.builder()
                    .newest_vaccination_campaign(new ArrayList<>())
                    .build();
        }

        // Build disease info
        CampaignDiseaseInfo diseaseInfo = CampaignDiseaseInfo.builder()
                .disease_id(newestCampaign.getDisease().getDiseaseId())
                .name(newestCampaign.getDisease().getName())
                .description(newestCampaign.getDisease().getDescription())
                .isInjectedVaccine(newestCampaign.getDisease().getIsInjectedVaccination())
                .doseQuantity(newestCampaign.getDisease().getDoseQuantity())
                .build();

        // Build vaccine info
        CampaignVaccineInfo vaccineInfo = CampaignVaccineInfo.builder()
                .vaccineId(newestCampaign.getVaccine().getVaccineId())
                .name(newestCampaign.getVaccine().getName())
                .manufacturer(newestCampaign.getVaccine().getManufacturer())
                .recommendedAge(newestCampaign.getVaccine().getRecommendedAge())
                .description(newestCampaign.getVaccine().getDescription())
                .build();

        // Determine implementation status based on dates
        String implementationStatus;
        LocalDate today = LocalDate.now();
        if (newestCampaign.getStartDate().isAfter(today)) {
            implementationStatus = "Pending";
        } else if (newestCampaign.getEndDate().isBefore(today)) {
            implementationStatus = "Completed";
        } else {
            implementationStatus = "In Progress";
        }

        // Build campaign info
        NewestCampaignInfo campaignInfo = NewestCampaignInfo.builder()
                .campaignId(newestCampaign.getCampaignId())
                .disease(diseaseInfo)
                .vaccine(vaccineInfo)
                .notes(newestCampaign.getNotes())
                .consentFormDeadline(newestCampaign.getFormDeadline())
                .startDate(newestCampaign.getStartDate())
                .endDate(newestCampaign.getEndDate())
                .campaignStatus(newestCampaign.getStatus().toString())
                .status(implementationStatus) // Default status as shown in JSON
                .build();

        // Build wrapper
        NewestCampaignWrapper wrapper = NewestCampaignWrapper.builder()
                .campaign(campaignInfo)
                .build();

        // Build final response
        return NewestCampaignResponse.builder()
                .newest_vaccination_campaign(List.of(wrapper))
                .build();
    }

    private void sendCancellationNotificationToParents(VaccinationCampagin campaign) {
        try {
            // Get all consent forms for this campaign to find affected pupils
            List<VaccinationConsentForm> allConsentForms = consentFormRepo.findByCampaign(campaign);
            List<UserNotification> cancellationNotifications = new ArrayList<>();

            String baseMessage = String.format(
                    "NOTICE OF CANCELLATION: The vaccination campaign '%s' has been canceled." +
                            "We apologize for any inconvenience caused. For further information, please contact the school.",
                    campaign.getTitleCampaign()
            );

            for (VaccinationConsentForm consentForm : allConsentForms) {
                Pupil pupil = consentForm.getPupil();

                String personalizedMessage = String.format(
                        "%s\n\nHọc sinh: %s %s",
                        baseMessage,
                        pupil.getLastName(),
                        pupil.getFirstName()
                );

                // Create notifications for all parents of this pupil
                for (User parent : pupil.getParents()) {
                    UserNotification notification = UserNotification.builder()
                            .user(parent)
                            .message(personalizedMessage)
                            .typeNotification(TypeNotification.VACCINATION_CAMPAIGN)
                            .sourceId(campaign.getCampaignId())
                            .active(false) // Unread by default
                            .build();
                    cancellationNotifications.add(notification);
                }
            }

            // Save all cancellation notifications
            notificationRepo.saveAll(cancellationNotifications);

            log.info("Successfully sent cancellation notifications to {} parents for campaign: {}",
                    cancellationNotifications.size(), campaign.getCampaignId());

        } catch (Exception e) {
            log.error("Failed to send cancellation notifications for campaign: {}",
                    campaign.getCampaignId(), e);
            // Don't fail the entire deletion if notification fails
        }
    }
    @Override
    @Transactional
    public void deleteCampaign(Long campaignId, String deletedBy) {
        log.info("Soft deleting vaccination campaign {} by user {}", campaignId, deletedBy);

        // Find the active campaign by id (this automatically filters out deleted campaigns)
        VaccinationCampagin campaign = vaccinationCampaignRepo.findByIdAndIsActiveTrue(campaignId)
                .orElseThrow(() -> new NotFoundException("Active campaign not found with id: " + campaignId));

        // Check if the campaign is in PENDING or PUBLISHED status (only these statuses can be deleted)
        if (campaign.getStatus() != VaccinationCampaignStatus.PENDING &&
                campaign.getStatus() != VaccinationCampaignStatus.PUBLISHED) {
            throw new IllegalStateException("Campaign can only be deleted when in PENDING or PUBLISHED status. Current status: " + campaign.getStatus());
        }

        if (campaign.getStatus() == VaccinationCampaignStatus.PUBLISHED) {
            // Send cancellation notifications to parents before deleting
            sendCancellationNotificationToParents(campaign);
            log.info("Sent cancellation notifications to parents for published campaign: {}", campaignId);

            // Soft delete all consent forms related to this campaign
            deleteConsentFormsForCampaign(campaign);
            log.info("Soft deleted all consent forms for published campaign: {}", campaignId);
        }

        // Soft delete the campaign
        campaign.setActive(false);
        vaccinationCampaignRepo.save(campaign);

        log.info("Soft deleted vaccination campaign with ID: {} (previous status: {})", campaignId, campaign.getStatus());
    }

    @Override
    @Transactional(readOnly = true)
    public AllCampaignsResponse getAllCampaignsEnhanced() {
        // Use optimized query to prevent N+1 problem
        List<VaccinationCampagin> campaigns = vaccinationCampaignRepo.findAllWithVaccineAndDisease();

        List<AllCampaignInfo> campaignInfos = campaigns.stream()
                .map(this::mapToAllCampaignInfo)
                .collect(Collectors.toList());

        return AllCampaignsResponse.builder()
                .getAllVaccinationCampaigns(campaignInfos)
                .build();
    }

    private AllCampaignInfo mapToAllCampaignInfo(VaccinationCampagin campaign) {
        // Determine implementation status based on dates
        String implementationStatus;
        LocalDate today = LocalDate.now();
        if (campaign.getStartDate().isAfter(today)) {
            implementationStatus = "Pending";
        } else if (campaign.getEndDate().isBefore(today)) {
            implementationStatus = "Completed";
        } else {
            implementationStatus = "In Progress";
        }

        return AllCampaignInfo.builder()
                .campaignId(campaign.getCampaignId())
                .diseaseId(campaign.getDisease().getDiseaseId())
                .diseaseName(campaign.getDisease().getName())
                .vaccineId(campaign.getVaccine().getVaccineId())
                .vaccineName(campaign.getVaccine().getName())
                .formDeadline(campaign.getFormDeadline())
                .startDate(campaign.getStartDate())
                .endDate(campaign.getEndDate())
                .notes(campaign.getNotes())
                .status(implementationStatus)
                .build();
    }

    private boolean isValidStatusTransition(VaccinationCampaignStatus current, VaccinationCampaignStatus target) {
        return switch (current) {
            case PENDING -> target == VaccinationCampaignStatus.PUBLISHED;
            case PUBLISHED -> target == VaccinationCampaignStatus.IN_PROGRESS || target == VaccinationCampaignStatus.CANCELED;
            case IN_PROGRESS -> target == VaccinationCampaignStatus.COMPLETED || target == VaccinationCampaignStatus.CANCELED;
            case COMPLETED, CANCELED -> false; // No transitions allowed from these states
        };
    }

    private void notifyParentsOnCompletion(VaccinationCampagin campaign) {
        // Get all consent forms for this campaign
        List<VaccinationConsentForm> allConsentForms = consentFormRepo.findByCampaign(campaign);
        List<UserNotification> completionNotifications = new ArrayList<>();

        for (VaccinationConsentForm consentForm : allConsentForms) {
            Pupil pupil = consentForm.getPupil();
            String statusMessage;

            switch (consentForm.getStatus()) {
                case INJECTED -> statusMessage = "Successfully vaccinated";
                case NO_SHOW -> statusMessage = "Absent on the vaccination day";
                case APPROVED -> statusMessage = "Approved by parent but not yet vaccinated";
                case REJECTED -> statusMessage = "Declined by parent";
                default -> statusMessage = "No response from parent";
            }

            String message = String.format(
                    "The vaccination campaign '%s' has been completed. Student %s %s %s.",
                    campaign.getTitleCampaign(),
                    pupil.getLastName(),
                    pupil.getFirstName(),
                    statusMessage
            );

            // Create notifications for all parents of this pupil
            for (User parent : pupil.getParents()) {
                UserNotification notification = UserNotification.builder()
                        .user(parent)
                        .message(message)
                        .typeNotification(TypeNotification.VACCINATION_CAMPAIGN)
                        .sourceId(campaign.getCampaignId())
                        .active(false)
                        .build();
                completionNotifications.add(notification);
            }
        }

        // Save all completion notifications
        notificationRepo.saveAll(completionNotifications);
    }


    private void deleteConsentFormsForCampaign(VaccinationCampagin campaign) {
        try {
            // Find all consent forms for this campaign
            List<VaccinationConsentForm> consentForms = consentFormRepo.findByCampaign(campaign);

            if (consentForms.isEmpty()) {
                log.info("No consent forms found for campaign: {}", campaign.getCampaignId());
                return;
            }

            // Soft delete all consent forms by setting isActive = false
            consentForms.forEach(form -> form.setActive(false));

            // Batch update all consent forms
            consentFormRepo.saveAll(consentForms);

            log.info("Successfully soft deleted {} consent forms for campaign: {}",
                    consentForms.size(), campaign.getCampaignId());

        } catch (Exception e) {
            log.error("Failed to delete consent forms for campaign: {}",
                    campaign.getCampaignId(), e);
            // Don't fail the entire deletion if consent form cleanup fails
            throw new RuntimeException("Failed to cleanup consent forms for campaign: " + campaign.getCampaignId(), e);
        }
    }

}
