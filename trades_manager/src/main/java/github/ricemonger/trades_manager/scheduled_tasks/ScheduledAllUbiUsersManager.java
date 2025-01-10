package github.ricemonger.trades_manager.scheduled_tasks;

import github.ricemonger.trades_manager.services.CentralTradeManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScheduledAllUbiUsersManager {

    private final CentralTradeManager centralTradeManager;

    @Scheduled(fixedRateString = "${app.scheduling.fixedRate}", initialDelayString = "${app.scheduling.initialDelay}")
    public void manageAllUsersTrades() {
        centralTradeManager.manageAllUsersTrades();
    }
}
