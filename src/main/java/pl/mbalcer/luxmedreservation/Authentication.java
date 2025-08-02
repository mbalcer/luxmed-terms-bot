package pl.mbalcer.luxmedreservation;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Authentication {
    @Value("${luxmed.email}")
    private String email;

    @Value("${luxmed.password}")
    private String password;

    private final WebDriver driver;

    public Authentication(WebDriver driver) {
        this.driver = driver;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void listen() {
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

        System.out.println("Cookies: " + seleniumCookies);
        driver.quit();
    }
}
