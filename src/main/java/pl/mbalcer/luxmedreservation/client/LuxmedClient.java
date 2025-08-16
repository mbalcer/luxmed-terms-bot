package pl.mbalcer.luxmedreservation.client;

import org.openqa.selenium.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import pl.mbalcer.luxmedreservation.client.model.MedicalAppointmentResponse;

import java.util.List;

@Component
public class LuxmedClient {
    private final RestClient restClient;

    public LuxmedClient() {
        this.restClient = RestClient.builder()
                .defaultHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public MedicalAppointmentResponse sendCheckTermsRequest(String url, List<Cookie> seleniumCookies) {
        StringBuilder cookieHeader = new StringBuilder();
        for (Cookie cookie : seleniumCookies) {
            cookieHeader.append(cookie.getName()).append("=").append(cookie.getValue()).append("; ");
        }

        return restClient.get()
                .uri(url)
                .header("Cookie", cookieHeader.toString())
                .retrieve()
                .body(MedicalAppointmentResponse.class);
    }
}
