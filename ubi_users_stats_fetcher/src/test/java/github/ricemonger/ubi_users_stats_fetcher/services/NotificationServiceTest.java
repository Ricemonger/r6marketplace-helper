package github.ricemonger.ubi_users_stats_fetcher.services;

import github.ricemonger.utilslibrarykafka.NotificationKafkaProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@SpringBootTest
class NotificationServiceTest {
    @Autowired
    private NotificationService notificationService;
    @MockBean
    private NotificationKafkaProducer notificationKafkaProducer;

    @Test
    public void sendPrivateNotification_should_handle_to_kafka() {
        notificationService.sendPrivateNotification(1L, "text");
        verify(notificationKafkaProducer).producePrivateNotification(1L, "text");
    }
}