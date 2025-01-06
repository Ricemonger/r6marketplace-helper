package github.ricemonger.users_ubi_accs_reauthorizer.services;

import github.ricemonger.utilslibrarykafka.NotificationKafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationKafkaProducer notificationKafkaProducer;

    public void sendPrivateNotification(Long userId, String string) {
        try {
            notificationKafkaProducer.producePrivateNotification(userId, string);
        } catch (Exception e) {
            log.warn("Failed to send private notification to user with id: " + userId + " with message: " + string + " due to: " + e.getMessage());
        }
    }
}
