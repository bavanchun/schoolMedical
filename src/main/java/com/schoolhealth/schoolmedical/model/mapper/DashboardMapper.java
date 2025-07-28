package com.schoolhealth.schoolmedical.model.mapper;

import com.schoolhealth.schoolmedical.model.dto.response.CampaignStatisticsDto;
import com.schoolhealth.schoolmedical.model.dto.response.EventStatisticsDto;
import com.schoolhealth.schoolmedical.model.dto.response.VaccinationStatisticsDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Spring component for dashboard statistics data transformation
 */
@Component
public class DashboardMapper {

    /**
     * Map campaign query results to CampaignStatisticsDto list
     * @param campaignData List of Object arrays [title, pupilCount]
     * @return List of CampaignStatisticsDto
     */
    public List<CampaignStatisticsDto> mapToCampaignStatistics(List<Object[]> campaignData) {
        return campaignData.stream()
                .map(this::mapToCampaignStatistic)
                .collect(Collectors.toList());
    }

    /**
     * Map single campaign Object array to CampaignStatisticsDto
     * @param data Object array [title, pupilCount]
     * @return CampaignStatisticsDto
     */
    public CampaignStatisticsDto mapToCampaignStatistic(Object[] data) {
        return CampaignStatisticsDto.builder()
                .title((String) data[0])
                .pupilCount(((Number) data[1]).longValue())
                .build();
    }

    /**
     * Map vaccination query results to VaccinationStatisticsDto list
     * @param vaccinationData List of Object arrays [vaccineName, count]
     * @return List of VaccinationStatisticsDto
     */
    public List<VaccinationStatisticsDto> mapToVaccinationStatistics(List<Object[]> vaccinationData) {
        return vaccinationData.stream()
                .map(this::mapToVaccinationStatistic)
                .collect(Collectors.toList());
    }

    /**
     * Map single vaccination Object array to VaccinationStatisticsDto
     * @param data Object array [vaccineName, count]
     * @return VaccinationStatisticsDto
     */
    public VaccinationStatisticsDto mapToVaccinationStatistic(Object[] data) {
        return VaccinationStatisticsDto.builder()
                .vaccine((String) data[0])
                .count(((Number) data[1]).longValue())
                .build();
    }

    /**
     * Map event query results to EventStatisticsDto list
     * @param eventData List of Object arrays [monthName, eventCount]
     * @return List of EventStatisticsDto
     */
    public List<EventStatisticsDto> mapToEventStatistics(List<Object[]> eventData) {
        return eventData.stream()
                .map(this::mapToEventStatistic)
                .collect(Collectors.toList());
    }

    /**
     * Map single event Object array to EventStatisticsDto
     * @param data Object array [monthName, eventCount]
     * @return EventStatisticsDto
     */
    public EventStatisticsDto mapToEventStatistic(Object[] data) {
        return EventStatisticsDto.builder()
                .date((String) data[0])
                .eventCount(((Number) data[1]).longValue())
                .build();
    }
}
