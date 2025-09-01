package pl.mbalcer.luxmedreservation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.mbalcer.luxmedreservation.client.model.TermsInfoForDay;
import pl.mbalcer.luxmedreservation.term.TermSearchService;

import java.util.List;

@Component
@Slf4j
public class AppointmentAvailabilityScheduler {

    private final AppointmentAvailabilityService appointmentAvailabilityService;
    private final TermSearchService termSearchService;

    public AppointmentAvailabilityScheduler(AppointmentAvailabilityService appointmentAvailabilityService, TermSearchService termSearchService) {
        this.appointmentAvailabilityService = appointmentAvailabilityService;
        this.termSearchService = termSearchService;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void checkPeriodically() {
        log.info("🔁 Automatyczne sprawdzanie dostępnych terminów (CRON)...");

        try {
            termSearchService.listSearching()
                    .forEach(termSearch -> {
                        List<TermsInfoForDay> termsInfoForDays = appointmentAvailabilityService.checkTerms(termSearch);
                        log.info("Znaleziono nowe terminy: " + termsInfoForDays);
                    });
        } catch (Exception e) {
            log.error("❌ Błąd podczas automatycznego sprawdzania terminów", e);
        }
    }
}
