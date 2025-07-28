package com.schoolhealth.schoolmedical.service.dashboard;

import com.schoolhealth.schoolmedical.model.dto.response.*;
import com.schoolhealth.schoolmedical.model.mapper.DashboardMapper;
import com.schoolhealth.schoolmedical.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final PupilRepo pupilRepo;
    private final HealthCheckHistoryRepo healthCheckHistoryRepo;
    private final VaccinationHistoryRepo vaccinationHistoryRepo;
    private final MedicalEventRepository medicalEventRepository;
    private final SendMedicationRepo sendMedicationRepo;
    private final HealthCheckCampaignRepo healthCheckCampaignRepo;
    private final DashboardMapper dashboardMapper;

    @Override
    public DashboardStatisticsResponse getDashboardStatistics(int year) {
        log.info("Getting dashboard statistics for year: {}", year);

        try {
            // Get basic counts
            Long totalPupils = pupilRepo.countActivePupilsByYear(year);
            Long totalHealthChecks = healthCheckHistoryRepo.countCompletedHealthCheckCampaignsByYear(year);
            Long totalVaccinations = vaccinationHistoryRepo.countCompletedVaccinationCampaignsByYear(year);
            Long totalMedicalEvents = medicalEventRepository.countMedicalEventsByYear(year);

            log.debug("Basic counts - Pupils: {}, Health Check Campaigns: {}, Vaccination Campaigns: {}, Medical Events: {}",
                    totalPupils, totalHealthChecks, totalVaccinations, totalMedicalEvents);

            // Get prescription statistics
            Long prescriptionCount = sendMedicationRepo.countPupilsWithPrescriptionsByYear(year);
            List<String> commonDiseaseTypes = sendMedicationRepo.getCommonDiseaseTypesByYear(year);

            // Limit to top 5 common types to match frontend example
            List<String> topCommonTypes = commonDiseaseTypes.size() > 5 ?
                    commonDiseaseTypes.subList(0, 5) : commonDiseaseTypes;

            PrescriptionStatsDto prescriptionStats = PrescriptionStatsDto.builder()
                    .count(prescriptionCount)
                    .commonTypes(topCommonTypes)
                    .build();

            // Get campaign statistics
            List<Object[]> campaignData = healthCheckCampaignRepo.getCampaignStatsByYear(year);
            List<CampaignStatisticsDto> campaigns = dashboardMapper.mapToCampaignStatistics(campaignData);

            // Get vaccination statistics
            List<Object[]> vaccinationData = vaccinationHistoryRepo.getVaccinationStatsByYear(year);
            List<VaccinationStatisticsDto> vaccinations = dashboardMapper.mapToVaccinationStatistics(vaccinationData);

            // Get medical event statistics by month
            List<Object[]> eventData = medicalEventRepository.getEventStatsByMonthAndYear(year);
            List<EventStatisticsDto> events = dashboardMapper.mapToEventStatistics(eventData);

            log.info("Successfully retrieved dashboard statistics for year: {}", year);

            return DashboardStatisticsResponse.builder()
                    .totalPupils(totalPupils)
                    .totalHealthChecks(totalHealthChecks)
                    .totalVaccinations(totalVaccinations)
                    .totalMedicalEvents(totalMedicalEvents)
                    .prescriptionsLastMonth(prescriptionStats)
                    .campaigns(campaigns)
                    .vaccinations(vaccinations)
                    .events(events)
                    .build();

        } catch (Exception e) {
            log.error("Error getting dashboard statistics for year {}: {}", year, e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve dashboard statistics", e);
        }
    }
}
