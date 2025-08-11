package github.ricemonger.utils.services.calculators;

import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.DTOs.common.ItemDaySalesStatsByItemId;
import lombok.RequiredArgsConstructor;

import java.util.*;

import static github.ricemonger.utils.services.calculators.ItemTradeTimeCalculator.MINUTES_IN_A_MONTH;

@RequiredArgsConstructor
public class ItemPotentialTradeStatsCalculator {
    private final ItemFancyPriceCalculator itemFancyPriceCalculator;

    private final ItemTradeTimeCalculator itemTradeTimeCalculator;

    public Integer calculatePotentialBuyTradePriceForTime(Item item, Collection<ItemDaySalesStatsByItemId> resultingPerDayStats, Integer minutesToBuy) {
        TreeMap<Integer, Integer> sortedMonthPricesAndQuantities = new TreeMap<>(Comparator.naturalOrder());
        for (ItemDaySalesStatsByItemId dayStat : resultingPerDayStats) {
            for (Map.Entry<Integer, Integer> priceAndQuantity : dayStat.getPriceAndQuantity().entrySet()) {
                sortedMonthPricesAndQuantities.put(priceAndQuantity.getKey(), sortedMonthPricesAndQuantities.getOrDefault(priceAndQuantity.getKey(), 0) + priceAndQuantity.getValue());
            }
        }

        int basicRequiredSalesAmount = MINUTES_IN_A_MONTH / minutesToBuy;

        Iterator<Map.Entry<Integer, Integer>> iterator = sortedMonthPricesAndQuantities.entrySet().iterator();

        int totalAmount = 0;

        while (iterator.hasNext()) {
            Map.Entry<Integer, Integer> entry = iterator.next();

            int price = entry.getKey();
            int quantity = entry.getValue();

            totalAmount += quantity;

            if (basicRequiredSalesAmount + itemTradeTimeCalculator.getSameOrHigherPricesBuyOrdersAmount(item, price) <= totalAmount) {
                return itemFancyPriceCalculator.getCurrentFancyBuyPrice(item, price);
            }
        }

        return null;
    }
}
