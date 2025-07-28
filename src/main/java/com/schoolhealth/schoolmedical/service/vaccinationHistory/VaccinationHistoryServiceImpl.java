package com.schoolhealth.schoolmedical.service.vaccinationHistory;

import com.schoolhealth.schoolmedical.entity.Disease;
import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.entity.User;
import com.schoolhealth.schoolmedical.entity.VaccinationConsentForm;
import com.schoolhealth.schoolmedical.entity.VaccinationHistory;
import com.schoolhealth.schoolmedical.entity.Vaccine;
import com.schoolhealth.schoolmedical.entity.enums.VaccinationSource;
import com.schoolhealth.schoolmedical.exception.EntityNotFoundException;
import com.schoolhealth.schoolmedical.model.dto.request.BulkVaccinationHistoryRequest;
import com.schoolhealth.schoolmedical.model.dto.request.VaccinationHistoryRequest;
import com.schoolhealth.schoolmedical.model.dto.response.BulkVaccinationHistoryResponse;
import com.schoolhealth.schoolmedical.model.dto.response.VaccinationHistoryResponse;
import com.schoolhealth.schoolmedical.repository.DiseaseRepo;
import com.schoolhealth.schoolmedical.repository.PupilRepo;
import com.schoolhealth.schoolmedical.repository.UserRepository;
import com.schoolhealth.schoolmedical.repository.VaccinationHistoryRepo;
import com.schoolhealth.schoolmedical.repository.VaccineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class VaccinationHistoryServiceImpl implements VaccinationHistoryService {
    private final VaccinationHistoryRepo vaccinationHistoryRepo;
    private final PupilRepo pupilRepo;
    private final VaccineRepository vaccineRepository;
    private final DiseaseRepo diseaseRepo;
    private final UserRepository userRepository;
    @Override
    public VaccinationHistoryResponse createFromCampaign(VaccinationConsentForm consentForm) {
        log.info("Creating vaccination history from campaign for pupil {}",
                consentForm.getPupil().getPupilId());

        VaccinationHistory history = VaccinationHistory.builder()
                .pupil(consentForm.getPupil())
                .vaccine(consentForm.getVaccine())
                .disease(consentForm.getCampaign().getDisease())
                .campaign(consentForm.getCampaign())
                .source(VaccinationSource.CAMPAIGN)
                .vaccinatedAt(LocalDate.now().atTime(LocalTime.NOON))
                .notes("Injection in the campaign: " + consentForm.getCampaign().getTitleCampaign())
                .isActive(true)
                .build();

        VaccinationHistory savedHistory = vaccinationHistoryRepo.save(history);
        log.info("Vaccination history created successfully with ID {}", savedHistory.getHistoryId());

        return mapToResponse(savedHistory);
    }

    @Override
    public VaccinationHistoryResponse createParentDeclaration(VaccinationHistoryRequest request, String parentUserId) {
        log.info("Creating parent declaration for pupil {} by parent {}", request.getPupilId(), parentUserId);

        // Validate parent permission
        validateParentPermission(request.getPupilId(), parentUserId);

        // Get entities
        Pupil pupil = pupilRepo.findById(request.getPupilId())
                .orElseThrow(() -> new EntityNotFoundException("Pupil", "pupilId", request.getPupilId()));

        Vaccine vaccine = vaccineRepository.findById(request.getVaccineId())
                .orElseThrow(() -> new EntityNotFoundException("Vaccine", "vaccineId", request.getVaccineId()));

        Disease disease = diseaseRepo.findById(request.getDiseaseId())
                .orElseThrow(() -> new EntityNotFoundException("Disease", "diseaseId", request.getDiseaseId()));

        VaccinationHistory history = VaccinationHistory.builder()
                .pupil(pupil)
                .vaccine(vaccine)
                .disease(disease)
                .campaign(null) // No campaign for parent declaration
                .source(VaccinationSource.PARENT_DECLARATION)
                .vaccinatedAt(request.getVaccinatedAt() != null ?
                        request.getVaccinatedAt().atTime(LocalTime.NOON) : null)
                .notes(request.getNotes())
                .isActive(true) // Auto-approved - no longer needs nurse confirmation
                .build();

        VaccinationHistory savedHistory = vaccinationHistoryRepo.save(history);
        log.info("Parent declaration created successfully with ID {} (auto-approved)",
                savedHistory.getHistoryId());

        return mapToResponse(savedHistory);
    }

    @Override
    public BulkVaccinationHistoryResponse createBulkParentDeclaration(BulkVaccinationHistoryRequest request, String parentUserId) {
        log.info("Creating bulk parent declaration for pupil {} by parent {}", request.getPupilId(), parentUserId);

        // Validate parent permission
        validateParentPermission(request.getPupilId(), parentUserId);

        // Get pupil once
        Pupil pupil = pupilRepo.findById(request.getPupilId())
                .orElseThrow(() -> new EntityNotFoundException("Pupil", "pupilId", request.getPupilId()));

        List<VaccinationHistoryResponse> successfulRecords = new ArrayList<>();
        List<BulkVaccinationHistoryResponse.BulkError> errors = new ArrayList<>();
        int totalCreated = 0;
        int totalFailed = 0;

        // Process each vaccination history item
        for (BulkVaccinationHistoryRequest.VaccinationHistoryItem item : request.getVaccinationHistories()) {
            try {
                // Get vaccine and disease once per item
                Vaccine vaccine = vaccineRepository.findById(item.getVaccineId())
                        .orElseThrow(() -> new EntityNotFoundException("Vaccine", "vaccineId", item.getVaccineId()));

                Disease disease = diseaseRepo.findById(item.getDiseaseId())
                        .orElseThrow(() -> new EntityNotFoundException("Disease", "diseaseId", item.getDiseaseId()));

                // Process each dose for this vaccine/disease combination
                for (BulkVaccinationHistoryRequest.DoseInfo dose : item.getDoses()) {
                    try {
                        // Create vaccination history record for each dose
                        String doseNote = dose.getNotes();
                        if (dose.getDoseNumber() != null) {
                            doseNote = (doseNote != null ? doseNote : "") +
                                    (doseNote != null && !doseNote.isEmpty() ? " " : "") +
                                    "[Dose " + dose.getDoseNumber() + "]";
                        }

                        VaccinationHistory history = VaccinationHistory.builder()
                                .pupil(pupil)
                                .vaccine(vaccine)
                                .disease(disease)
                                .campaign(null) // No campaign for parent declaration
                                .source(VaccinationSource.PARENT_DECLARATION)
                                .vaccinatedAt(dose.getVaccinatedAt() != null ?
                                        dose.getVaccinatedAt().atTime(LocalTime.NOON) : null)
                                .notes(doseNote)
                                .isActive(true) // Auto-approved
                                .build();

                        VaccinationHistory savedHistory = vaccinationHistoryRepo.save(history);
                        successfulRecords.add(mapToResponse(savedHistory));
                        totalCreated++;

                        log.info("Successfully created vaccination history for pupil {} - vaccine {} - dose {}",
                                request.getPupilId(), item.getVaccineId(), dose.getDoseNumber());

                    } catch (Exception doseException) {
                        totalFailed++;
                        BulkVaccinationHistoryResponse.BulkError error = BulkVaccinationHistoryResponse.BulkError.builder()
                                .vaccineId(item.getVaccineId())
                                .diseaseId(item.getDiseaseId())
                                .errorMessage("Dose " + dose.getDoseNumber() + ": " + doseException.getMessage())
                                .build();
                        errors.add(error);

                        log.error("Failed to create dose {} for pupil {} - vaccine {}: {}",
                                dose.getDoseNumber(), request.getPupilId(), item.getVaccineId(), doseException.getMessage());
                    }
                }

            } catch (Exception itemException) {
                // This catches vaccine/disease lookup errors
                int dosesCount = item.getDoses() != null ? item.getDoses().size() : 0;
                totalFailed += dosesCount;

                BulkVaccinationHistoryResponse.BulkError error = BulkVaccinationHistoryResponse.BulkError.builder()
                        .vaccineId(item.getVaccineId())
                        .diseaseId(item.getDiseaseId())
                        .errorMessage(itemException.getMessage())
                        .build();
                errors.add(error);

                log.error("Failed to process vaccination item for pupil {} - vaccine {}: {}",
                        request.getPupilId(), item.getVaccineId(), itemException.getMessage());
            }
        }

        log.info("Bulk parent declaration completed: {} created, {} failed", totalCreated, totalFailed);

        return BulkVaccinationHistoryResponse.builder()
                .pupilId(pupil.getPupilId())
                .pupilName(pupil.getLastName() + " " + pupil.getFirstName())
                .totalCreated(totalCreated)
                .totalFailed(totalFailed)
                .successfulRecords(successfulRecords)
                .errors(errors)
                .build();
    }

    @Override
    public VaccinationHistoryResponse confirmParentDeclaration(Long historyId, boolean approved) {
        log.info("Confirming parent declaration {} with approval: {}", historyId, approved);

        VaccinationHistory history = vaccinationHistoryRepo.findById(historyId)
                .orElseThrow(() -> new EntityNotFoundException("VaccinationHistory", "historyId", historyId));

        if (history.getSource() != VaccinationSource.PARENT_DECLARATION) {
            throw new IllegalArgumentException("Only parent declarations can be confirmed");
        }

        if (approved) {
            history.setActive(true);
            log.info("Parent declaration {} approved and activated", historyId);
        } else {
            // Mark as rejected by setting a note
            history.setNotes((history.getNotes() != null ? history.getNotes() : "") + " [REJECTED BY NURSE]");
            history.setActive(false);
            log.info("Parent declaration {} rejected", historyId);
        }

        VaccinationHistory savedHistory = vaccinationHistoryRepo.save(history);
        return mapToResponse(savedHistory);
    }

    @Override
    public List<VaccinationHistoryResponse> getPupilVaccinationHistory(String pupilId) {
        log.info("Getting vaccination history for pupil {}", pupilId);

        Pupil pupil = pupilRepo.findById(pupilId)
                .orElseThrow(() -> new EntityNotFoundException("Pupil", "id", pupilId));

        List<VaccinationHistory> histories = vaccinationHistoryRepo
                .findByPupilAndIsActiveTrueOrderByVaccinatedAtDesc(pupil);

        return histories.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public VaccinationHistoryResponse getHistoryById(Long historyId) {
        log.info("Getting vaccination history by ID {}", historyId);

        VaccinationHistory history = vaccinationHistoryRepo.findById(historyId)
                .orElseThrow(() -> new EntityNotFoundException("VaccinationHistory", "id", historyId));

        return mapToResponse(history);
    }

    @Override
    public VaccinationHistoryResponse updateHistory(Long historyId, VaccinationHistoryRequest request) {
        log.info("Updating vaccination history {}", historyId);

        VaccinationHistory history = vaccinationHistoryRepo.findById(historyId)
                .orElseThrow(() -> new EntityNotFoundException("VaccinationHistory", "id", historyId));

        // Only allow updating notes and vaccination date for parent declarations
        if (history.getSource() == VaccinationSource.PARENT_DECLARATION) {
            history.setVaccinatedAt(request.getVaccinatedAt() != null ?
                    request.getVaccinatedAt().atTime(LocalTime.NOON) : null);
            history.setNotes(request.getNotes());
        } else {
            // For campaign records, only allow updating notes
            history.setNotes(request.getNotes());
        }

        VaccinationHistory savedHistory = vaccinationHistoryRepo.save(history);
        log.info("Vaccination history updated successfully");

        return mapToResponse(savedHistory);
    }

    @Override
    public boolean hasCompletedVaccination(String pupilId, Long diseaseId, int requiredDoses) {
        log.info("Checking if pupil {} has completed vaccination for disease {} (required: {} doses)",
                pupilId, diseaseId, requiredDoses);

        Pupil pupil = pupilRepo.findById(pupilId)
                .orElseThrow(() -> new EntityNotFoundException("Pupil", "id", pupilId));

        Disease disease = diseaseRepo.findById(diseaseId)
                .orElseThrow(() -> new EntityNotFoundException("Disease", "id", diseaseId));

        int completedDoses = vaccinationHistoryRepo.countByPupilAndDiseaseAndIsActiveTrue(pupil, disease);

        boolean isCompleted = completedDoses >= requiredDoses;
        log.info("Pupil {} has completed {}/{} doses for disease {}",
                pupilId, completedDoses, requiredDoses, diseaseId);

        return isCompleted;
    }

    @Override
    @Transactional(readOnly = true)
    public List<VaccinationHistoryResponse> getPendingParentDeclarations() {
        log.info("Getting all pending parent declarations");

        List<VaccinationHistory> pendingDeclarations = vaccinationHistoryRepo
                .findBySourceAndIsActiveFalseOrderByVaccinatedAtDesc(VaccinationSource.PARENT_DECLARATION);

        return pendingDeclarations.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VaccinationHistoryResponse> getPendingParentDeclarationsByPupil(String pupilId) {
        log.info("Getting pending parent declarations for pupil {}", pupilId);

        Pupil pupil = pupilRepo.findById(pupilId)
                .orElseThrow(() -> new EntityNotFoundException("Pupil", "id", pupilId));

        List<VaccinationHistory> pendingDeclarations = vaccinationHistoryRepo
                .findByPupilAndSourceAndIsActiveFalseOrderByVaccinatedAtDesc(pupil, VaccinationSource.PARENT_DECLARATION);

        return pendingDeclarations.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private void validateParentPermission(String pupilId, String parentUserId) {
        User parent = userRepository.findById(parentUserId)
                .orElseThrow(() -> new EntityNotFoundException("User", "id", parentUserId));

        List<Pupil> children = pupilRepo.findByParent(parent);
        boolean isParentOfPupil = children.stream()
                .anyMatch(child -> child.getPupilId().equals(pupilId));

        if (!isParentOfPupil) {
            throw new IllegalStateException("Parent does not have permission to create declaration for this pupil");
        }
    }

    private VaccinationHistoryResponse mapToResponse(VaccinationHistory history) {
        return VaccinationHistoryResponse.builder()
                .historyId(history.getHistoryId())
                .pupilId(history.getPupil().getPupilId())
                .pupilName(history.getPupil().getLastName() + " " + history.getPupil().getFirstName()) // Updated: lastName + firstName
                .vaccineName(history.getVaccine().getName()) // Fixed: Assumed getName() exists
                .diseaseName(history.getDisease().getName()) // Fixed: Assumed getName() exists
                .campaignName(history.getCampaign() != null ? history.getCampaign().getTitleCampaign() : null) // Fixed: Used getTitleCampaign for consistency
                .source(history.getSource())
                .vaccinatedAt(history.getVaccinatedAt())
                .notes(history.getNotes())
                .isActive(history.isActive())
                .build();
    }
}
