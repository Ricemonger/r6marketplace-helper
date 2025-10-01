package github.ricemonger.utils.services.calculators;

import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.DTOs.common.ItemDaySalesStatsByItemId;
import github.ricemonger.utils.DTOs.common.PotentialTradePriceAndTimeStats;
import github.ricemonger.utils.DTOs.common.PotentialTradeStats;
import github.ricemonger.utils.DTOs.personal.UbiTrade;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static github.ricemonger.utils.services.calculators.ItemTradeTimeCalculator.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

class ItemTradeStatsCalculatorTest {

    private final ItemFancyPriceCalculator itemFancyPriceCalculator = mock(ItemFancyPriceCalculator.class);

    private final ItemTradeTimeCalculator itemTradeTimeCalculator = mock(ItemTradeTimeCalculator.class);

    private final ItemTradePriorityCalculator itemTradePriorityCalculator = mock(ItemTradePriorityCalculator.class);

    private final ItemTradeStatsCalculator itemTradeStatsCalculator = spy(new ItemTradeStatsCalculator(itemFancyPriceCalculator, itemTradeTimeCalculator, itemTradePriorityCalculator));

    @Test
    public void calculatePotentialSellTradeStatsByMaxBuyPrice_should_return_calculatePotentialSellTradeStats_result_with_max_buy_price_and_TRADE_MANAGER_FIXED_RATE_MINUTES() {
        Item item = new Item();
        item.setMaxBuyPrice(1000);

        doReturn(new PotentialTradeStats(1, 2, 3L)).when(itemTradeStatsCalculator).calculatePotentialSellTradeStats(same(item), same(item.getMaxBuyPrice()), eq(TRADE_MANAGER_FIXED_RATE_MINUTES));

        assertEquals(new PotentialTradeStats(1, 2, 3L), itemTradeStatsCalculator.calculatePotentialSellTradeStatsByMaxBuyPrice(item));
    }

    @Test
    public void calculatePotentialSellTradeStatsByNextFancySellPrice_should_return_calculatePotentialSellTradeStats_result_with_next_fancy_sell_price_and_expected_time() {
        Item item = new Item();

        PotentialTradePriceAndTimeStats priceAndTime = new PotentialTradePriceAndTimeStats(1000, 100);
        doReturn(priceAndTime).when(itemTradeTimeCalculator).calculatePriceAndTimeForNextFancySellPriceSale(same(item));

        doReturn(new PotentialTradeStats(1, 2, 3L)).when(itemTradeStatsCalculator).calculatePotentialSellTradeStats(same(item), eq(priceAndTime.price()), eq(priceAndTime.time()));

        assertEquals(new PotentialTradeStats(1, 2, 3L), itemTradeStatsCalculator.calculatePotentialSellTradeStatsByNextFancySellPrice(item));
    }

    @Test
    public void calculatePotentialBuyTradeStatsByMinSellPrice_should_return_calculatePotentialBuyTradeStats_result() {
        Item item = new Item();
        item.setMinSellPrice(1000);

        doReturn(new PotentialTradeStats(1, 2, 3L)).when(itemTradeStatsCalculator).calculatePotentialBuyTradeStats(same(item), same(item.getMinSellPrice()), eq(TRADE_MANAGER_FIXED_RATE_MINUTES));

        assertEquals(new PotentialTradeStats(1, 2, 3L), itemTradeStatsCalculator.calculatePotentialBuyTradeStatsByMinSellPrice(item));
    }

