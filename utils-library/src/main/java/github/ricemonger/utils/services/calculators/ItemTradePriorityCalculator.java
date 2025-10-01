package github.ricemonger.utils.services.calculators;

import github.ricemonger.utils.DTOs.common.Item;
import lombok.RequiredArgsConstructor;

import static github.ricemonger.utils.services.calculators.ItemTradeTimeCalculator.MINUTES_IN_A_MONTH;
import static github.ricemonger.utils.services.calculators.ItemTradeTimeCalculator.TRADE_MANAGER_FIXED_RATE_MINUTES;

@RequiredArgsConstructor
public class ItemTradePriorityCalculator {

    private final PricesCommonValuesService commonValuesService;

    public Long calculatePotentialSellTradePriority(Item item, Integer price, Integer minutesToTrade) {
        if (price != null && price > 0) {

            if (item.getMinSellPrice() != null && item.getMinSellPrice() < price) {
                return Long.MIN_VALUE;
            }

            int monthMedianPrice = item.getMonthMedianPrice() == null ? 0 : item.getMonthMedianPrice();

            if (minutesToTrade == null) {
                return null;
            }

            long tradePriority = getPriceFactor(price, 0.5) *
                                 getPriceDifferenceFactor(price, monthMedianPrice, 1) *
                                 getPriceRatioFactorPercent(price, monthMedianPrice, 1) *
                                 getTimeFactor(minutesToTrade, 0.4);

            if (price < monthMedianPrice) {
                tradePriority = -tradePriority;
            }

            return tradePriority;

        } else {
            return null;
        }
    }


    public Long calculatePotentialBuyTradePriority(Item item, Integer price, Integer minutesToTrade) {
        long constant = commonValuesService.getMaximumMarketplacePrice();
        if (price != null && price > 0) {

            int monthMedianPrice = item.getMonthMedianPrice() == null ? 0 : item.getMonthMedianPrice();

            if (minutesToTrade == null) {
                return null;
            }

            long tradePriority = (constant / getPriceFactor(price, 0.8)) *
                                 getPriceDifferenceFactor(price, monthMedianPrice, 1) *
                                 getPriceRatioFactorPercent(price, monthMedianPrice, 1) *
                                 getTimeToResellFactor(item, 0.7) *
                                 getTimeFactor(minutesToTrade, 0.8);

            if (item.getMonthMedianPrice() != null && price > monthMedianPrice) {
                tradePriority = -tradePriority;
            }

            return tradePriority;
        } else {
            return null;
        }
    }

    private long getPriceFactor(int price, double pow) {
        return (long) Math.pow(price, pow);
    }

    private long getPriceDifferenceFactor(int price, int medianPrice, double pow) {
        if (medianPrice == 0) {
            return 1;
        }

        long result = (long) Math.pow(price - medianPrice, pow);

        return Math.max(Math.abs(result), 1);
    }

    private long getPriceRatioFactorPercent(int price, int medianPrice, double pow) {
        if (medianPrice == 0) {
            return 1;
        }

        int differenceInPercent = ((price - medianPrice) * 100) / medianPrice;

        long result = (long) Math.pow(differenceInPercent, pow);

        return Math.max(Math.abs(result), 1);
    }

    private long getTimeToResellFactor(Item item, double pow) {
        int sales = item.getMonthSales() == null ? 0 : Math.max(item.getMonthSales(), 0);

        return (long) Math.pow(sales, pow);
    }

    private long getTimeFactor(int minutesToTrade, double pow) {
        minutesToTrade = Math.max(minutesToTrade, TRADE_MANAGER_FIXED_RATE_MINUTES);

        return MINUTES_IN_A_MONTH / (long) (Math.pow(minutesToTrade, pow));
    }
}
