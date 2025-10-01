package github.ricemonger;

import github.ricemonger.marketplace.databases.redis.services.RedisUtilsConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ComponentScan(basePackages = {"github.ricemonger.marketplace", "github.ricemonger.telegramBot", "github.ricemonger.utilspostgresschema"},
        basePackageClasses = {github.ricemonger.utils.PublicMethodLogger.class,
                TelegramBotUpdatesConsumerApplication.class})
@EntityScan(basePackages = "github.ricemonger.utilspostgresschema")
@Import({RedisUtilsConfiguration.class})
public class TelegramBotUpdatesConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelegramBotUpdatesConsumerApplication.class, args);
    }

}
