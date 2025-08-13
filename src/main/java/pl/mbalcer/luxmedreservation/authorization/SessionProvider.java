package pl.mbalcer.luxmedreservation.authorization;

import org.openqa.selenium.Cookie;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface SessionProvider {
    @Cacheable("luxmedCookies")
    List<Cookie> loginAndGetCookies(LoginRequest loginRequest);
}
