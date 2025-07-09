package com.schoolhealth.schoolmedical.service.sheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VaccinationSchedulerService {
//    private final VaccinationConsentFormService consentFormService;

    /**
     * Chạy mỗi đêm lúc 2:00 AM để tự động cập nhật các phiếu đồng ý quá hạn
     * từ WAITING -> REJECTED
     */
//    @Scheduled(cron = "0 0 2 * * *")
//    public void updateExpiredConsentForms() {
//        log.info("Starting scheduled task to update expired consent forms");
//
//        try {
//            int updatedCount = consentFormService.updateExpiredConsentForms();
//            log.info("Scheduled task completed: {} expired consent forms updated", updatedCount);
//        } catch (Exception e) {
//            log.error("Error during scheduled task execution", e);
//        }
//    }
    // Removed: VaccinationConsentFormService dependency - no longer needed
    // Removed: updateExpiredConsentForms() - business logic changed to default REJECTED status

    /**
     * Health check scheduler - can be extended for future monitoring needs
     * Chạy mỗi giờ để kiểm tra hệ thống
     */
    @Scheduled(fixedRate = 3600000) // 1 hour
    public void healthCheck() {
        log.debug("Vaccination system health check - scheduler is running");
    }
}
