package pl.mbalcer.luxmedreservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LuxmedReservationApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuxmedReservationApplication.class, args);
    }

}
