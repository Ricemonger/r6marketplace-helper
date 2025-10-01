package github.ricemonger.utils.services.calculators;

import org.springframework.context.annotation.Bean;

public class TradeStatsCalculatorUtilsConfiguration {

    @Bean
    public ItemFancyPriceCalculator itemFancyPriceCalculator(PricesCommonValuesService pricesCommonValuesService) {
        return new ItemFancyPriceCalculator(pricesCommonValuesService);
    }

    @Bean
    public ItemTradeTimeCalculator itemTradeTimeCalculator(PricesCommonValuesService pricesCommonValuesService, ItemFancyPriceCalculator itemFancyPriceCalculator) {
        return new ItemTradeTimeCalculator(pricesCommonValuesService, itemFancyPriceCalculator);
    }

    @Bean
    ItemTradePriorityCalculator itemTradePriorityCalculator(PricesCommonValuesService pricesCommonValuesService) {
        return new ItemTradePriorityCalculator(pricesCommonValuesService);
    }

    @Bean
    public ItemTradeStatsCalculator itemTradeStatsCalculator(ItemFancyPriceCalculator itemFancyPriceCalculator, ItemTradeTimeCalculator itemTradeTimeCalculator, ItemTradePriorityCalculator itemTradePriorityCalculator) {
        return new ItemTradeStatsCalculator(itemFancyPriceCalculator, itemTradeTimeCalculator, itemTradePriorityCalculator);
    }
}
