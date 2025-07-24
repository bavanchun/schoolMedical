package com.schoolhealth.schoolmedical;

import com.schoolhealth.schoolmedical.entity.*;
import com.schoolhealth.schoolmedical.entity.enums.ConsentFormStatus;
import com.schoolhealth.schoolmedical.entity.enums.TypeNotification;
import com.schoolhealth.schoolmedical.entity.enums.VaccinationCampaignStatus;
import com.schoolhealth.schoolmedical.exception.NotFoundException;
import com.schoolhealth.schoolmedical.model.dto.request.VaccinationCampaignRequest;
import com.schoolhealth.schoolmedical.model.dto.response.VaccinationCampaignResponse;
import com.schoolhealth.schoolmedical.model.mapper.VaccinationCampaignMapper;
import com.schoolhealth.schoolmedical.repository.*;
import com.schoolhealth.schoolmedical.service.vaccinationCampaign.VaccinationCampaignServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VaccinationServiceTest {

    @InjectMocks
    private VaccinationCampaignServiceImpl vaccinationCampaignService;

    @Mock
    private VaccinationCampaignRepo vaccinationCampaignRepo;

    @Mock
    private VaccineRepository vaccineRepository;

    @Mock
    private DiseaseRepo diseaseRepo;

    @Mock
    private VaccinationCampaignMapper mapper;

    @Mock
    private PupilRepo pupilRepo;

    @Mock
    private VaccinationConsentFormRepo consentFormRepo;

    @Mock
    private VaccinationHistoryRepo vaccinationHistoryRepo;

    @Mock
    private NotificationRepo notificationRepo;

    // Test data
    private VaccinationCampaignRequest request;
    private VaccinationCampagin campaign;
    private VaccinationCampaignResponse response;
    private Vaccine vaccine;
    private Disease disease;
    private Pupil pupil;
    private User parent;
    private VaccinationConsentForm consentForm;

    @BeforeEach
    void setUp() {
        // Setup test data
        vaccine = new Vaccine();
        vaccine.setVaccineId(1L);
        vaccine.setName("COVID-19 Vaccine");

        disease = new Disease();
        disease.setDiseaseId(1L);
        disease.setName("COVID-19");
        disease.setDoseQuantity(2);

        parent = new User();
        parent.setUserId("parent1");
        parent.setFirstName("John");
        parent.setLastName("Doe");

        pupil = new Pupil();
        pupil.setPupilId("pupil1");
        pupil.setFirstName("Jane");
        pupil.setLastName("Doe");
        pupil.setParents(List.of(parent));

        request = VaccinationCampaignRequest.builder()
                .titleCampaign("COVID-19 Vaccination Campaign")
                .vaccineId(1)
                .diseaseId(1)
                .startDate(LocalDate.now().plusDays(10))
                .endDate(LocalDate.now().plusDays(20))
                .formDeadline(LocalDate.now().plusDays(5))
                .notes("Important vaccination campaign")
                .build();

        campaign = VaccinationCampagin.builder()
                .campaignId(1L)
                .titleCampaign("COVID-19 Vaccination Campaign")
                .vaccine(vaccine)
                .disease(disease)
                .startDate(LocalDate.now().plusDays(10))
                .endDate(LocalDate.now().plusDays(20))
                .formDeadline(LocalDate.now().plusDays(5))
                .notes("Important vaccination campaign")
                .status(VaccinationCampaignStatus.PENDING)
                .isActive(true)
                .build();

        response = VaccinationCampaignResponse.builder()
                .campaignId(1)
                .titleCampaign("COVID-19 Vaccination Campaign")
                .vaccineName("COVID-19 Vaccine")
                .diseaseName("COVID-19")
                .startDate(LocalDate.now().plusDays(10))
                .endDate(LocalDate.now().plusDays(20))
                .formDeadline(LocalDate.now().plusDays(5))
                .notes("Important vaccination campaign")
                .status("PENDING")
                .build();

        consentForm = VaccinationConsentForm.builder()
                .campaign(campaign)
                .pupil(pupil)
                .vaccine(vaccine)
                .status(ConsentFormStatus.REJECTED)
                .isActive(true)
                .build();
    }

    // ================== createCampaign Tests ==================

    @Test
    void testCreateCampaign_Success() {
        when(vaccineRepository.findById(1L)).thenReturn(Optional.of(vaccine));
        when(diseaseRepo.findById(1L)).thenReturn(Optional.of(disease));
        when(mapper.toVaccinationCampaign(request)).thenReturn(campaign);
        when(vaccinationCampaignRepo.save(any(VaccinationCampagin.class))).thenReturn(campaign);
        when(mapper.toVaccinationCampaignResponse(campaign)).thenReturn(response);

        VaccinationCampaignResponse result = vaccinationCampaignService.createCampaign(request);

        assertNotNull(result);
        assertEquals("COVID-19 Vaccination Campaign", result.getTitleCampaign());
        assertEquals("PENDING", result.getStatus());
        verify(vaccinationCampaignRepo, times(1)).save(any(VaccinationCampagin.class));
    }

    @Test
    void testCreateCampaign_VaccineNotFound() {
        when(vaccineRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
            vaccinationCampaignService.createCampaign(request));

        verify(diseaseRepo, never()).findById(anyLong());
        verify(vaccinationCampaignRepo, never()).save(any());
    }

    @Test
    void testCreateCampaign_DiseaseNotFound() {
        when(vaccineRepository.findById(1L)).thenReturn(Optional.of(vaccine));
        when(diseaseRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
            vaccinationCampaignService.createCampaign(request));

        verify(vaccinationCampaignRepo, never()).save(any());
    }

    // ================== publishCampaign Tests ==================

    @Test
    void testPublishCampaign_Success() {
        List<Pupil> eligiblePupils = List.of(pupil);

        when(vaccinationCampaignRepo.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(campaign));
        when(pupilRepo.findPupilsNeedingVaccination(disease.getDiseaseId(), disease.getDoseQuantity()))
            .thenReturn(eligiblePupils);
        when(vaccinationHistoryRepo.countByPupilAndDiseaseAndIsActiveTrue(pupil, disease)).thenReturn(0);
        when(consentFormRepo.saveAll(anyList())).thenReturn(Collections.emptyList());
        when(notificationRepo.saveAll(anyList())).thenReturn(Collections.emptyList());
        when(vaccinationCampaignRepo.save(campaign)).thenReturn(campaign);

        vaccinationCampaignService.publishCampaign(1L);

        verify(consentFormRepo, times(1)).saveAll(anyList());
        verify(notificationRepo, times(1)).saveAll(anyList());
        verify(vaccinationCampaignRepo, times(1)).save(campaign);
        assertEquals(VaccinationCampaignStatus.PUBLISHED, campaign.getStatus());
    }

    @Test
    void testPublishCampaign_CampaignNotFound() {
        when(vaccinationCampaignRepo.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
            vaccinationCampaignService.publishCampaign(1L));

        verify(pupilRepo, never()).findPupilsNeedingVaccination(anyLong(), anyInt());
    }

    @Test
    void testPublishCampaign_InvalidStatus() {
        campaign.setStatus(VaccinationCampaignStatus.PUBLISHED);
        when(vaccinationCampaignRepo.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(campaign));

        assertThrows(IllegalStateException.class, () ->
            vaccinationCampaignService.publishCampaign(1L));

        verify(pupilRepo, never()).findPupilsNeedingVaccination(anyLong(), anyInt());
    }

    @Test
    void testPublishCampaign_NoEligiblePupils() {
        when(vaccinationCampaignRepo.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(campaign));
        when(pupilRepo.findPupilsNeedingVaccination(disease.getDiseaseId(), disease.getDoseQuantity()))
            .thenReturn(Collections.emptyList());
        when(vaccinationCampaignRepo.save(campaign)).thenReturn(campaign);

        vaccinationCampaignService.publishCampaign(1L);

        verify(consentFormRepo, times(1)).saveAll(eq(Collections.emptyList()));
        verify(notificationRepo, times(1)).saveAll(eq(Collections.emptyList()));
        assertEquals(VaccinationCampaignStatus.PUBLISHED, campaign.getStatus());
    }

    @Test
    void testPublishCampaign_WithMultiplePupilsAndParents() {
        User parent2 = new User();
        parent2.setUserId("parent2");

        Pupil pupil2 = new Pupil();
        pupil2.setPupilId("pupil2");
        pupil2.setParents(List.of(parent, parent2)); // Multiple parents

        List<Pupil> eligiblePupils = List.of(pupil, pupil2);

        when(vaccinationCampaignRepo.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(campaign));
        when(pupilRepo.findPupilsNeedingVaccination(disease.getDiseaseId(), disease.getDoseQuantity()))
            .thenReturn(eligiblePupils);
        when(vaccinationHistoryRepo.countByPupilAndDiseaseAndIsActiveTrue(any(Pupil.class), eq(disease)))
            .thenReturn(0);
        when(consentFormRepo.saveAll(anyList())).thenReturn(Collections.emptyList());
        when(notificationRepo.saveAll(anyList())).thenReturn(Collections.emptyList());
        when(vaccinationCampaignRepo.save(campaign)).thenReturn(campaign);

        vaccinationCampaignService.publishCampaign(1L);

//        verify(consentFormRepo, times(1)).saveAll(argThat(list -> list.size() == 2)); // 2 consent forms
//        verify(notificationRepo, times(1)).saveAll(argThat(list -> list.size() == 3)); // 3 notifications (1+2 parents)
        assertEquals(VaccinationCampaignStatus.PUBLISHED, campaign.getStatus());
    }

    // ================== updateStatus Tests ==================

    @Test
    void testUpdateStatus_PendingToPublished_Success() {
        campaign.setStatus(VaccinationCampaignStatus.PENDING);
        when(vaccinationCampaignRepo.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(campaign));
        when(vaccinationCampaignRepo.save(campaign)).thenReturn(campaign);

        vaccinationCampaignService.updateStatus(1L, VaccinationCampaignStatus.PUBLISHED);

        assertEquals(VaccinationCampaignStatus.PUBLISHED, campaign.getStatus());
        verify(vaccinationCampaignRepo, times(1)).save(campaign);
    }

    @Test
    void testUpdateStatus_PublishedToInProgress_Success() {
        campaign.setStatus(VaccinationCampaignStatus.PUBLISHED);
        when(vaccinationCampaignRepo.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(campaign));
        when(vaccinationCampaignRepo.save(campaign)).thenReturn(campaign);

        vaccinationCampaignService.updateStatus(1L, VaccinationCampaignStatus.IN_PROGRESS);

        assertEquals(VaccinationCampaignStatus.IN_PROGRESS, campaign.getStatus());
        verify(vaccinationCampaignRepo, times(1)).save(campaign);
    }

    @Test
    void testUpdateStatus_InProgressToCompleted_Success() {
        campaign.setStatus(VaccinationCampaignStatus.IN_PROGRESS);
        when(vaccinationCampaignRepo.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(campaign));
        when(consentFormRepo.findByCampaign(campaign)).thenReturn(List.of(consentForm));
        when(notificationRepo.saveAll(anyList())).thenReturn(Collections.emptyList());
        when(vaccinationCampaignRepo.save(campaign)).thenReturn(campaign);

        vaccinationCampaignService.updateStatus(1L, VaccinationCampaignStatus.COMPLETED);

        assertEquals(VaccinationCampaignStatus.COMPLETED, campaign.getStatus());
        verify(notificationRepo, times(1)).saveAll(anyList()); // Completion notifications
        verify(vaccinationCampaignRepo, times(1)).save(campaign);
    }

    @Test
    void testUpdateStatus_PublishedToCanceled_Success() {
        campaign.setStatus(VaccinationCampaignStatus.PUBLISHED);
        when(vaccinationCampaignRepo.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(campaign));
        when(vaccinationCampaignRepo.save(campaign)).thenReturn(campaign);

        vaccinationCampaignService.updateStatus(1L, VaccinationCampaignStatus.CANCELED);

        assertEquals(VaccinationCampaignStatus.CANCELED, campaign.getStatus());
        verify(vaccinationCampaignRepo, times(1)).save(campaign);
        verify(notificationRepo, never()).saveAll(anyList()); // No notifications are sent on cancellation via updateStatus
    }

    @Test
    void testUpdateStatus_CampaignNotFound() {
        when(vaccinationCampaignRepo.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
            vaccinationCampaignService.updateStatus(1L, VaccinationCampaignStatus.PUBLISHED));

        verify(vaccinationCampaignRepo, never()).save(any());
    }

    @Test
    void testUpdateStatus_InvalidTransition_PendingToCompleted() {
        campaign.setStatus(VaccinationCampaignStatus.PENDING);
        when(vaccinationCampaignRepo.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(campaign));

        assertThrows(IllegalStateException.class, () ->
            vaccinationCampaignService.updateStatus(1L, VaccinationCampaignStatus.COMPLETED));

        verify(vaccinationCampaignRepo, never()).save(any());
    }

    @Test
    void testUpdateStatus_InvalidTransition_CompletedToPublished() {
        campaign.setStatus(VaccinationCampaignStatus.COMPLETED);
        when(vaccinationCampaignRepo.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(campaign));

        assertThrows(IllegalStateException.class, () ->
            vaccinationCampaignService.updateStatus(1L, VaccinationCampaignStatus.PUBLISHED));

        verify(vaccinationCampaignRepo, never()).save(any());
    }

    @Test
    void testUpdateStatus_InvalidTransition_CanceledToInProgress() {
        campaign.setStatus(VaccinationCampaignStatus.CANCELED);
        when(vaccinationCampaignRepo.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(campaign));

        assertThrows(IllegalStateException.class, () ->
            vaccinationCampaignService.updateStatus(1L, VaccinationCampaignStatus.IN_PROGRESS));

        verify(vaccinationCampaignRepo, never()).save(any());
    }

    @Test
    void testUpdateStatus_CompletionWithDifferentConsentStatuses() {
        campaign.setStatus(VaccinationCampaignStatus.IN_PROGRESS);

        // Create consent forms with different statuses
        VaccinationConsentForm injectedForm = VaccinationConsentForm.builder()
                .campaign(campaign).pupil(pupil).vaccine(vaccine)
                .status(ConsentFormStatus.INJECTED).build();

        VaccinationConsentForm noShowForm = VaccinationConsentForm.builder()
                .campaign(campaign).pupil(pupil).vaccine(vaccine)
                .status(ConsentFormStatus.NO_SHOW).build();

        VaccinationConsentForm approvedForm = VaccinationConsentForm.builder()
                .campaign(campaign).pupil(pupil).vaccine(vaccine)
                .status(ConsentFormStatus.APPROVED).build();

        List<VaccinationConsentForm> consentForms = List.of(injectedForm, noShowForm, approvedForm);

        when(vaccinationCampaignRepo.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(campaign));
        when(consentFormRepo.findByCampaign(campaign)).thenReturn(consentForms);
        when(notificationRepo.saveAll(anyList())).thenReturn(Collections.emptyList());
        when(vaccinationCampaignRepo.save(campaign)).thenReturn(campaign);

        vaccinationCampaignService.updateStatus(1L, VaccinationCampaignStatus.COMPLETED);

        assertEquals(VaccinationCampaignStatus.COMPLETED, campaign.getStatus());
//        verify(notificationRepo, times(1)).saveAll(argThat(list -> list.size() >= 3)); // At least 3 notifications
    }

    // ================== Edge Cases and Additional Tests ==================

    @Test
    void testPublishCampaign_PupilWithNoParents() {
        Pupil orphanPupil = new Pupil();
        orphanPupil.setPupilId("orphan1");
        orphanPupil.setParents(Collections.emptyList());

        List<Pupil> eligiblePupils = List.of(orphanPupil);

        when(vaccinationCampaignRepo.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(campaign));
        when(pupilRepo.findPupilsNeedingVaccination(disease.getDiseaseId(), disease.getDoseQuantity()))
            .thenReturn(eligiblePupils);
        when(vaccinationHistoryRepo.countByPupilAndDiseaseAndIsActiveTrue(orphanPupil, disease))
            .thenReturn(0);
        when(consentFormRepo.saveAll(anyList())).thenReturn(Collections.emptyList());
        when(notificationRepo.saveAll(anyList())).thenReturn(Collections.emptyList());
        when(vaccinationCampaignRepo.save(campaign)).thenReturn(campaign);

        vaccinationCampaignService.publishCampaign(1L);

        // Use ArgumentCaptor for robust verification
        ArgumentCaptor<List<VaccinationConsentForm>> consentFormCaptor = ArgumentCaptor.forClass(List.class);
        verify(consentFormRepo, times(1)).saveAll(consentFormCaptor.capture());
        assertEquals(1, consentFormCaptor.getValue().size());

        ArgumentCaptor<List<UserNotification>> notificationCaptor = ArgumentCaptor.forClass(List.class);
        verify(notificationRepo, times(1)).saveAll(notificationCaptor.capture());
        assertTrue(notificationCaptor.getValue().isEmpty());

        assertEquals(VaccinationCampaignStatus.PUBLISHED, campaign.getStatus());
    }

    @Test
    void testPublishCampaign_PupilWithPartialVaccination() {
        when(vaccinationCampaignRepo.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(campaign));
        when(pupilRepo.findPupilsNeedingVaccination(disease.getDiseaseId(), disease.getDoseQuantity()))
            .thenReturn(List.of(pupil));
        when(vaccinationHistoryRepo.countByPupilAndDiseaseAndIsActiveTrue(pupil, disease))
            .thenReturn(1); // Already has 1 dose, needs second dose
        when(consentFormRepo.saveAll(anyList())).thenReturn(Collections.emptyList());
        when(notificationRepo.saveAll(anyList())).thenReturn(Collections.emptyList());
        when(vaccinationCampaignRepo.save(campaign)).thenReturn(campaign);

        vaccinationCampaignService.publishCampaign(1L);

        verify(consentFormRepo, times(1)).saveAll(anyList());
        verify(notificationRepo, times(1)).saveAll(anyList());
        assertEquals(VaccinationCampaignStatus.PUBLISHED, campaign.getStatus());
    }

    @Test
    void testCreateCampaign_MapperReturnsNull() {
        when(vaccineRepository.findById(1L)).thenReturn(Optional.of(vaccine));
        when(diseaseRepo.findById(1L)).thenReturn(Optional.of(disease));
        when(mapper.toVaccinationCampaign(request)).thenReturn(null);

        // Expect a NullPointerException because the service doesn't handle a null campaign from the mapper
        assertThrows(NullPointerException.class, () ->
            vaccinationCampaignService.createCampaign(request));
    }


    // Test all valid status transitions
    @Test
    void testUpdateStatus_AllValidTransitions() {
        // PENDING -> PUBLISHED
        campaign.setStatus(VaccinationCampaignStatus.PENDING);
        when(vaccinationCampaignRepo.findByIdAndIsActiveTrue(1L)).thenReturn(Optional.of(campaign));
        when(vaccinationCampaignRepo.save(campaign)).thenReturn(campaign);

        vaccinationCampaignService.updateStatus(1L, VaccinationCampaignStatus.PUBLISHED);
        assertEquals(VaccinationCampaignStatus.PUBLISHED, campaign.getStatus());

        // PUBLISHED -> IN_PROGRESS
        campaign.setStatus(VaccinationCampaignStatus.PUBLISHED);
        vaccinationCampaignService.updateStatus(1L, VaccinationCampaignStatus.IN_PROGRESS);
        assertEquals(VaccinationCampaignStatus.IN_PROGRESS, campaign.getStatus());

        // IN_PROGRESS -> CANCELED
        campaign.setStatus(VaccinationCampaignStatus.IN_PROGRESS);
        // No special logic for cancellation, so no extra mocks needed
        vaccinationCampaignService.updateStatus(1L, VaccinationCampaignStatus.CANCELED);
        assertEquals(VaccinationCampaignStatus.CANCELED, campaign.getStatus());

        // IN_PROGRESS -> COMPLETED
        campaign.setStatus(VaccinationCampaignStatus.IN_PROGRESS);
        when(consentFormRepo.findByCampaign(campaign)).thenReturn(Collections.emptyList()); // Mock for completion logic
        vaccinationCampaignService.updateStatus(1L, VaccinationCampaignStatus.COMPLETED);
        assertEquals(VaccinationCampaignStatus.COMPLETED, campaign.getStatus());

        verify(vaccinationCampaignRepo, times(4)).save(campaign);
    }
}
