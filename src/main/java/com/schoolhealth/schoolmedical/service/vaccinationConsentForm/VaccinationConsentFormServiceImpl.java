package com.schoolhealth.schoolmedical.service.vaccinationConsentForm;

import com.schoolhealth.schoolmedical.entity.Disease;
import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.entity.User;
import com.schoolhealth.schoolmedical.entity.VaccinationCampagin;
import com.schoolhealth.schoolmedical.entity.VaccinationConsentForm;

import com.schoolhealth.schoolmedical.entity.Vaccine;
import com.schoolhealth.schoolmedical.entity.enums.ConsentFormStatus;
import com.schoolhealth.schoolmedical.entity.enums.GradeLevel;
import com.schoolhealth.schoolmedical.entity.enums.VaccinationCampaignStatus;
import com.schoolhealth.schoolmedical.exception.EntityNotFoundException;
import com.schoolhealth.schoolmedical.model.dto.request.VaccinationConsentFormRequest;
import com.schoolhealth.schoolmedical.model.dto.response.PupilApprovedInfo;
import com.schoolhealth.schoolmedical.model.dto.response.PupilsApprovedByGradeResponse;
import com.schoolhealth.schoolmedical.model.dto.response.VaccinationConsentFormResponse;
import com.schoolhealth.schoolmedical.repository.PupilRepo;
import com.schoolhealth.schoolmedical.repository.UserRepository;
import com.schoolhealth.schoolmedical.repository.VaccinationCampaignRepo;
import com.schoolhealth.schoolmedical.repository.VaccinationConsentFormRepo;
import com.schoolhealth.schoolmedical.repository.VaccinationHistoryRepo;
import com.schoolhealth.schoolmedical.service.vaccinationHistory.VaccinationHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class VaccinationConsentFormServiceImpl implements VaccinationConsentFormService {

    private final VaccinationConsentFormRepo consentFormRepo;
    private final VaccinationCampaignRepo vaccinationCampaignRepo;
    private final PupilRepo pupilRepo;
    private final UserRepository userRepository;
    private final VaccinationHistoryService vaccinationHistoryService; // Added missing dependency
    private final VaccinationHistoryRepo vaccinationHistoryRepo;

    @Override
    public VaccinationConsentFormResponse parentRespond(Long formId, String parentUserId, VaccinationConsentFormRequest request) {
        log.info("Parent {} responding to consent form {}", parentUserId, formId);

        // Validate parent permission
        VaccinationConsentForm consentForm = validateParentPermission(formId, parentUserId);

        // Business Rule: Parents can only respond when campaign is PUBLISHED
        // Once campaign moves to IN_PROGRESS, responses are locked
        if (!consentForm.getCampaign().getStatus().equals(VaccinationCampaignStatus.PUBLISHED)) {
            throw new IllegalStateException("Cannot respond: Campaign is not in PUBLISHED status. Responses are locked.");
        }

        // Business Rule: Responses must be submitted before deadline
        if (LocalDateTime.now().isAfter(consentForm.getCampaign().getFormDeadline().atTime(LocalTime.MAX))) {
            throw new IllegalStateException("Cannot respond: Response deadline has passed");
        }

        // Validate response options - parents can only choose APPROVED or REJECTED
        if (request.getStatus() != ConsentFormStatus.APPROVED &&
                request.getStatus() != ConsentFormStatus.REJECTED) {
            throw new IllegalArgumentException("Invalid response: Parents can only respond with APPROVED or REJECTED");
        }

        // Business Rule: Parents can freely toggle between REJECTED ↔ APPROVED before deadline
        // Default status is REJECTED, so parents must actively approve to participate
        consentForm.setStatus(request.getStatus());
        consentForm.setRespondedAt(LocalDateTime.now());

        VaccinationConsentForm savedForm = consentFormRepo.save(consentForm);
        log.info("Parent response updated successfully for form {} - Status: {}", formId, request.getStatus());

        return mapToResponse(savedForm);
    }

    @Override
    public VaccinationConsentFormResponse nurseUpdateStatus(Long formId, VaccinationConsentFormRequest request) {
        log.info("Nurse updating status for consent form {}", formId);

        VaccinationConsentForm consentForm = consentFormRepo.findById(formId)
                .orElseThrow(() -> new EntityNotFoundException("VaccinationConsentForm", "id", formId));

        // Check if campaign is in progress
        if (!consentForm.getCampaign().getStatus().equals(VaccinationCampaignStatus.IN_PROGRESS)) {
            throw new IllegalStateException("Campaign must be in IN_PROGRESS status for nurse updates");
        }

        // Validate status - nurse can only update to INJECTED or NO_SHOW from APPROVED
        if (consentForm.getStatus() != ConsentFormStatus.APPROVED) {
            throw new IllegalStateException("Can only update status from APPROVED");
        }

        if (request.getStatus() != ConsentFormStatus.INJECTED &&
                request.getStatus() != ConsentFormStatus.NO_SHOW) {
            throw new IllegalArgumentException("Nurse can only update to INJECTED or NO_SHOW");
        }

        // Update consent form
        consentForm.setStatus(request.getStatus());
        VaccinationConsentForm savedForm = consentFormRepo.save(consentForm);

        // If injected, create vaccination history record
        if (request.getStatus() == ConsentFormStatus.INJECTED) {
            vaccinationHistoryService.createFromCampaign(consentForm);
            log.info("Vaccination history created for pupil {} in campaign {}",
                    consentForm.getPupil().getPupilId(), consentForm.getCampaign().getCampaignId());
        }

        log.info("Nurse update completed for form {}", formId);
        return mapToResponse(savedForm);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VaccinationConsentFormResponse> getApprovedPupilsByCampaign(Long campaignId) {
        log.info("Getting approved pupils for campaign {}", campaignId);

        List<VaccinationConsentForm> approvedForms = consentFormRepo
                .findByCampaignIdAndStatusOrderByGradeAndName(campaignId, ConsentFormStatus.APPROVED);

        return approvedForms.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VaccinationConsentFormResponse> getMyConsentForms(String parentUserId) {
        log.info("Getting consent forms for parent {}", parentUserId);

        // Use the method that filters by current year instead of all years
        List<Pupil> children = pupilRepo.getAllPupilsByParent(parentUserId);

        return children.stream()
                .flatMap(pupil -> {
                    List<VaccinationConsentForm> activeConsentForms = consentFormRepo.findByPupilAndIsActiveTrue(pupil);

                    // Apply multiple filters to determine which consent forms to show
                    return activeConsentForms.stream()
                            .filter(form -> {
                                // Filter 1: Exclude campaigns that are COMPLETED
                                // Parents don't need to see completed campaigns
                                VaccinationCampaignStatus campaignStatus = form.getCampaign().getStatus();
                                if (campaignStatus == VaccinationCampaignStatus.COMPLETED) {
                                    return false; // Exclude completed campaigns
                                }

                                // Filter 2: Exclude diseases that have been fully vaccinated
                                Disease disease = form.getCampaign().getDisease();
                                int requiredDoses = disease.getDoseQuantity(); // Use disease's max doses, not vaccine's

                                // Get completed doses for this pupil and disease (not vaccine)
                                int completedDoses = vaccinationHistoryRepo.countByPupilAndDiseaseAndIsActiveTrue(pupil, disease);

                                // Include only if completed doses are less than required
                                return completedDoses < requiredDoses;
                            });
                })
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VaccinationConsentFormResponse> getConsentFormsByCampaignAndStatus(Long campaignId, String status) {
        log.info("Getting consent forms for campaign {} with status {}", campaignId, status);

        VaccinationCampagin campaign = vaccinationCampaignRepo.findById(campaignId)
                .orElseThrow(() -> new EntityNotFoundException("VaccinationCampaign", "id", campaignId));

        ConsentFormStatus formStatus = ConsentFormStatus.valueOf(status.toUpperCase());

        List<VaccinationConsentForm> forms = consentFormRepo.findByCampaignAndStatus(campaign, formStatus);

        return forms.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

//    @Override
//    public int updateExpiredConsentForms() {
//        log.info("Starting automatic update of expired consent forms");
//
//        List<VaccinationConsentForm> expiredForms = consentFormRepo
//                .findExpiredWaitingForms(ConsentFormStatus.WAITING, LocalDateTime.now());
//
//        int updatedCount = 0;
//        for (VaccinationConsentForm form : expiredForms) {
//            form.setStatus(ConsentFormStatus.REJECTED);
//            form.setRespondedAt(LocalDateTime.now());
//            consentFormRepo.save(form);
//            updatedCount++;
//        }
//
//        log.info("Updated {} expired consent forms from WAITING to REJECTED", updatedCount);
//        return updatedCount;
//    }
    // Removed: updateExpiredConsentForms() method
    // Reason: With new business logic, default status is REJECTED, so no automatic expiry handling needed

    @Override
    @Transactional(readOnly = true)
    public PupilsApprovedByGradeResponse getPupilsApprovedByGrade(Long campaignId) {
        log.info("Getting pupils approved by grade for campaign {}", campaignId);

        List<VaccinationConsentForm> approvedForms = consentFormRepo
                .findByCampaignIdAndStatusOrderByGradeAndName(campaignId, ConsentFormStatus.APPROVED);

        List<PupilApprovedInfo> pupilInfos = approvedForms.stream()
                .map(this::mapToPupilApprovedInfo)
                .collect(Collectors.toList());

        return PupilsApprovedByGradeResponse.builder()
                .getPupilsApprovedByGrade(pupilInfos)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PupilsApprovedByGradeResponse getPupilsApprovedBySpecificGrade(Long campaignId, GradeLevel gradeLevel) {
        log.info("Getting pupils approved by specific grade {} for campaign {}", gradeLevel, campaignId);

        List<VaccinationConsentForm> approvedForms = consentFormRepo
                .findByCampaignIdAndStatusAndGradeLevelOrderByName(campaignId, ConsentFormStatus.APPROVED, gradeLevel);

        List<PupilApprovedInfo> pupilInfos = approvedForms.stream()
                .map(this::mapToPupilApprovedInfo)
                .collect(Collectors.toList());

        return PupilsApprovedByGradeResponse.builder()
                .getPupilsApprovedByGrade(pupilInfos)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public VaccinationConsentFormResponse getConsentFormById(Long formId) {
        log.info("Getting consent form by ID: {}", formId);

        // Validate input
        if (formId == null || formId <= 0) {
            throw new IllegalArgumentException("Invalid consent form ID: " + formId);
        }

        // Find consent form by ID with optimized query
        VaccinationConsentForm consentForm = consentFormRepo.findById(formId)
                .orElseThrow(() -> new EntityNotFoundException("VaccinationConsentForm", "id", formId));

        // Check if consent form is active
        if (!consentForm.isActive()) {
            throw new EntityNotFoundException("VaccinationConsentForm", "id", formId);
        }

        // Map to response DTO - reuse existing mapping logic for consistency
        return mapToResponse(consentForm);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VaccinationConsentFormResponse> getAllConsentFormsByCampaign(Long campaignId) {
        log.info("Getting all active consent forms for campaign {}", campaignId);

        // Validate input
        if (campaignId == null || campaignId <= 0) {
            throw new IllegalArgumentException("Invalid campaign ID: " + campaignId);
        }

        // Validate campaign exists first (for better error handling)
        if (!vaccinationCampaignRepo.existsById(campaignId)) {
            throw new EntityNotFoundException("VaccinationCampaign", "id", campaignId);
        }

        // Get all active consent forms for this campaign with optimized query
        List<VaccinationConsentForm> consentForms = consentFormRepo
                .findAllActiveByCampaignIdOrderByGradeAndName(campaignId);

        // Map to response DTOs - no need to filter as query already filters active
        return consentForms.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private VaccinationConsentForm validateParentPermission(Long formId, String parentUserId) {
        VaccinationConsentForm consentForm = consentFormRepo.findById(formId)
                .orElseThrow(() -> new EntityNotFoundException("VaccinationConsentForm", "id", formId));

        User parent = userRepository.findById(parentUserId)
                .orElseThrow(() -> new EntityNotFoundException("User", "id", parentUserId));

        // Check if this parent is the parent of the pupil
        List<Pupil> children = pupilRepo.findByParent(parent);
        boolean isParentOfPupil = children.stream()
                .anyMatch(child -> child.getPupilId().equals(consentForm.getPupil().getPupilId()));

        if (!isParentOfPupil) {
            throw new IllegalStateException("Parent does not have permission to respond to this consent form");
        }

        return consentForm;
    }

    private VaccinationConsentFormResponse mapToResponse(VaccinationConsentForm form) {
        // Extract campaign, disease, vaccine, and pupil information
        VaccinationCampagin campaign = form.getCampaign();
        Disease disease = campaign.getDisease();
        Vaccine vaccine = form.getVaccine();
        Pupil pupil = form.getPupil();

        // Calculate current completed doses for this disease from all sources
        // This includes both campaign injections and approved parent declarations
        int currDoseNumber = vaccinationHistoryRepo.countByPupilAndDiseaseAndIsActiveTrue(pupil, disease);

        // Get pupil's grade level
        String gradeLevel = pupil.getPupilGrade().stream()
                .map(pg -> pg.getGrade().getGradeLevel().toString())
                .findFirst()
                .orElse("Unknown");
        return VaccinationConsentFormResponse.builder()
                .consentFormId(form.getConsentFormId())
                .respondedAt(form.getRespondedAt())
                .status(form.getStatus())
                .formDeadline(campaign.getFormDeadline().atTime(LocalTime.MAX))

                // Campaign information
                .campaignId(campaign.getCampaignId())
                .campaignName(campaign.getTitleCampaign())

                // Disease information
                .diseaseId(disease.getDiseaseId())
                .diseaseName(disease.getName())
                .doseNumber(disease.getDoseQuantity())       // Total doses required
                .currDoseNumber(currDoseNumber)              // Current completed doses

                // Vaccine information
                .vaccineId(vaccine.getVaccineId())
                .vaccineName(vaccine.getName())

                // Pupil information
                .pupilId(pupil.getPupilId())
                .pupilName(pupil.getLastName() + " " + pupil.getFirstName())
                .gradeLevel(gradeLevel)
                .build();
    }
    private PupilApprovedInfo mapToPupilApprovedInfo(VaccinationConsentForm form) {
        Pupil pupil = form.getPupil();

        // Get class ID - assuming first pupilGrade entry
        Long classId = pupil.getPupilGrade().stream()
                .findFirst()
                .map(pg -> pg.getPupilGradeId().getGradeId())
                .orElse(null);

        // Get grade level
        Integer grade = pupil.getPupilGrade().stream()
                .findFirst()
                .map(pg -> pg.getGrade().getGradeLevel().ordinal() + 1)
                .orElse(1);

        return PupilApprovedInfo.builder()
                .pupilId(pupil.getPupilId())
                .classId(classId)
                .dateOfBirth(pupil.getBirthDate())
                .gender(String.valueOf(pupil.getGender()))
                .lastName(pupil.getLastName())
                .firstName(pupil.getFirstName())
                .avatar(pupil.getAvatar())
                .isActive(pupil.isActive())
                .consentFormId(form.getConsentFormId())
                .campaignId(form.getCampaign().getCampaignId())
                .vaccineId(form.getVaccine().getVaccineId())
                .respondedAt(form.getRespondedAt())
                .status(form.getStatus().toString())
                .Grade(grade)
                .build();
    }
}
