package com.schoolhealth.schoolmedical;

import com.schoolhealth.schoolmedical.entity.Disease;
import com.schoolhealth.schoolmedical.entity.HealthCheckCampaign;
import com.schoolhealth.schoolmedical.entity.Pupil;
import com.schoolhealth.schoolmedical.entity.User;
import com.schoolhealth.schoolmedical.entity.enums.StatusHealthCampaign;
import com.schoolhealth.schoolmedical.exception.UpdateNotAllowedException;
import com.schoolhealth.schoolmedical.model.dto.request.HealthCheckCampaignReq;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckCampaignRes;
import com.schoolhealth.schoolmedical.model.mapper.DiseaseMapper;
import com.schoolhealth.schoolmedical.model.mapper.HealthCheckCampaignMapper;
import com.schoolhealth.schoolmedical.repository.HealthCheckCampaignRepo;
import com.schoolhealth.schoolmedical.service.DiseaseService;
import com.schoolhealth.schoolmedical.service.HealthCheckCampaignImpl;
import com.schoolhealth.schoolmedical.service.HealthCheckConsentService;
import com.schoolhealth.schoolmedical.service.HealthCheckDiseaseService;
import com.schoolhealth.schoolmedical.service.Notification.FCMService;
import com.schoolhealth.schoolmedical.service.Notification.UserNotificationService;
import com.schoolhealth.schoolmedical.service.pupil.PupilService;
import com.schoolhealth.schoolmedical.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class HealthCheckCampaignServiceTest {

    @InjectMocks
    private HealthCheckCampaignImpl healthCheckCampaignService;

    @Mock
    private HealthCheckCampaignRepo healthCheckCampaignRepo;

    @Mock
    private DiseaseService diseaseService;

    @Mock
    private HealthCheckDiseaseService healthCheckDiseaseService;

    @Mock
    private HealthCheckCampaignMapper healthCheckCampaignMapper;

    @Mock
    private PupilService pupilService;

    @Mock
    private UserService userService;

    @Mock
    private UserNotificationService userNotificationService;

    @Mock
    private FCMService fcmService;

    @Mock
    private DiseaseMapper diseaseMapper;

    @Mock
    private HealthCheckConsentService healthCheckConsentService;

    private HealthCheckCampaign healthCheckCampaign;
    private HealthCheckCampaignReq healthCheckCampaignReq;
    private HealthCheckCampaignRes healthCheckCampaignRes;

    @BeforeEach
    void setUp() {
        healthCheckCampaign = new HealthCheckCampaign();
        healthCheckCampaign.setCampaignId(1L);
        healthCheckCampaign.setTitle("Test Campaign");
        healthCheckCampaign.setStatusHealthCampaign(StatusHealthCampaign.PENDING);
        healthCheckCampaign.setHealthCheckDiseases(new java.util.ArrayList<>());

        healthCheckCampaignReq = new HealthCheckCampaignReq();
        healthCheckCampaignReq.setTitle("Test Campaign");
        healthCheckCampaignReq.setDiseaseIds(Collections.singletonList(1L));

        healthCheckCampaignRes = new HealthCheckCampaignRes();
        healthCheckCampaignRes.setCampaignId(1L);
        healthCheckCampaignRes.setTitle("Test Campaign");
    }

    @Test
    void testSaveHealthCheckCampaign() {
        when(healthCheckCampaignMapper.toEntity(any(HealthCheckCampaignReq.class))).thenReturn(healthCheckCampaign);
        when(healthCheckCampaignRepo.save(any(HealthCheckCampaign.class))).thenReturn(healthCheckCampaign);
        when(diseaseService.getAllDiseasesById(any())).thenReturn(Collections.singletonList(new Disease()));
        when(diseaseMapper.toHealthCheckDiseaseDtoList(any())).thenReturn(Collections.emptyList());

        HealthCheckCampaignRes result = healthCheckCampaignService.saveHealthCheckCampaign(healthCheckCampaignReq);

        assertNotNull(result);
        assertEquals("Test Campaign", result.getTitle());
        verify(healthCheckDiseaseService).saveHealthCheckDisease(any());
    }

    @Test
    void testUpdateStatusHealthCheckCampaign_Published() {
        when(healthCheckCampaignRepo.findById(1L)).thenReturn(Optional.of(healthCheckCampaign));
        when(healthCheckCampaignRepo.findCurrentCampaignByStatus(any(Integer.class), any())).thenReturn(Optional.empty());
        when(healthCheckCampaignRepo.save(any(HealthCheckCampaign.class))).thenReturn(healthCheckCampaign);
        when(pupilService.getAll()).thenReturn(Collections.singletonList(new Pupil()));
        when(userService.findAllWithPupilByParent()).thenReturn(Collections.singletonList(new User()));

        healthCheckCampaignService.updateStatusHealthCheckCampaign(1L, StatusHealthCampaign.PUBLISHED);

        verify(healthCheckCampaignRepo).save(any(HealthCheckCampaign.class));
        verify(userNotificationService).saveAllUserNotifications(any());
    }

    @Test
    void testUpdateStatusHealthCheckCampaign_Published_AlreadyExists() {
        when(healthCheckCampaignRepo.findById(1L)).thenReturn(Optional.of(healthCheckCampaign));
        when(healthCheckCampaignRepo.findCurrentCampaignByStatus(any(Integer.class), any())).thenReturn(Optional.of(new HealthCheckCampaign()));

        assertThrows(UpdateNotAllowedException.class, () -> {
            healthCheckCampaignService.updateStatusHealthCheckCampaign(1L, StatusHealthCampaign.PUBLISHED);
        });
    }

    @Test
    void testUpdateStatusHealthCheckCampaign_InProgress_AlreadyExists() {
        when(healthCheckCampaignRepo.findById(1L)).thenReturn(Optional.of(healthCheckCampaign));
        when(healthCheckCampaignRepo.findCurrentCampaignByStatus(any(Integer.class), any())).thenReturn(Optional.of(new HealthCheckCampaign()));

        assertThrows(UpdateNotAllowedException.class, () -> {
            healthCheckCampaignService.updateStatusHealthCheckCampaign(1L, StatusHealthCampaign.IN_PROGRESS);
        });
    }

    @Test
    void testUpdateStatusHealthCheckCampaign_InProgress() {
        when(healthCheckCampaignRepo.findById(1L)).thenReturn(Optional.of(healthCheckCampaign));
        when(healthCheckCampaignRepo.findCurrentCampaignByStatus(any(Integer.class), any())).thenReturn(Optional.empty());
        when(healthCheckCampaignRepo.save(any(HealthCheckCampaign.class))).thenReturn(healthCheckCampaign);

        healthCheckCampaignService.updateStatusHealthCheckCampaign(1L, StatusHealthCampaign.IN_PROGRESS);

        verify(healthCheckCampaignRepo).save(any(HealthCheckCampaign.class));
    }

    @Test
    void testUpdateStatusHealthCheckCampaign_Cancelled_NotAllowed() {
        healthCheckCampaign.setStatusHealthCampaign(StatusHealthCampaign.IN_PROGRESS);
        when(healthCheckCampaignRepo.findById(1L)).thenReturn(Optional.of(healthCheckCampaign));

        assertThrows(UpdateNotAllowedException.class, () -> {
            healthCheckCampaignService.updateStatusHealthCheckCampaign(1L, StatusHealthCampaign.CANCELLED);
        });
    }

    @Test
    void testUpdateStatusHealthCheckCampaign_Cancelled_FromPublished() {
        healthCheckCampaign.setStatusHealthCampaign(StatusHealthCampaign.PUBLISHED);
        when(healthCheckCampaignRepo.findById(1L)).thenReturn(Optional.of(healthCheckCampaign));
        when(healthCheckCampaignRepo.save(any(HealthCheckCampaign.class))).thenReturn(healthCheckCampaign);
        when(userService.findAllWithPupilByParent()).thenReturn(Collections.singletonList(new User()));

        healthCheckCampaignService.updateStatusHealthCheckCampaign(1L, StatusHealthCampaign.CANCELLED);

        verify(healthCheckCampaignRepo).save(any(HealthCheckCampaign.class));
        verify(userNotificationService).saveAllUserNotifications(any());
    }

    @Test
    void testUpdateStatusHealthCheckCampaign_Completed() {
        when(healthCheckCampaignRepo.findById(1L)).thenReturn(Optional.of(healthCheckCampaign));
        when(healthCheckCampaignRepo.save(any(HealthCheckCampaign.class))).thenReturn(healthCheckCampaign);
        when(userService.findAllWithPupilByParent()).thenReturn(Collections.singletonList(new User()));

        healthCheckCampaignService.updateStatusHealthCheckCampaign(1L, StatusHealthCampaign.COMPLETED);

        verify(healthCheckCampaignRepo).save(any(HealthCheckCampaign.class));
        verify(userNotificationService).saveAllUserNotifications(any());
    }

    @Test
    void testUpdateHealthCheckCampaignAndDiseases() {
        when(healthCheckCampaignRepo.findById(1L)).thenReturn(Optional.of(healthCheckCampaign));
        when(diseaseService.getAllDiseasesById(any())).thenReturn(Collections.singletonList(new Disease()));
        when(healthCheckCampaignRepo.save(any(HealthCheckCampaign.class))).thenReturn(healthCheckCampaign);
        when(healthCheckCampaignMapper.toDto(any(HealthCheckCampaign.class))).thenReturn(healthCheckCampaignRes);

        HealthCheckCampaignRes result = healthCheckCampaignService.updateHealthCheckCampaignAndDiseases(1L, healthCheckCampaignReq);

        assertNotNull(result);
        assertEquals("Test Campaign", result.getTitle());
    }

    @Test
    void testGetAllHealthCheckCampaigns() {
        when(healthCheckCampaignRepo.findAllByActiveTrue()).thenReturn(Collections.singletonList(healthCheckCampaign));
        when(healthCheckCampaignMapper.toDto(any(List.class))).thenReturn(Collections.singletonList(healthCheckCampaignRes));

        List<HealthCheckCampaignRes> result = healthCheckCampaignService.getAllHealthCheckCampaigns();

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
