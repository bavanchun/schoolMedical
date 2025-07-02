package com.schoolhealth.schoolmedical.service.HealthCheckHistory;

import com.schoolhealth.schoolmedical.entity.HealthCheckHistory;
import com.schoolhealth.schoolmedical.model.dto.request.HealthCheckHistoryReq;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckConsentRes;
import com.schoolhealth.schoolmedical.model.dto.response.HealthCheckHistoryRes;

public interface HealthCheckHistoryService {
    HealthCheckHistory saveHealthCheckHistory(HealthCheckHistoryReq healthCheckHistoryReq, Long healthCheckConsentId);
    HealthCheckHistoryRes getHealthCheckHistoryByPupilIdAndSchoolYear(String pupilId, int schoolYear);
}
