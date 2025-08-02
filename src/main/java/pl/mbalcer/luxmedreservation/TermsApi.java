package pl.mbalcer.luxmedreservation;

import org.openqa.selenium.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequestMapping("/api/terms")
public class TermsApi {
    private final Authentication authentication;

    public TermsApi(Authentication authentication) {
        this.authentication = authentication;
    }

    @GetMapping("/check")
    public String checkTerms() {
        List<Cookie> seleniumCookies = authentication.getSeleniumCookies();
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
        return restClient.get()
                .uri(url)
                .header("Cookie", cookieHeader.toString())
                .retrieve()
                .body(String.class);
    }
}
