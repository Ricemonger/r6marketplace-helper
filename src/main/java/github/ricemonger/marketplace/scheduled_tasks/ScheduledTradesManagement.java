package github.ricemonger.marketplace.scheduled_tasks;

import github.ricemonger.marketplace.services.CentralTradeManager;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledTradesManagement {
    private final CentralTradeManager centralTradeManager;

    //@Scheduled(fixedRate = TRADE_MANAGER_FIXED_RATE_MINUTES * 60 * 1000, initialDelay = 5 * 60 * 1000) // every 1m after 5m of delay
    public void manageAllUsersTrades() {
        centralTradeManager.manageAllUsersTrades();
    }
}
