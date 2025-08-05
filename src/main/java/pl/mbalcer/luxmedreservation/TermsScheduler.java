package pl.mbalcer.luxmedreservation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TermsScheduler {

    private final TermsService termsService;

    public TermsScheduler(TermsService termsService) {
        this.termsService = termsService;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void checkPeriodically() {
        try {
            log.info("🔁 Automatyczne sprawdzanie dostępnych terminów (CRON)...");
            termsService.checkTerms();
        } catch (Exception e) {
            log.error("❌ Błąd podczas automatycznego sprawdzania terminów", e);
        }
    }
}
