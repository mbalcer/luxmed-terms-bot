package pl.mbalcer.luxmedreservation;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AuthenticationService {
    @Value("${luxmed.email}")
    private String email;

    @Value("${luxmed.password}")
    private String password;

    private final ObjectFactory<WebDriver> webDriverFactory;

    public AuthenticationService(ObjectFactory<WebDriver> webDriverFactory) {
        this.webDriverFactory = webDriverFactory;
    }

    public List<Cookie> loginAndGetCookies() {
        WebDriver driver = webDriverFactory.getObject();

        driver.get("https://portalpacjenta.luxmed.pl");
        driver.findElement(By.id("Login")).sendKeys(email);
        driver.findElement(By.id("Password")).sendKeys(password);
        driver.findElement(By.id("LoginSubmit")).click();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<Cookie> seleniumCookies = driver.manage().getCookies().stream().toList();

        log.debug("Cookies: {}", seleniumCookies);
        driver.quit();
        return seleniumCookies;
    }
}
