package com.schoolhealth.schoolmedical.service.sheduler;

import com.schoolhealth.schoolmedical.service.vaccinationConsentForm.VaccinationConsentFormService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VaccinationSchedulerService {
    private final VaccinationConsentFormService consentFormService;

    /**
     * Chạy mỗi đêm lúc 2:00 AM để tự động cập nhật các phiếu đồng ý quá hạn
     * từ WAITING -> REJECTED
     */
    @Scheduled(cron = "0 0 2 * * *")
    public void updateExpiredConsentForms() {
        log.info("Starting scheduled task to update expired consent forms");

        try {
            int updatedCount = consentFormService.updateExpiredConsentForms();
            log.info("Scheduled task completed: {} expired consent forms updated", updatedCount);
        } catch (Exception e) {
            log.error("Error during scheduled task execution", e);
        }
    }

    /**
     * Chạy mỗi giờ để kiểm tra và cập nhật trạng thái chiến dịch
     * (có thể mở rộng thêm logic khác trong tương lai)
     */
    @Scheduled(fixedRate = 3600000) // 1 hour
    public void healthCheck() {
        log.debug("Vaccination system health check - scheduler is running");
    }
}
