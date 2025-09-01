package pl.mbalcer.luxmedreservation;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Cookie;
import org.springframework.stereotype.Service;
import pl.mbalcer.luxmedreservation.authorization.EncryptionService;
import pl.mbalcer.luxmedreservation.authorization.LoginRequest;
import pl.mbalcer.luxmedreservation.authorization.SessionProvider;
import pl.mbalcer.luxmedreservation.client.LuxmedClient;
import pl.mbalcer.luxmedreservation.client.TermSearchToLuxmedUrlMapper;
import pl.mbalcer.luxmedreservation.client.model.MedicalAppointmentResponse;
import pl.mbalcer.luxmedreservation.client.model.TermsInfoForDay;
import pl.mbalcer.luxmedreservation.notification.MessageBuilder;
import pl.mbalcer.luxmedreservation.notification.TelegramNotifier;
import pl.mbalcer.luxmedreservation.term.TermSearchDTO;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class AppointmentAvailabilityService {
    private final LuxmedClient luxmedClient;
    private final TelegramNotifier telegramNotifier;
    private final MessageBuilder messageBuilder;
    private final EncryptionService encryptionService;
    private final SessionProvider sessionProvider;

    public AppointmentAvailabilityService(LuxmedClient luxmedClient, TelegramNotifier telegramNotifier, MessageBuilder messageBuilder, EncryptionService encryptionService, SessionProvider sessionProvider) {
        this.luxmedClient = luxmedClient;
        this.telegramNotifier = telegramNotifier;
        this.messageBuilder = messageBuilder;
        this.encryptionService = encryptionService;
        this.sessionProvider = sessionProvider;
    }

    public List<TermsInfoForDay> checkTerms(TermSearchDTO termSearchDTO) {
        LoginRequest user = termSearchDTO.user(); // User with password encrypted
        LoginRequest loginRequest = new LoginRequest(user.email(), encryptionService.decrypt(user.password()));
        List<Cookie> cookies = sessionProvider.loginAndGetCookies(loginRequest);

        String url = TermSearchToLuxmedUrlMapper.mapDtoToUrl(termSearchDTO);

        MedicalAppointmentResponse response = luxmedClient.sendCheckTermsRequest(url, cookies);

        log.debug("Response from Luxmed is success = {}", response.success());

        List<TermsInfoForDay> availableDays = Optional.ofNullable(Objects.requireNonNull(response).termsForService())
                .orElseThrow()
                .termsInfoForDays()
                .stream()
                .filter(day -> day.termsCounter().termsNumber() > 0)
                .toList();

        Optional<String> message = messageBuilder.buildMessage(availableDays);
        if (message.isPresent()) {
            telegramNotifier.sendMessage(message.get());
            log.debug("Message to Telegram: " + message.get());
        }

        return availableDays;
    }
}
