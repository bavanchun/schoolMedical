package com.schoolhealth.schoolmedical.service.HealthCheckHistory;

import com.schoolhealth.schoolmedical.entity.HealthCheckHistory;

public interface HealthCheckHistoryService {
    HealthCheckHistory saveHealthCheckHistory(HealthCheckHistory healthCheckHistory, Long healthCheckConsentId);
}
