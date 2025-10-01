package github.ricemonger.utils.services.calculators;

import github.ricemonger.utils.DTOs.common.Item;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemTradePriorityCalculatorTest {

    private final PricesCommonValuesService commonValuesService = mock(PricesCommonValuesService.class);

    private final ItemTradePriorityCalculator itemTradePriorityCalculator = new ItemTradePriorityCalculator(commonValuesService);

    @Test
    public void calculatePotentialSellTradePriority_should_return_min_value_if_price_is_higher_than_minSellPrice() {
        when(commonValuesService.getMaximumMarketplacePrice()).thenReturn(100_000);

        Item item = new Item();
        item.setMinSellPrice(1500);
        item.setMonthMedianPrice(1000);

        int price = 1501;
        int minutesToTrade = 432;

        long expectedPriority = Long.MIN_VALUE;

        assertEquals(expectedPriority, itemTradePriorityCalculator.calculatePotentialSellTradePriority(item, price, minutesToTrade));
    }

    @Test
    public void calculatePotentialSellTradePriority_should_return_expected_result_if_price_equals_min_sell_price() {
        when(commonValuesService.getMaximumMarketplacePrice()).thenReturn(100_000);

        Item item = new Item();
        item.setMinSellPrice(1500);
        item.setMonthMedianPrice(1000);

        int price = 1500;
        int minutesToTrade = 432;

        long expectedPriority = 3730650000L;

        assertEquals(expectedPriority, itemTradePriorityCalculator.calculatePotentialSellTradePriority(item, price, minutesToTrade));
    }

    @Test
    public void calculatePotentialSellTradePriority_should_return_expected_object_for_profitable_trade() {
        when(commonValuesService.getMaximumMarketplacePrice()).thenReturn(100_000);

        Item item = new Item();
        item.setMonthMedianPrice(1000);

        int price = 1500;
        int minutesToTrade = 432;

        long expectedPriority = 3730650000L;

        assertEquals(expectedPriority, itemTradePriorityCalculator.calculatePotentialSellTradePriority(item, price, minutesToTrade));
    }

    @Test
    public void calculatePotentialSellTradePriority_should_return_nulls_if_minutes_to_trade_is_null() {
        when(commonValuesService.getMaximumMarketplacePrice()).thenReturn(100_000);

        Item item = new Item();
        item.setMonthMedianPrice(1000);

        int price = 1500;

        assertNull(itemTradePriorityCalculator.calculatePotentialSellTradePriority(item, price, null));
    }

    @Test
    public void calculatePotentialSellTradePriority_should_return_expected_object_for_non_profitable_trade() {
        when(commonValuesService.getMaximumMarketplacePrice()).thenReturn(100_000);

        Item item = new Item();
        item.setMonthMedianPrice(1000);

        int price = 500;
        int minutesToTrade = 432;

        long expectedPriority = -2159850000L;

        assertEquals(expectedPriority, itemTradePriorityCalculator.calculatePotentialSellTradePriority(item, price, minutesToTrade));
    }

    @Test
    public void calculatePotentialSellTradePriority_should_return_expected_object_for_zero_monthMedianPrice() {
        when(commonValuesService.getMaximumMarketplacePrice()).thenReturn(100_000);

        Item item = new Item();
        item.setMonthMedianPrice(0);

        int price = 500;
        int minutesToTrade = 432;

        long expectedPriority = 86394L;

        assertEquals(expectedPriority, itemTradePriorityCalculator.calculatePotentialSellTradePriority(item, price, minutesToTrade));
    }

    @Test
    public void calculatePotentialSellTradePriority_should_return_null_tradePriority_for_null_or_negative_price() {
        when(commonValuesService.getMaximumMarketplacePrice()).thenReturn(15_000);

        assertNull(itemTradePriorityCalculator.calculatePotentialSellTradePriority(new Item(), null, 1));
        assertNull(itemTradePriorityCalculator.calculatePotentialSellTradePriority(new Item(), 0, 1));
    }

    @Test
    public void calculatePotentialBuyTradePriority_should_return_expected_object_for_profitable_trade() {
        when(commonValuesService.getMaximumMarketplacePrice()).thenReturn(100_000);

        Item item = new Item();
        item.setMonthMedianPrice(1000);
        item.setMonthSales(100);

        int price = 500;
        int minutesToTrade = 432;

        long expectedPriority = 146173750000L;

        assertEquals(expectedPriority, itemTradePriorityCalculator.calculatePotentialBuyTradePriority(item, price, minutesToTrade));
    }

    @Test
    public void calculatePotentialBuyTradePriority_should_return_expected_object_for_non_profitable_trade() {
        when(commonValuesService.getMaximumMarketplacePrice()).thenReturn(100_000);

        Item item = new Item();
        item.setMonthMedianPrice(1000);
        item.setMonthSales(100);

        int price = 1500;
        int minutesToTrade = 432;

        long expectedPriority = -60660000000L;

        assertEquals(expectedPriority, itemTradePriorityCalculator.calculatePotentialBuyTradePriority(item, price, minutesToTrade));
    }

    @Test
    public void calculatePotentialBuyTradePriority_should_return_expected_object_for_zero_month_sales() {
        when(commonValuesService.getMaximumMarketplacePrice()).thenReturn(100_000);

        Item item = new Item();
        item.setMonthMedianPrice(1000);
        item.setMonthSales(0);

        int price = 1500;
        int minutesToTrade = 432;

        long expectedPriority = 0;

        assertEquals(expectedPriority, itemTradePriorityCalculator.calculatePotentialBuyTradePriority(item, price, minutesToTrade));
    }

    @Test
    public void calculatePotentialBuyTradePriority_should_return_expected_object_for_zero_monthMedianPrice() {
        when(commonValuesService.getMaximumMarketplacePrice()).thenReturn(100_000);

        Item item = new Item();
        item.setMonthMedianPrice(0);
        item.setMonthSales(100);

        int price = 500;
        int minutesToTrade = 432;

        long expectedPriority = -5846950L;

        assertEquals(expectedPriority, itemTradePriorityCalculator.calculatePotentialBuyTradePriority(item, price, minutesToTrade));
    }

    @Test
    public void calculatePotentialBuyTradePriority_should_return_nulls_for_null_time() {
        when(commonValuesService.getMaximumMarketplacePrice()).thenReturn(100_000);

        Item item = new Item();
        item.setMonthMedianPrice(1000);
        item.setMonthSales(100);

        int price = 500;

        assertNull(itemTradePriorityCalculator.calculatePotentialBuyTradePriority(item, price, null));
    }

    @Test
    public void calculatePotentialBuyTradePriority_should_return_null_tradePriority_for_null_or_negative_price() {
        when(commonValuesService.getMaximumMarketplacePrice()).thenReturn(15_000);

        assertNull(itemTradePriorityCalculator.calculatePotentialBuyTradePriority(new Item(), null, 1));
        assertNull(itemTradePriorityCalculator.calculatePotentialBuyTradePriority(new Item(), 0, 1));
    }
}