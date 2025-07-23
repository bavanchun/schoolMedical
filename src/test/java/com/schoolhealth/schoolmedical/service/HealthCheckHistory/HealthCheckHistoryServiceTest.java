package com.schoolhealth.schoolmedical.service.HealthCheckHistory;

import com.schoolhealth.schoolmedical.entity.ConsentDisease;
import com.schoolhealth.schoolmedical.entity.Disease;
import com.schoolhealth.schoolmedical.entity.HealthCheckConsentForm;
import com.schoolhealth.schoolmedical.entity.HealthCheckHistory;
import com.schoolhealth.schoolmedical.exception.NotFoundException;
import com.schoolhealth.schoolmedical.model.dto.request.ConsentDiseaseReq;
import com.schoolhealth.schoolmedical.model.dto.request.HealthCheckHistoryReq;
import com.schoolhealth.schoolmedical.model.mapper.HealthCheckHistoryMapper;
import com.schoolhealth.schoolmedical.repository.HealthCheckConsentRepo;
import com.schoolhealth.schoolmedical.repository.HealthCheckHistoryRepo;
import com.schoolhealth.schoolmedical.service.DiseaseService;
import com.schoolhealth.schoolmedical.service.consentDisease.ConsentDiseaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    private HealthCheckHistoryReq healthCheckHistoryReq;
    private HealthCheckConsentForm consentForm;
    private HealthCheckHistory healthCheckHistory;

    @BeforeEach
    void setUp() {
        healthCheckHistoryReq = new HealthCheckHistoryReq();
        healthCheckHistoryReq.setDiseases(Collections.singletonList(new ConsentDiseaseReq(1L, "note")));

        consentForm = new HealthCheckConsentForm();
        consentForm.setConsentFormId(1L);

        healthCheckHistory = new HealthCheckHistory();
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
}

