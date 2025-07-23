package com.schoolhealth.schoolmedical;

import com.schoolhealth.schoolmedical.entity.enums.GradeLevel;
import com.schoolhealth.schoolmedical.exception.NotFoundException;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckConsentFlatData;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckConsentRes;
import com.schoolhealth.schoolmedical.repository.HealthCheckConsentRepo;
import com.schoolhealth.schoolmedical.service.HealthCheckConsentImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HealthCheckConsentServiceTest {

    @InjectMocks
    private HealthCheckConsentImpl healthCheckConsentService;

    @Mock
    private HealthCheckConsentRepo healthCheckConsentRepo;

    private HealthCheckConsentFlatData flatData;

    @BeforeEach
    void setUp() {
        flatData = new HealthCheckConsentFlatData() {
            @Override
            public Long getHealthCheckConsentId() { return 1L; }
            @Override
            public int getSchoolYear() { return 2023; }
            @Override
            public boolean isActive() { return true; }
            @Override
            public String getPupilId() { return "P1"; }
            @Override
            public String getLastName() { return "Last"; }
            @Override
            public String getFirstName() { return "First"; }
            @Override
            public LocalDate getBirthDate() { return LocalDate.of(2000, 5, 10); }
            @Override
            public char getGender() { return 'M'; }
            @Override
            public String getGradeName() { return "Grade 1"; }
            @Override
            public Long getDiseaseId() { return 101L; }
            @Override
            public String getName() { return "Disease 1"; }
            @Override
            public String getDescription() { return "Description 1"; }
            @Override
            public String getNote() { return "Note 1"; }
        };
    }

    @Test
    void testGetHealthCheckConsentByGradeAndSchoolYear() {
        when(healthCheckConsentRepo.findListPupilByGradeAndSchoolYear(GradeLevel.GRADE_1)).thenReturn(List.of(flatData));

        List<HealthCheckConsentRes> result = healthCheckConsentService.getHealthCheckConsentByGradeAndSchoolYear(GradeLevel.GRADE_1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getConsentFormId());
    }

    @Test
    void testGetHealthCheckConsentByGradeAndSchoolYear_NotFound() {
        when(healthCheckConsentRepo.findListPupilByGradeAndSchoolYear(GradeLevel.GRADE_1)).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> {
            healthCheckConsentService.getHealthCheckConsentByGradeAndSchoolYear(GradeLevel.GRADE_1);
        });
    }
}
