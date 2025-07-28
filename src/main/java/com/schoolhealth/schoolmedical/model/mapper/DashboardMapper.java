package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.model.dto.response.EventStatisticsDto;
import com.schoolhealth.schoolmedical.model.dto.response.VaccinationCampaignStatisticsDto;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckCampaignStatisticsDto;
import com.schoolhealth.schoolmedical.entity.enums.ConsentFormStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * MapStruct mapper for dashboard statistics data transformation
 */
@Mapper(componentModel = "spring")
public interface DashboardMapper {

    /**
     * Map health check campaign query results to HealthCheckCampaignStatisticsDto list
     * @param campaignData List of Object arrays [campaign_title, total_consent_forms, total_examinations]
     * @return List of HealthCheckCampaignStatisticsDto
     */
    default List<HealthCheckCampaignStatisticsDto> mapToHealthCheckCampaignStatistics(List<Object[]> campaignData) {
        return campaignData.stream()
                .map(data -> {
                    String campaignTitle = (String) data[0];
                    Long totalConsentForms = ((Number) data[1]).longValue();
                    Long totalExaminations = ((Number) data[2]).longValue();

                    // Business logic for Health Check Campaigns:
                    // - totalPupils: all pupils who received notifications (consent forms created)
                    // - examinedCount: pupils who completed health check (have health history)
                    // - absentCount: pupils who received notification but haven't been examined
                    // - agreedCount: all pupils notified (implicit agreement when consent form created)
                    // - disagreedCount: 0 (no explicit disagreement mechanism in health check)

                    Long totalPupils = totalConsentForms;  // Total pupils notified
                    Long examinedCount = totalExaminations;  // Pupils who completed health check
                    Long absentCount = totalPupils - examinedCount;  // Notified but not examined

                    // For health check campaigns, consent form creation implies agreement
                    // There's no explicit disagree mechanism like in vaccination campaigns
                    Long agreedCount = totalPupils;
                    Long disagreedCount = 0L;

                    return HealthCheckCampaignStatisticsDto.builder()
                            .campaignTitle(campaignTitle)
                            .agreedCount(agreedCount)
                            .disagreedCount(disagreedCount)
                            .examinedCount(examinedCount)
                            .absentCount(absentCount)
                            .totalPupils(totalPupils)
                            .build();
                })
                .collect(Collectors.toList());
    }


    /**
     * Map event query results to EventStatisticsDto list
     * @param eventData List of Object arrays [monthName, eventCount]
     * @return List of EventStatisticsDto
     */
    default List<EventStatisticsDto> mapToEventStatistics(List<Object[]> eventData) {
        return eventData.stream()
                .map(this::mapToEventStatistic)
                .collect(Collectors.toList());
    }

    /**
     * Map single event Object array to EventStatisticsDto
     * @param data Object array [monthName, eventCount]
     * @return EventStatisticsDto
     */
    @Named("mapEventStatistic")
    default EventStatisticsDto mapToEventStatistic(Object[] data) {
        return EventStatisticsDto.builder()
                .date((String) data[0])
                .eventCount(((Number) data[1]).longValue())
                .build();
    }

    /**
     * Map vaccination campaign query results to VaccinationCampaignStatisticsDto list
     * @param vaccinationCampaignData List of Object arrays [campaign_title, disease_name, vaccine_name, consent_status, count]
     * @return List of VaccinationCampaignStatisticsDto
     */
    default List<VaccinationCampaignStatisticsDto> mapToVaccinationCampaignStatistics(List<Object[]> vaccinationCampaignData) {
        // Group by campaign (title, disease, vaccine) and aggregate counts by status
        Map<String, Map<ConsentFormStatus, Long>> campaignStatsMap = vaccinationCampaignData.stream()
                .collect(Collectors.groupingBy(
                        data -> (String) data[0] + "|" + (String) data[1] + "|" + (String) data[2], // campaign key
                        Collectors.groupingBy(
                                data -> (ConsentFormStatus) data[3], // status
                                Collectors.summingLong(data -> ((Number) data[4]).longValue()) // count
                        )
                ));

        // Convert to DTO list
        return campaignStatsMap.entrySet().stream()
                .map(entry -> {
                    String[] campaignInfo = entry.getKey().split("\\|");
                    String campaignTitle = campaignInfo[0];
                    String diseaseName = campaignInfo[1];
                    String vaccineName = campaignInfo[2];

                    Map<ConsentFormStatus, Long> statusCounts = entry.getValue();

                    // Calculate statistics based on business logic
                    Long approvedCount = statusCounts.getOrDefault(ConsentFormStatus.APPROVED, 0L);
                    Long injectedCount = statusCounts.getOrDefault(ConsentFormStatus.INJECTED, 0L);
                    Long noShowCount = statusCounts.getOrDefault(ConsentFormStatus.NO_SHOW, 0L);
                    Long rejectedCount = statusCounts.getOrDefault(ConsentFormStatus.REJECTED, 0L);
                    Long waitingCount = statusCounts.getOrDefault(ConsentFormStatus.WAITING, 0L);

                    // Business logic for counts:
                    // - totalPupils: ALL consent forms created (all statuses)
                    // - agreedCount: APPROVED + INJECTED + NO_SHOW (pupils who initially approved)
                    // - disagreedCount: REJECTED + WAITING
                    // - vaccinatedCount: INJECTED only
                    // - absentCount: NO_SHOW only
                    Long totalPupils = approvedCount + injectedCount + noShowCount + rejectedCount + waitingCount;
                    Long agreedCount = approvedCount + injectedCount + noShowCount;
                    Long disagreedCount = rejectedCount + waitingCount;

                    return VaccinationCampaignStatisticsDto.builder()
                            .campaignTitle(campaignTitle)
                            .diseaseName(diseaseName)
                            .vaccineName(vaccineName)
                            .agreedCount(agreedCount)
                            .disagreedCount(disagreedCount)
                            .vaccinatedCount(injectedCount)
                            .absentCount(noShowCount)
                            .totalPupils(totalPupils)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
