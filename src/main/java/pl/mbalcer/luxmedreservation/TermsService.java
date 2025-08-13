package pl.mbalcer.luxmedreservation;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import pl.mbalcer.luxmedreservation.authorization.SessionProvider;
import pl.mbalcer.luxmedreservation.model.MedicalAppointmentResponse;
import pl.mbalcer.luxmedreservation.model.TermsInfoForDay;
import pl.mbalcer.luxmedreservation.notification.MessageBuilder;
import pl.mbalcer.luxmedreservation.notification.TelegramNotifier;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TermsService {
    private final SessionProvider sessionProvider;
    private final TelegramNotifier telegramNotifier;
    private final MessageBuilder messageBuilder;

    public TermsService(SessionProvider sessionProvider, TelegramNotifier telegramNotifier, MessageBuilder messageBuilder) {
        this.sessionProvider = sessionProvider;
        this.telegramNotifier = telegramNotifier;
        this.messageBuilder = messageBuilder;
    }

    public List<TermsInfoForDay> checkTerms() {
//        List<Cookie> seleniumCookies = sessionProvider.loginAndGetCookies();
        StringBuilder cookieHeader = new StringBuilder();
//        for (Cookie cookie : seleniumCookies) {
//            cookieHeader.append(cookie.getName()).append("=").append(cookie.getValue()).append("; ");
//        }

        RestClient restClient = RestClient.builder()
                .defaultHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();

        String url = new LuxmedUrlBuilder()
                .searchPlaceId(10)
                .serviceVariantId(4461)
                .searchDateFrom(LocalDate.now().plusDays(1))
                .searchDateTo(LocalDate.now().plusDays(15))
                .addDoctorId(15665)
                .addDoctorId(37970)
                .delocalized(false)
                .build();

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

        String message = messageBuilder.buildMessage(availableDays);
        if (!message.isEmpty()) {
            telegramNotifier.sendMessage(message);
            log.debug("Message to Telegram: " + message);
        }

        return availableDays;
    }

    public List<String> getRecentChecks() {
        return messageBuilder.getTimes().stream().map(OffsetDateTime::toString).collect(Collectors.toList());
    }
}
