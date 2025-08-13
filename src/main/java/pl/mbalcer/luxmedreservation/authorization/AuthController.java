package pl.mbalcer.luxmedreservation.authorization;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Cookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Validated
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final LuxmedSessionService luxmedSessionService;
    private final UserCredentialService userCredentialService;

    public AuthController(LuxmedSessionService luxmedSessionService, UserCredentialService userCredentialService) {
        this.luxmedSessionService = luxmedSessionService;
        this.userCredentialService = userCredentialService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            log.info("Login attempt for {}", request.email());

            List<Cookie> cookies = luxmedSessionService.loginAndGetCookies(request);

            if (cookies == null || cookies.isEmpty()) {
                log.info("Login failed for {}: no cookies returned", request.email());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponse("ERROR", "Invalid credentials or login failed."));
            }

            userCredentialService.save(request.email(), request.password());

            log.info("Login successful for {}; credentials saved/updated", request.email());
            return ResponseEntity.ok(new LoginResponse("SUCCESS", "Logged in successfully."));
        } catch (Exception e) {
            log.warn("Login error for {}: {}", request.email(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("ERROR", "Login process failed. Try again later."));
        }
    }

}
