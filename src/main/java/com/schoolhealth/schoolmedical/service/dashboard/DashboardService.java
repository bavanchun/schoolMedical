package com.schoolhealth.schoolmedical.service.dashboard;

import com.schoolhealth.schoolmedical.model.dto.response.DashboardStatisticsResponse;

/**
 * Service interface for dashboard statistics operations
 */
public interface DashboardService {

    /**
     * Get dashboard statistics for a specific year
     * @param year School year to get statistics for
     * @return DashboardStatisticsResponse containing all dashboard data
     */
    DashboardStatisticsResponse getDashboardStatistics(int year);
}
