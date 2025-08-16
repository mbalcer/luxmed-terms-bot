package pl.mbalcer.luxmedreservation.notification;

import org.springframework.stereotype.Component;
import pl.mbalcer.luxmedreservation.client.model.TermsInfoForDay;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MessageBuilder {
    private List<OffsetDateTime> times;

    public MessageBuilder() {
        times = new ArrayList<>();
    }

    public String buildMessage(List<TermsInfoForDay> termsInfoForDay) {
        if (termsInfoForDay == null || termsInfoForDay.isEmpty()) {
            times.add(OffsetDateTime.now());
            if (times.size() == 10) {
                String summary = times.stream()
                        .map(time -> time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .collect(Collectors.joining("\n"));
                times.clear();
                return "🙁 Szukamy co jakiś czas terminów, ale nadal nic nie ma. Ostatnie wyszukiwania: \n" + summary;
            } else {
                return "";
            }
        } else {
            String summaryTerms = termsInfoForDay.stream()
                    .map(termsInfo -> {
                        String date = OffsetDateTime.parse(termsInfo.day()).toLocalDate().toString();
                        int count = termsInfo.termsCounter().termsNumber();

                        return date + ": " + count;
                    })
                    .collect(Collectors.joining("\n"));

            return "🔔 Znaleziono nowe terminy: \n" + summaryTerms;
        }
    }

    public List<OffsetDateTime> getTimes() {
        return times;
    }
}
