package pl.mbalcer.luxmedreservation.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.mbalcer.luxmedreservation.client.model.TermsInfoForDay;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class NotificationService {
    private final TelegramNotifier telegramNotifier;
    private final MessageBuilder messageBuilder;

    public NotificationService(TelegramNotifier telegramNotifier, MessageBuilder messageBuilder) {
        this.telegramNotifier = telegramNotifier;
        this.messageBuilder = messageBuilder;
    }

    public void notifyIfAvailableTermsFound(List<TermsInfoForDay> availableDays) {
        Optional<String> message = messageBuilder.buildMessage(availableDays);
        if (message.isPresent()) {
            telegramNotifier.sendMessage(message.get());
            log.debug("Message to Telegram: " + message.get());
        }
    }
}