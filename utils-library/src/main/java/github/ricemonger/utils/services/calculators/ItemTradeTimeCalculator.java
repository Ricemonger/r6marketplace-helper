package github.ricemonger.utils.services.calculators;

import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.DTOs.common.PotentialTradePriceAndTimeStats;
import github.ricemonger.utils.DTOs.personal.UbiTradeI;
import github.ricemonger.utils.enums.TradeCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public class ItemTradeTimeCalculator {
    public static final int MINUTES_IN_A_MONTH = 43200;
    public static final int MINUTES_IN_A_WEEK = 10080;
    public static final int MINUTES_IN_A_DAY = 1440;
    public static final int MINUTES_IN_6_HOURS = 360;
    public static final int MINUTES_IN_AN_HOUR = 60;
    public static final int TRADE_MANAGER_FIXED_RATE_MINUTES = 1;

    private final PricesCommonValuesService commonValuesService;

    private final ItemFancyPriceCalculator itemFancyPriceCalculator;

    public Integer getExpectedPaymentsSuccessMinutesForExistingTradeOrNull(UbiTradeI ubiTrade) {
        int minutesTradeExists = (int) Duration.between(ubiTrade.getLastModifiedAt(), LocalDateTime.now().plusMinutes(10)).toMinutes();

        Integer prognosedTradeSuccessMinutes = getPrognosedTradeSuccessMinutesByPriceOrNull(ubiTrade.getItem(), ubiTrade.getProposedPaymentPrice(), ubiTrade.getCategory());

        if (prognosedTradeSuccessMinutes != null) {
            return Math.max(prognosedTradeSuccessMinutes - minutesTradeExists, TRADE_MANAGER_FIXED_RATE_MINUTES);
        } else {
            return null;
        }
    }

    public Integer getPrognosedTradeSuccessMinutesByPriceOrNull(Item item, Integer proposedPaymentPrice, TradeCategory category) {
        if (category == TradeCategory.Buy) {
            if (item.getMinSellPrice() != null && proposedPaymentPrice >= item.getMinSellPrice()) {
                return TRADE_MANAGER_FIXED_RATE_MINUTES;
            } else if (item.getPriceToBuyIn1Hour() != null && proposedPaymentPrice >= item.getPriceToBuyIn1Hour()) {
                return MINUTES_IN_AN_HOUR;
            } else if (item.getPriceToBuyIn6Hours() != null && proposedPaymentPrice >= item.getPriceToBuyIn6Hours()) {
                return MINUTES_IN_6_HOURS;
            } else if (item.getPriceToBuyIn24Hours() != null && proposedPaymentPrice >= item.getPriceToBuyIn24Hours()) {
                return MINUTES_IN_A_DAY;
            } else if (item.getPriceToBuyIn168Hours() != null && proposedPaymentPrice >= item.getPriceToBuyIn168Hours()) {
                return MINUTES_IN_A_WEEK;
            } else if (item.getPriceToBuyIn720Hours() != null && proposedPaymentPrice >= item.getPriceToBuyIn720Hours()) {
                return MINUTES_IN_A_MONTH;
            } else {
                return null;
            }
        } else if (category == TradeCategory.Sell) {
            if (item.getMaxBuyPrice() != null && proposedPaymentPrice <= item.getMaxBuyPrice()) {
                return TRADE_MANAGER_FIXED_RATE_MINUTES;
            } else {
                return calculatePrognosedTimeToSellItemByNextSellPrice(item);
            }
        } else {
            log.error("getPrognosedTradeSuccessMinutes:Trade category is Unknown or null for itemId: {}, proposedPrice: {} and tradeCategory: {} ",
                    item, proposedPaymentPrice, category);
            return null;
        }
    }

    public PotentialTradePriceAndTimeStats calculatePriceAndTimeForNextFancySellPriceSale(Item item) {
        int nextFancySellPrice = itemFancyPriceCalculator.getNextFancySellPrice(item);
        int timeToSellByNextFancySellPrice = calculatePrognosedTimeToSellItemByNextSellPrice(item);

        return new PotentialTradePriceAndTimeStats(nextFancySellPrice, timeToSellByNextFancySellPrice);
    }

    public Integer calculatePrognosedTimeToSellItemByNextSellPrice(Item item) {
        int monthSalesPerDay = item.getMonthSalesPerDay() == null || item.getMonthSalesPerDay() <= 0 ? 1 : item.getMonthSalesPerDay();

        if (item.getMinSellPrice() != null && item.getMinSellPrice() == commonValuesService.getMinimumPriceByRarity(item.getRarity())) {
            int sellOrdersCount = item.getSellOrdersCount() == null || item.getSellOrdersCount() <= 0 ? 1 : item.getSellOrdersCount();
            return sellOrdersCount * MINUTES_IN_A_DAY / monthSalesPerDay;
        } else {
            return MINUTES_IN_A_DAY / monthSalesPerDay;
        }
    }

    public int getSameOrHigherPricesBuyOrdersAmount(Item item, int price) {
        int currentBuyers = item.getBuyOrdersCount();
        // buyers prices proportions: 1% - maxBuyPrice
        // 4% - prevFancyPrice
        // 10% - maxBuyPrice * 0.66
        // 15% - maxBuyPrice * 0.5
        // 70% - limitMinPrice
        int maxBuyPrice = item.getMaxBuyPrice();

        float percentOfSameOrHigherPricesBuyOrdersAmount;

        if (price <= commonValuesService.getMinimumPriceByRarity(item.getRarity())) {
            percentOfSameOrHigherPricesBuyOrdersAmount = 1.0f;
        } else if (price <= maxBuyPrice * 0.5) {
            percentOfSameOrHigherPricesBuyOrdersAmount = 0.3f;
        } else if (price <= maxBuyPrice * 0.66) {
            percentOfSameOrHigherPricesBuyOrdersAmount = 0.15f;
        } else if (price <= itemFancyPriceCalculator.getPrevFancyBuyPriceByMaxBuyPrice(item)) {
            percentOfSameOrHigherPricesBuyOrdersAmount = 0.05f;
        } else if (price <= maxBuyPrice) {
            percentOfSameOrHigherPricesBuyOrdersAmount = 0.01f;
        } else {
            percentOfSameOrHigherPricesBuyOrdersAmount = 0.0f;
        }

        return Math.round(currentBuyers * percentOfSameOrHigherPricesBuyOrdersAmount);
    }
}
