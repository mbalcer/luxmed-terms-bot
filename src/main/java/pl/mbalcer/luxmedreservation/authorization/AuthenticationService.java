package pl.mbalcer.luxmedreservation.authorization;

import org.openqa.selenium.Cookie;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {
    private final EncryptionService encryptionService;
    private final SessionProvider sessionProvider;

    public AuthenticationService(EncryptionService encryptionService, SessionProvider sessionProvider) {
        this.encryptionService = encryptionService;
        this.sessionProvider = sessionProvider;
    }

    public List<Cookie> authenticateAndGetCookies(LoginRequest encryptedUser) {
        LoginRequest loginRequest = new LoginRequest(
            encryptedUser.email(), 
            encryptionService.decrypt(encryptedUser.password())
        );
        return sessionProvider.loginAndGetCookies(loginRequest);
    }
}