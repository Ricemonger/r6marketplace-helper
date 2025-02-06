package github.ricemonger.trades_manager.services;

import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.DTOs.common.PotentialTradeStats;
import github.ricemonger.utils.DTOs.common.PrioritizedPotentialTradeStats;
import github.ricemonger.utils.services.calculators.ItemTradeTimeCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static github.ricemonger.utils.services.calculators.ItemTradeTimeCalculator.*;

@Service
@RequiredArgsConstructor
public class PotentialTradeStatsService {

    private final CommonValuesService commonValuesService;

    private final ItemTradeTimeCalculator itemTradeTimeCalculator;

    public List<PrioritizedPotentialTradeStats> getPotentialBuyTradesStatsOfItem(Item item) {
        List<PrioritizedPotentialTradeStats> result = new ArrayList<>();

        if (item.getPriorityToBuyByMinSellPrice() != null) {
            int price;
            if (item.getMinSellPrice() == null || item.getMinSellPrice() <= 0) {
                price = commonValuesService.getMinimumPriceByRarity(item.getRarity());
            } else {
                price = item.getMinSellPrice();
            }
            result.add(new PrioritizedPotentialTradeStats(price, TRADE_MANAGER_FIXED_RATE_MINUTES, item.getPriorityToBuyByMinSellPrice()));
        }
        if (item.getPriceToBuyIn1Hour() != null && item.getPriorityToBuyIn1Hour() != null) {
            result.add(new PrioritizedPotentialTradeStats(item.getPriceToBuyIn1Hour(), MINUTES_IN_AN_HOUR, item.getPriorityToBuyIn1Hour()));
        }
        if (item.getPriceToBuyIn6Hours() != null && item.getPriorityToBuyIn6Hours() != null) {
            result.add(new PrioritizedPotentialTradeStats(item.getPriceToBuyIn6Hours(), MINUTES_IN_6_HOURS, item.getPriorityToBuyIn6Hours()));
        }
        if (item.getPriceToBuyIn24Hours() != null && item.getPriorityToBuyIn24Hours() != null) {
            result.add(new PrioritizedPotentialTradeStats(item.getPriceToBuyIn24Hours(), MINUTES_IN_A_DAY, item.getPriorityToBuyIn24Hours()));
        }
        if (item.getPriceToBuyIn168Hours() != null && item.getPriorityToBuyIn168Hours() != null) {
            result.add(new PrioritizedPotentialTradeStats(item.getPriceToBuyIn168Hours(), MINUTES_IN_A_WEEK, item.getPriorityToBuyIn168Hours()));
        }
        if (item.getPriceToBuyIn720Hours() != null && item.getPriorityToBuyIn720Hours() != null) {
            result.add(new PrioritizedPotentialTradeStats(item.getPriceToBuyIn720Hours(), MINUTES_IN_A_MONTH, item.getPriorityToBuyIn720Hours()));
        }

        return result;
    }

    public List<PrioritizedPotentialTradeStats> getPotentialSellTradesStatsOfItem(Item item) {
        List<PrioritizedPotentialTradeStats> result = new ArrayList<>();

        if (item.getPriorityToSellByNextFancySellPrice() != null) {
            PotentialTradeStats priceAndTime = itemTradeTimeCalculator.calculatePriceAndTimeForNextFancySellPriceSale(item);
            result.add(new PrioritizedPotentialTradeStats(priceAndTime.price(), priceAndTime.time(), item.getPriorityToSellByNextFancySellPrice()));
        }

        if (item.getPriorityToSellByMaxBuyPrice() != null && item.getMaxBuyPrice() != null) {
            result.add(new PrioritizedPotentialTradeStats(item.getMaxBuyPrice(), TRADE_MANAGER_FIXED_RATE_MINUTES, item.getPriorityToSellByMaxBuyPrice()));
        }

        return result;
    }
}
