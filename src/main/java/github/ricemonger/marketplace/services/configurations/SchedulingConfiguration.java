package github.ricemonger.marketplace.services.configurations;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@ConditionalOnProperty(
        value = "app.scheduling.enabled", havingValue = "true", matchIfMissing = true)
@Configuration
@EnableScheduling
public class SchedulingConfiguration {
}
