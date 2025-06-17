package com.schoolhealth.schoolmedical.service.HealthCheckHistory;

import com.schoolhealth.schoolmedical.entity.HealthCheckHistory;
import com.schoolhealth.schoolmedical.repository.HealthCheckHistoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HealthCheckHistoryImpl implements HealthCheckHistoryService {
    @Autowired
    private HealthCheckHistoryRepo healthCheckHistoryRepo;

    @Override
    public HealthCheckHistory saveHealthCheckHistory(HealthCheckHistory healthCheckHistory) {
        return healthCheckHistoryRepo.save(healthCheckHistory);
    }
}
