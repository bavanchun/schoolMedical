package com.schoolhealth.schoolmedical.service.vaccinationConsentForm;

import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.entity.User;
import com.schoolhealth.schoolmedical.entity.VaccinationCampagin;
import com.schoolhealth.schoolmedical.entity.VaccinationConsentForm;
import com.schoolhealth.schoolmedical.entity.VaccinationHistory;
import com.schoolhealth.schoolmedical.entity.Vaccine;
import com.schoolhealth.schoolmedical.entity.enums.ConsentFormStatus;
import com.schoolhealth.schoolmedical.entity.enums.VaccinationCampaignStatus;
import com.schoolhealth.schoolmedical.exception.EntityNotFoundException;
import com.schoolhealth.schoolmedical.model.dto.request.VaccinationConsentFormRequest;
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

        // Check if campaign is still accepting responses
        if (!consentForm.getCampaign().getStatus().equals(VaccinationCampaignStatus.PUBLISHED)) {
            throw new IllegalStateException("Campaign is not in PUBLISHED status");
        }

        // Check if deadline has passed
        if (LocalDateTime.now().isAfter(consentForm.getCampaign().getFormDeadline().atTime(LocalTime.MAX))) {
            throw new IllegalStateException("Response deadline has passed");
        }

        // Validate status - parent can only respond with APPROVED or REJECTED
        if (request.getStatus() != ConsentFormStatus.APPROVED &&
                request.getStatus() != ConsentFormStatus.REJECTED) {
            throw new IllegalArgumentException("Parents can only respond with APPROVED or REJECTED");
        }

        // Update consent form
        consentForm.setStatus(request.getStatus());
        consentForm.setRespondedAt(LocalDateTime.now());

        VaccinationConsentForm savedForm = consentFormRepo.save(consentForm);
        log.info("Parent response saved successfully for form {}", formId);

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

        User parent = userRepository.findById(parentUserId)
                .orElseThrow(() -> new EntityNotFoundException("User", "id", parentUserId));

        List<Pupil> children = pupilRepo.findByParent(parent);

        return children.stream()
                .flatMap(pupil -> {
                    List<VaccinationConsentForm> activeConsentForms = consentFormRepo.findByPupilAndIsActiveTrue(pupil);

                    // Filter out consent forms for vaccines that have been fully completed
                    return activeConsentForms.stream()
                            .filter(form -> {
                                Vaccine vaccine = form.getVaccine();
                                int requiredDoses = vaccine.getDoseNumber();

                                // Get completed doses for this pupil and vaccine
                                List<VaccinationHistory> completedVaccinations =
                                        vaccinationHistoryRepo.findByPupilAndVaccineAndIsActiveTrue(pupil, vaccine);

                                // If completed doses are less than required, include this form
                                return completedVaccinations.size() < requiredDoses;
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

    @Override
    public int updateExpiredConsentForms() {
        log.info("Starting automatic update of expired consent forms");

        List<VaccinationConsentForm> expiredForms = consentFormRepo
                .findExpiredWaitingForms(ConsentFormStatus.WAITING, LocalDateTime.now());

        int updatedCount = 0;
        for (VaccinationConsentForm form : expiredForms) {
            form.setStatus(ConsentFormStatus.REJECTED);
            form.setRespondedAt(LocalDateTime.now());
            consentFormRepo.save(form);
            updatedCount++;
        }

        log.info("Updated {} expired consent forms from WAITING to REJECTED", updatedCount);
        return updatedCount;
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
        return VaccinationConsentFormResponse.builder()
                .consentFormId(form.getConsentFormId())
                .doseNumber(form.getDoseNumber())
                .respondedAt(form.getRespondedAt())
                .status(form.getStatus())
                .campaignName(form.getCampaign().getTitleCampaign())
                .vaccineName(form.getVaccine().getName())
                .pupilName(form.getPupil().getFirstName())
                .pupilId(form.getPupil().getPupilId())
                .gradeLevel(form.getPupil().getPupilGrade().stream()
                        .map(pg -> pg.getGrade().getGradeLevel().toString())
                        .findFirst()
                        .orElse("Unknown"))
                .formDeadline(form.getCampaign().getFormDeadline().atTime(LocalTime.MAX))
                .build();
    }
}