    @Test
    public void calculatePotentialBuyTradeStatsForTime_should_return_expected_result() {
        Map<Integer, Integer> priceAndQuantity1 = new HashMap<>();
        priceAndQuantity1.put(500, 250);
        priceAndQuantity1.put(1000, 250);
        priceAndQuantity1.put(1500, 250);
        ItemDaySalesStatsByItemId itemDaySalesStatsByItemId1 = new ItemDaySalesStatsByItemId();
        itemDaySalesStatsByItemId1.setPriceAndQuantity(priceAndQuantity1);

        Map<Integer, Integer> priceAndQuantity2 = new HashMap<>();
        priceAndQuantity2.put(500, 250); //500
        priceAndQuantity2.put(1000, 250); //500
        priceAndQuantity2.put(1500, 250); //500
        ItemDaySalesStatsByItemId itemDaySalesStatsByItemId2 = new ItemDaySalesStatsByItemId();
        itemDaySalesStatsByItemId2.setPriceAndQuantity(priceAndQuantity2);

        Item item = new Item();
        item.setBuyOrdersCount(5);
        item.setMaxBuyPrice(5);

        doReturn(280).when(itemTradeTimeCalculator).getSameOrHigherPricesBuyOrdersAmount(item, 1000);
        PotentialTradeStats expected = new PotentialTradeStats(1000, MINUTES_IN_AN_HOUR, 1000L);
        doReturn(expected).when(itemTradeStatsCalculator).calculatePotentialBuyTradeStats(any(), anyInt(), anyInt());
        assertEquals(expected, itemTradeStatsCalculator.calculatePotentialBuyTradeStatsForTime(item, List.of(itemDaySalesStatsByItemId1, itemDaySalesStatsByItemId2), MINUTES_IN_AN_HOUR));

        doReturn(281).when(itemTradeTimeCalculator).getSameOrHigherPricesBuyOrdersAmount(item, 1000);
        expected = new PotentialTradeStats(1500, MINUTES_IN_AN_HOUR, 1000L);
        doReturn(expected).when(itemTradeStatsCalculator).calculatePotentialBuyTradeStats(any(), anyInt(), anyInt());
        assertEquals(expected, itemTradeStatsCalculator.calculatePotentialBuyTradeStatsForTime(item, List.of(itemDaySalesStatsByItemId1, itemDaySalesStatsByItemId2), MINUTES_IN_AN_HOUR));

        doReturn(470).when(itemTradeTimeCalculator).getSameOrHigherPricesBuyOrdersAmount(item, 500);
        expected = new PotentialTradeStats(500, MINUTES_IN_A_DAY, 1000L);
        doReturn(expected).when(itemTradeStatsCalculator).calculatePotentialBuyTradeStats(any(), anyInt(), anyInt());
        assertEquals(expected, itemTradeStatsCalculator.calculatePotentialBuyTradeStatsForTime(item, List.of(itemDaySalesStatsByItemId1, itemDaySalesStatsByItemId2), MINUTES_IN_A_DAY));

        doReturn(471).when(itemTradeTimeCalculator).getSameOrHigherPricesBuyOrdersAmount(item, 500);
        expected = new PotentialTradeStats(1000, MINUTES_IN_A_DAY, 1000L);
        doReturn(expected).when(itemTradeStatsCalculator).calculatePotentialBuyTradeStats(any(), anyInt(), anyInt());
        assertEquals(expected, itemTradeStatsCalculator.calculatePotentialBuyTradeStatsForTime(item, List.of(itemDaySalesStatsByItemId1, itemDaySalesStatsByItemId2), MINUTES_IN_A_DAY));
    }

    @Test
    public void calculatePotentialSellTradeStatsForExistingTrade_should_return_expected_result() {
        UbiTrade existingTrade = new UbiTrade();
        existingTrade.setProposedPaymentPrice(1000);

        Item item = new Item();

        existingTrade.setItem(item);

        doReturn(10000).when(itemTradeTimeCalculator).getExpectedPaymentsSuccessMinutesForExistingTradeOrNull(existingTrade);
        doReturn(new PotentialTradeStats(2, 3, 4L)).when(itemTradeStatsCalculator).calculatePotentialSellTradeStats(same(item), eq(1000), eq(10000));

        assertEquals(new PotentialTradeStats(2, 3, 4L), itemTradeStatsCalculator.calculatePotentialSellTradeStatsForExistingTrade(existingTrade));
    }

    @Test
    public void calculatePotentialBuyTradeStatsForExistingTrade_should_return_expected_result() {
        UbiTrade existingTrade = new UbiTrade();
        existingTrade.setProposedPaymentPrice(1000);

        Item item = new Item();

        existingTrade.setItem(item);

        doReturn(10000).when(itemTradeTimeCalculator).getExpectedPaymentsSuccessMinutesForExistingTradeOrNull(existingTrade);
        doReturn(new PotentialTradeStats(2, 3, 4L)).when(itemTradeStatsCalculator).calculatePotentialBuyTradeStats(same(item), eq(1000), eq(10000));

        assertEquals(new PotentialTradeStats(2, 3, 4L), itemTradeStatsCalculator.calculatePotentialBuyTradeStatsForExistingTrade(existingTrade));
    }

    @Test
    public void calculatePotentialSellTradeStats_should_return_expected_object() {
        Item item = new Item();
        item.setMonthMedianPrice(1000);

        int price = 1500;
        int minutesToTrade = 432;

        long expectedPriority = 3730650000L;

        when(itemTradePriorityCalculator.calculatePotentialSellTradePriority(item, price, minutesToTrade)).thenReturn(expectedPriority);

        assertEquals(new PotentialTradeStats(price, minutesToTrade, expectedPriority), itemTradeStatsCalculator.calculatePotentialSellTradeStats(item, price, minutesToTrade));
    }

    @Test
    public void calculatePotentialBuyTradeStats_should_return_expected_object() {
        Item item = new Item();
        item.setMonthMedianPrice(1000);

        int price = 1500;
        int minutesToTrade = 432;

        long expectedPriority = 3730650000L;

        when(itemTradePriorityCalculator.calculatePotentialBuyTradePriority(item, price, minutesToTrade)).thenReturn(expectedPriority);

        assertEquals(new PotentialTradeStats(price, minutesToTrade, expectedPriority), itemTradeStatsCalculator.calculatePotentialBuyTradeStats(item, price, minutesToTrade));
    }
}