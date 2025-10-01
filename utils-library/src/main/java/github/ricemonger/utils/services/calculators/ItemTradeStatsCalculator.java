package github.ricemonger.utils.services.calculators;

import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.DTOs.common.ItemDaySalesStatsByItemId;
import github.ricemonger.utils.DTOs.common.PotentialTradePriceAndTimeStats;
import github.ricemonger.utils.DTOs.common.PotentialTradeStats;
import github.ricemonger.utils.DTOs.personal.UbiTradeI;
import lombok.RequiredArgsConstructor;

import java.util.*;

import static github.ricemonger.utils.services.calculators.ItemTradeTimeCalculator.MINUTES_IN_A_MONTH;
import static github.ricemonger.utils.services.calculators.ItemTradeTimeCalculator.TRADE_MANAGER_FIXED_RATE_MINUTES;

@RequiredArgsConstructor
public class ItemTradeStatsCalculator {
    private final ItemFancyPriceCalculator itemFancyPriceCalculator;

    private final ItemTradeTimeCalculator itemTradeTimeCalculator;

    private final ItemTradePriorityCalculator itemTradePriorityCalculator;

    public PotentialTradeStats calculatePotentialSellTradeStatsByMaxBuyPrice(Item item) {
        return calculatePotentialSellTradeStats(item, item.getMaxBuyPrice(), TRADE_MANAGER_FIXED_RATE_MINUTES);
    }

    public PotentialTradeStats calculatePotentialSellTradeStatsByNextFancySellPrice(Item item) {
        PotentialTradePriceAndTimeStats priceAndTime = itemTradeTimeCalculator.calculatePriceAndTimeForNextFancySellPriceSale(item);
        return calculatePotentialSellTradeStats(item, priceAndTime.price(), priceAndTime.time());
    }

    public PotentialTradeStats calculatePotentialBuyTradeStatsByMinSellPrice(Item item) {
        return calculatePotentialBuyTradeStats(item, item.getMinSellPrice(), TRADE_MANAGER_FIXED_RATE_MINUTES);
    }

    public PotentialTradeStats calculatePotentialBuyTradeStatsForTime(Item item, Collection<ItemDaySalesStatsByItemId> resultingPerDayStats, Integer minutesToBuy) {
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
                return calculatePotentialBuyTradeStats(item, itemFancyPriceCalculator.getCurrentFancyBuyPrice(item, price), minutesToBuy);
            }
        }

        return calculatePotentialBuyTradeStats(item, null, minutesToBuy);
    }

    public PotentialTradeStats calculatePotentialSellTradeStatsForExistingTrade(UbiTradeI existingTrade) {
        return calculatePotentialSellTradeStats(existingTrade.getItem(), existingTrade.getProposedPaymentPrice(), itemTradeTimeCalculator.getExpectedPaymentsSuccessMinutesForExistingTradeOrNull(existingTrade));
    }

    public PotentialTradeStats calculatePotentialBuyTradeStatsForExistingTrade(UbiTradeI existingTrade) {
        return calculatePotentialBuyTradeStats(existingTrade.getItem(), existingTrade.getProposedPaymentPrice(), itemTradeTimeCalculator.getExpectedPaymentsSuccessMinutesForExistingTradeOrNull(existingTrade));
    }

    public PotentialTradeStats calculatePotentialSellTradeStats(Item item, Integer price, Integer minutesToTrade) {
        return new PotentialTradeStats(price, minutesToTrade, itemTradePriorityCalculator.calculatePotentialSellTradePriority(item, price, minutesToTrade));
    }

    public PotentialTradeStats calculatePotentialBuyTradeStats(Item item, Integer price, Integer minutesToTrade) {
        return new PotentialTradeStats(price, minutesToTrade, itemTradePriorityCalculator.calculatePotentialBuyTradePriority(item, price, minutesToTrade));
    }
}
