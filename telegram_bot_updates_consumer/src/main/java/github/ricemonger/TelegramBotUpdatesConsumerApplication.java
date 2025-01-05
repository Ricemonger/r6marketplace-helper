package github.ricemonger;

import github.ricemonger.marketplace.databases.redis.services.RedisUtilsConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({RedisUtilsConfiguration.class})
public class TelegramBotUpdatesConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelegramBotUpdatesConsumerApplication.class, args);
    }

}
