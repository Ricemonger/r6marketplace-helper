package github.ricemonger.marketplace.services.configurations;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class TelegramBotConfigurationTest {

    @Autowired
    private TelegramBotConfiguration telegramBotConfiguration;

    @Test
    public void telegramBotConfigurationPropertiesShouldBeAutowired() {
        assertNotNull(telegramBotConfiguration.getTELEGRAM_BOT_TOKEN());
        assertNotNull(telegramBotConfiguration.getTELEGRAM_BOT_USERNAME());
        assertNotNull(telegramBotConfiguration.getMessageHeight());
        assertNotNull(telegramBotConfiguration.getMessageLimit());
    }
}
