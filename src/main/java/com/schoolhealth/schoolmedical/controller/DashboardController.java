package com.schoolhealth.schoolmedical.controller;

import com.schoolhealth.schoolmedical.model.dto.response.DashboardStatisticsResponse;
import com.schoolhealth.schoolmedical.service.dashboard.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Dashboard", description = "Dashboard statistics operations")
@Slf4j
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * Get dashboard statistics for a specific year
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN') or hasRole('SCHOOL_NURSE')")
    @Operation(
            summary = "Get dashboard statistics",
            description = "Retrieve comprehensive dashboard statistics including pupils, health checks, vaccinations, medical events, and chart data for a specific school year"
    )
    public ResponseEntity<DashboardStatisticsResponse> getDashboardStatistics(
            @Parameter(description = "School year for statistics (default: current year)", example = "2025")
            @RequestParam(value = "year", required = false) Integer year) {

        // Default to current year if not provided
        int targetYear = (year != null) ? year : java.time.Year.now().getValue();

        log.info("Received request for dashboard statistics for year: {} (provided: {})", targetYear, year);

        try {
            DashboardStatisticsResponse response = dashboardService.getDashboardStatistics(targetYear);
            log.info("Successfully retrieved dashboard statistics for year: {}", targetYear);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error retrieving dashboard statistics for year {}: {}", targetYear, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get dashboard statistics for current year (deprecated - use /statistics without year parameter)
     */
    @GetMapping("/statistics/current")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN') or hasRole('SCHOOL_NURSE')")
    @Operation(
            summary = "Get current year dashboard statistics (deprecated)",
            description = "Retrieve dashboard statistics for the current school year. Use /statistics without year parameter instead."
    )
    @Deprecated
    public ResponseEntity<DashboardStatisticsResponse> getCurrentYearDashboardStatistics() {

        int currentYear = java.time.Year.now().getValue();
        log.info("Received request for current year dashboard statistics: {}", currentYear);

        try {
            DashboardStatisticsResponse response = dashboardService.getDashboardStatistics(currentYear);
            log.info("Successfully retrieved current year dashboard statistics");

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error retrieving current year dashboard statistics: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
