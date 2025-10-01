package github.ricemonger.item_trade_stats_calculator.scheduled_tasks;

import github.ricemonger.item_trade_stats_calculator.services.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledAllItemsPotentialTradeStatsByCurrentPricesRecalculator {

    private final ItemService itemService;

    @Scheduled(fixedRateString = "${app.scheduling.current_prices.fixedRate}", initialDelayString = "${app.scheduling.current_prices.initialDelay}")
    public void recalculateAndSaveAllPotentialTradeStatsByCurrentPrices() {
        itemService.recalculateAndSaveAllItemsPotentialTradeStatsByCurrentPrices();
    }
}
