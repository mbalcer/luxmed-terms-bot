package pl.mbalcer.luxmedreservation.authorization;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
@Slf4j
public class LuxmedSessionService implements SessionProvider {
    private final ObjectFactory<WebDriver> webDriverFactory;

    public LuxmedSessionService(ObjectFactory<WebDriver> webDriverFactory) {
        this.webDriverFactory = webDriverFactory;
    }

    @Override
    public List<Cookie> loginAndGetCookies(LoginRequest loginRequest) {
        WebDriver driver = webDriverFactory.getObject();
        driver.get("https://portalpacjenta.luxmed.pl");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement loginInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Login")));
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Password")));
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("LoginSubmit")));

        loginInput.sendKeys(loginRequest.email());
        passwordInput.sendKeys(loginRequest.password());
        loginButton.click();

        wait.until(ExpectedConditions.urlContains("/Dashboard"));

        List<Cookie> seleniumCookies = driver.manage().getCookies().stream().toList();

        log.debug("Cookies: {}", seleniumCookies);
        driver.quit();
        return seleniumCookies;
    }
}
