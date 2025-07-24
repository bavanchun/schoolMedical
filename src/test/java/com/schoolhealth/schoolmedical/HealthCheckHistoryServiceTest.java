package com.schoolhealth.schoolmedical;

import com.schoolhealth.schoolmedical.entity.*;
import com.schoolhealth.schoolmedical.exception.NotFoundException;
import com.schoolhealth.schoolmedical.model.dto.request.ConsentDiseaseReq;
import com.schoolhealth.schoolmedical.model.dto.request.HealthCheckHistoryReq;
import com.schoolhealth.schoolmedical.model.dto.request.UpdateHealthCheckHistoryReq;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckConsentRes;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckHistoryRes;
import com.schoolhealth.schoolmedical.model.mapper.DiseaseMapper;
import com.schoolhealth.schoolmedical.model.mapper.HealthCheckHistoryMapper;
import com.schoolhealth.schoolmedical.repository.HealthCheckConsentRepo;
import com.schoolhealth.schoolmedical.repository.HealthCheckHistoryRepo;
import com.schoolhealth.schoolmedical.service.Disease.DiseaseService;
import com.schoolhealth.schoolmedical.service.HealthCheckHistory.HealthCheckHistoryImpl;
import com.schoolhealth.schoolmedical.service.consentDisease.ConsentDiseaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HealthCheckHistoryServiceTest {

    @InjectMocks
    private HealthCheckHistoryImpl healthCheckHistoryService;

    @Mock
    private HealthCheckHistoryRepo healthCheckHistoryRepo;

    @Mock
    private HealthCheckConsentRepo healthCheckConsentRepo;

    @Mock
    private HealthCheckHistoryMapper healthCheckHistoryMapper;

    @Mock
    private DiseaseService diseaseService;

    @Mock
    private ConsentDiseaseService consentDiseaseService;

    @Mock
    private DiseaseMapper diseaseMapper;

    private HealthCheckHistoryReq healthCheckHistoryReq;
    private UpdateHealthCheckHistoryReq updateHealthCheckHistoryReq;
    private HealthCheckConsentForm consentForm;
    private HealthCheckHistory healthCheckHistory;

    @BeforeEach
    void setUp() {
        healthCheckHistoryReq = new HealthCheckHistoryReq();
        healthCheckHistoryReq.setDiseases(Collections.singletonList(new ConsentDiseaseReq(1L, "note")));

        updateHealthCheckHistoryReq = new UpdateHealthCheckHistoryReq();
        updateHealthCheckHistoryReq.setHealthId(1L);
        updateHealthCheckHistoryReq.setDiseases(Collections.singletonList(new ConsentDiseaseReq(1L, "new note")));

        consentForm = new HealthCheckConsentForm();
        consentForm.setConsentFormId(1L);

        healthCheckHistory = new HealthCheckHistory();
        healthCheckHistory.setHealthId(1L);
    }

    @Test
    void testSaveHealthCheckHistory() {
        when(healthCheckConsentRepo.findById(1L)).thenReturn(Optional.of(consentForm));
        when(healthCheckHistoryMapper.toHealthCheckHistory(any(HealthCheckHistoryReq.class))).thenReturn(healthCheckHistory);
        when(diseaseService.getDiseaseEntityById(1L)).thenReturn(new Disease());
        when(healthCheckHistoryRepo.save(any(HealthCheckHistory.class))).thenReturn(healthCheckHistory);

        HealthCheckHistory result = healthCheckHistoryService.saveHealthCheckHistory(healthCheckHistoryReq, 1L);

        assertNotNull(result);
        verify(consentDiseaseService).saveConsentDisease(any());
        verify(healthCheckConsentRepo).save(any(HealthCheckConsentForm.class));
    }

    @Test
    void testSaveHealthCheckHistory_ConsentFormNotFound() {
        when(healthCheckConsentRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            healthCheckHistoryService.saveHealthCheckHistory(healthCheckHistoryReq, 1L);
        });
    }

    @Test
    void testGetHealthCheckHistoryByPupilIdAndSchoolYear() {
        when(healthCheckHistoryRepo.findHealthCheckHistoryByPupilIdAndSchoolYear("P1", 2023)).thenReturn(List.of(healthCheckHistory));
        when(healthCheckHistoryMapper.toHealthCheckHistoryResWithStage(any(HealthCheckHistory.class), any(Integer.class))).thenReturn(new HealthCheckHistoryRes());

        List<HealthCheckHistoryRes> result = healthCheckHistoryService.getHealthCheckHistoryByPupilIdAndSchoolYear("P1", 2023);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(healthCheckHistoryRepo).findHealthCheckHistoryByPupilIdAndSchoolYear("P1", 2023);
        verify(healthCheckHistoryMapper).toHealthCheckHistoryResWithStage(any(HealthCheckHistory.class), any(Integer.class));
    }

    @Test
    void testGetHealthCheckHistoryByPupilIdAndSchoolYear_NotFound() {
        when(healthCheckHistoryRepo.findHealthCheckHistoryByPupilIdAndSchoolYear("P1", 2023)).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> {
            healthCheckHistoryService.getHealthCheckHistoryByPupilIdAndSchoolYear("P1", 2023);
        });
    }

    @Test
    void testUpdateHealthCheckHistory() {
        when(healthCheckConsentRepo.findById(1L)).thenReturn(Optional.of(consentForm));
        when(healthCheckHistoryRepo.findById(1L)).thenReturn(Optional.of(healthCheckHistory));
        when(consentDiseaseService.getConsentDiseaseById(any(ConsentDiseaseId.class))).thenReturn(new ConsentDisease());
        when(healthCheckHistoryRepo.save(any(HealthCheckHistory.class))).thenReturn(healthCheckHistory);
        when(healthCheckHistoryMapper.toHealthCheckHistoryRes(any(HealthCheckHistory.class))).thenReturn(new HealthCheckHistoryRes());

        HealthCheckConsentRes result = healthCheckHistoryService.updateHealthCheckHistory(updateHealthCheckHistoryReq, 1L);

        assertNotNull(result);
        verify(consentDiseaseService).saveConsentDisease(any());
    }

    @Test
    void testUpdateHealthCheckHistory_ConsentFormNotFound() {
        when(healthCheckConsentRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            healthCheckHistoryService.updateHealthCheckHistory(updateHealthCheckHistoryReq, 1L);
        });
    }

    @Test
    void testUpdateHealthCheckHistory_HistoryNotFound() {
        when(healthCheckConsentRepo.findById(1L)).thenReturn(Optional.of(consentForm));
        when(healthCheckHistoryRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            healthCheckHistoryService.updateHealthCheckHistory(updateHealthCheckHistoryReq, 1L);
        });
    }
}
