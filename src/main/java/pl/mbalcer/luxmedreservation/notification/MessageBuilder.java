package pl.mbalcer.luxmedreservation.notification;

import org.springframework.stereotype.Component;
import pl.mbalcer.luxmedreservation.client.model.TermsInfoForDay;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MessageBuilder {

    public Optional<String> buildMessage(List<TermsInfoForDay> termsInfoForDay) {
        if (termsInfoForDay == null || termsInfoForDay.isEmpty()) {
            return Optional.empty();
        } else {
            String summaryTerms = termsInfoForDay.stream()
                    .map(termsInfo -> {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                        String date = LocalDateTime.parse(termsInfo.day(), formatter)
                                .toLocalDate()
                                .toString();
                        int count = termsInfo.termsCounter().termsNumber();

                        return date + ": " + count;
                    })
                    .collect(Collectors.joining("\n"));

            return Optional.of("🔔 Znaleziono nowe terminy: \n" + summaryTerms);
        }
    }
}
