package github.ricemonger.trades_manager.services;

import github.ricemonger.utilslibrarykafka.NotificationKafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationKafkaProducer notificationKafkaProducer;

    public void sendTradeManagerNotification(Long userId, String string) {
        notificationKafkaProducer.produceTradeManagerNotification(userId, string);
    }
}
