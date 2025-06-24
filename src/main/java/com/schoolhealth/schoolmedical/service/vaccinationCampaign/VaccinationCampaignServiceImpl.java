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
import com.schoolhealth.schoolmedical.model.dto.response.VaccinationCampaignResponse;
import com.schoolhealth.schoolmedical.model.mapper.VaccinationCampaignMapper;
import com.schoolhealth.schoolmedical.repository.DiseaseRepo;
import com.schoolhealth.schoolmedical.repository.NotificationRepo;
import com.schoolhealth.schoolmedical.repository.PupilRepo;
import com.schoolhealth.schoolmedical.repository.VaccinationCampaignRepo;
import com.schoolhealth.schoolmedical.repository.VaccinationConsentFormRepo;
import com.schoolhealth.schoolmedical.repository.VaccineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VaccinationCampaignServiceImpl implements  VaccinationCampaignService {
    private final VaccinationCampaignRepo vaccinationCampaignRepo;
    private final VaccineRepository vaccineRepository;
    private final DiseaseRepo diseaseRepo;
    private final VaccinationCampaignMapper mapper;
    private final PupilRepo pupilRepo;
    private final VaccinationConsentFormRepo consentFormRepo;
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
        VaccinationCampagin campaign = vaccinationCampaignRepo.findById(campaignId)
                .orElseThrow(() -> new NotFoundException("Campaign not found with id: " + campaignId));

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
        VaccinationCampagin campaign = vaccinationCampaignRepo.findById(campaignId)
                .orElseThrow(() -> new NotFoundException("Campaign not found with id: " + campaignId));

        if (campaign.getStatus() != VaccinationCampaignStatus.PENDING) {
            throw new IllegalStateException("Campaign can only be published when in PENDING status.");
        }

        // 2. Find eligible pupils
        List<Pupil> eligiblePupils = pupilRepo.findPupilsNeedingVaccination(
                campaign.getDisease().getDiseaseId(),
                campaign.getDoseNumber()
        );

        List<VaccinationConsentForm> newConsentForms = new ArrayList<>();
        List<UserNotification> newNotifications = new ArrayList<>();

        // 3. Create Consent Forms and Notifications for each pupil
        for (Pupil pupil : eligiblePupils) {
            // Create Consent Form
            VaccinationConsentForm consentForm = VaccinationConsentForm.builder()
                    .campaign(campaign)
                    .pupil(pupil)
                    .vaccine(campaign.getVaccine())
                    .doseNumber(campaign.getDoseNumber())
                    .status(ConsentFormStatus.WAITING)
                    .isActive(true)
                    .build();
            newConsentForms.add(consentForm);

            // Create Notifications for parents
            for (User parent : pupil.getParents()) {
                String message = String.format(
                        "Thông báo về chiến dịch tiêm chủng '%s' cho học sinh %s %s.",
                        campaign.getTitleCampaign(),
                        pupil.getLastName(),
                        pupil.getFirstName()
                );

                UserNotification notification = UserNotification.builder()
                        .user(parent)
                        .message(message)
                        .typeNotification(TypeNotification.VACCINATION_CAMPAIGN)
                        .sourceId(campaignId)
                        .isRead(false)
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
        VaccinationCampagin campaign = vaccinationCampaignRepo.findById(campaignId)
                .orElseThrow(() -> new NotFoundException("Campaign not found with id: " + campaignId));

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
        List<VaccinationCampagin> campaigns = vaccinationCampaignRepo.findAll();
        return campaigns.stream()
                .map(mapper::toVaccinationCampaignResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public VaccinationCampaignResponse getCampaignById(Long campaignId) {
        VaccinationCampagin campaign = vaccinationCampaignRepo.findById(campaignId)
                .orElseThrow(() -> new NotFoundException("Campaign not found with id: " + campaignId));

        return mapper.toVaccinationCampaignResponse(campaign);
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
                case INJECTED -> statusMessage = "đã được tiêm chủng thành công";
                case NO_SHOW -> statusMessage = "đã vắng mặt trong ngày tiêm chủng";
                case APPROVED -> statusMessage = "đã được đồng ý nhưng chưa thực hiện tiêm";
                case REJECTED -> statusMessage = "đã bị từ chối bởi phụ huynh";
                default -> statusMessage = "chưa có phản hồi từ phụ huynh";
            }

            String message = String.format(
                    "Chiến dịch tiêm chủng '%s' đã hoàn thành. Học sinh %s %s %s.",
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
                        .isRead(false)
                        .build();
                completionNotifications.add(notification);
            }
        }

        // Save all completion notifications
        notificationRepo.saveAll(completionNotifications);
    }




}
