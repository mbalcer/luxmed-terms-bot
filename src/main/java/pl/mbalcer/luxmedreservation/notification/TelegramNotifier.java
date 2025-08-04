package pl.mbalcer.luxmedreservation.notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class TelegramNotifier {

    private final RestClient restClient;
    private final String chatId;

    public TelegramNotifier(
            @Value("${notification.telegram.token}") String botToken,
            @Value("${notification.telegram.chat-id}") String chatId
    ) {
        this.chatId = chatId;
        this.restClient = RestClient.builder()
                .baseUrl("https://api.telegram.org/bot" + botToken)
                .build();
    }

    public void sendMessage(String message) {
        var payload = new TelegramMessage(chatId, message);
        restClient.post()
                .uri("/sendMessage")
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve()
                .toBodilessEntity();
    }
}
