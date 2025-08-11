package github.ricemonger.item_trade_stats_calculator.scheduled_tasks;

import github.ricemonger.item_trade_stats_calculator.services.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledAllItemsStatsRecalculator {

    private final ItemService itemService;

    @Scheduled(fixedRateString = "${app.scheduling.items.fixedRate}", initialDelayString = "${app.scheduling.items.initialDelay}")
    public void recalculateAndSaveAllItemsHistoryFields() {
        itemService.recalculateAndSaveAllItemsHistoryFields();
    }
}

