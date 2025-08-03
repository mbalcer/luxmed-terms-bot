package pl.mbalcer.luxmedreservation;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import pl.mbalcer.luxmedreservation.model.MedicalAppointmentResponse;
import pl.mbalcer.luxmedreservation.model.TermsInfoForDay;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/terms")
@Slf4j
public class TermsApi {
    private final SessionProvider sessionProvider;

    public TermsApi(SessionProvider sessionProvider) {
        this.sessionProvider = sessionProvider;
    }

    @GetMapping("/check")
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
        String url = "https://portalpacjenta.luxmed.pl/PatientPortal/NewPortal/terms/index?searchPlace.id=10&searchPlace.name=Bydgoszcz&searchPlace.type=0&serviceVariantId=4461&languageId=10&searchDateFrom=2025-08-02&searchDateTo=2025-08-28&doctorsIds=15665&doctorsIds=37970&nextSearch=false&delocalized=false";

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

        availableDays.forEach(termsInfoForDay -> log.info("Available day: {} with {} slots", termsInfoForDay.day(), termsInfoForDay.termsCounter().termsNumber()));

        return availableDays;
    }
}
