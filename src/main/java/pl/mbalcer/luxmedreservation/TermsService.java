package pl.mbalcer.luxmedreservation;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import pl.mbalcer.luxmedreservation.model.MedicalAppointmentResponse;
import pl.mbalcer.luxmedreservation.model.TermsInfoForDay;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class TermsService {
    private final SessionProvider sessionProvider;
    private final TelegramNotifier telegramNotifier;

    public TermsService(SessionProvider sessionProvider, TelegramNotifier telegramNotifier) {
        this.sessionProvider = sessionProvider;
        this.telegramNotifier = telegramNotifier;
    }

    public List<TermsInfoForDay> checkTerms() {
        List<Cookie> seleniumCookies = sessionProvider.loginAndGetCookies();
        StringBuilder cookieHeader = new StringBuilder();
        for (Cookie cookie : seleniumCookies) {
            cookieHeader.append(cookie.getName()).append("=").append(cookie.getValue()).append("; ");
        }

// Creating RestClient with default headers
        RestClient restClient = RestClient.builder()
                .defaultHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();

// URL with parameters for checking appointments
        String url = "https://portalpacjenta.luxmed.pl/PatientPortal/NewPortal/terms/index?searchPlace.id=10&searchPlace.name=Bydgoszcz&searchPlace.type=0&serviceVariantId=4461&languageId=10&searchDateFrom=2025-08-12&searchDateTo=2025-08-28&doctorsIds=15665&doctorsIds=37970&nextSearch=false&delocalized=false";

// Making the request with RestClient
        MedicalAppointmentResponse response = restClient.get()
                .uri(url)
                .header("Cookie", cookieHeader.toString())
                .retrieve()
                .body(MedicalAppointmentResponse.class);

        log.debug(String.valueOf(response));

        List<TermsInfoForDay> availableDays = Optional.ofNullable(Objects.requireNonNull(response).termsForService())
                .orElseThrow()
                .termsInfoForDays()
                .stream()
                .filter(day -> day.termsCounter().termsNumber() > 0)
                .toList();

        if (!availableDays.isEmpty()) {
            telegramNotifier.sendMessage("Znaleziono nowe terminy: " + availableDays);
            availableDays.forEach(termsInfoForDay -> log.info("Available day: {} with {} slots", termsInfoForDay.day(), termsInfoForDay.termsCounter().termsNumber()));
        } else {
            telegramNotifier.sendMessage("Szukałem właśnie nowych terminów, ale nic nie znalazłem.");
        }

        return availableDays;
    }
}
