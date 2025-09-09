package pl.mbalcer.luxmedreservation;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Cookie;
import org.springframework.stereotype.Service;
import pl.mbalcer.luxmedreservation.authorization.AuthenticationService;
import pl.mbalcer.luxmedreservation.client.LuxmedClient;
import pl.mbalcer.luxmedreservation.client.TermSearchToLuxmedUrlMapper;
import pl.mbalcer.luxmedreservation.client.model.MedicalAppointmentResponse;
import pl.mbalcer.luxmedreservation.client.model.TermsInfoForDay;
import pl.mbalcer.luxmedreservation.notification.NotificationService;
import pl.mbalcer.luxmedreservation.term.TermSearchDTO;
import pl.mbalcer.luxmedreservation.term.TermsProcessingService;

import java.util.List;

@Service
@Slf4j
public class AppointmentAvailabilityService {
    private final LuxmedClient luxmedClient;
    private final AuthenticationService authenticationService;
    private final TermsProcessingService termsProcessingService;
    private final NotificationService notificationService;

    public AppointmentAvailabilityService(LuxmedClient luxmedClient, 
                                        AuthenticationService authenticationService,
                                        TermsProcessingService termsProcessingService, 
                                        NotificationService notificationService) {
        this.luxmedClient = luxmedClient;
        this.authenticationService = authenticationService;
        this.termsProcessingService = termsProcessingService;
        this.notificationService = notificationService;
    }

    public List<TermsInfoForDay> checkTerms(TermSearchDTO termSearchDTO) {
        List<Cookie> cookies = authenticationService.authenticateAndGetCookies(termSearchDTO.user());
        String url = TermSearchToLuxmedUrlMapper.mapDtoToUrl(termSearchDTO);
        MedicalAppointmentResponse response = luxmedClient.sendCheckTermsRequest(url, cookies);

        log.debug("Response from Luxmed is success = {}", response.success());

        List<TermsInfoForDay> availableDays = termsProcessingService.extractAvailableTerms(response);
        notificationService.notifyIfAvailableTermsFound(availableDays);

        return availableDays;
    }
}
