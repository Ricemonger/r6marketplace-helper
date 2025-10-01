package github.ricemonger.utils.services.calculators;

import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.DTOs.common.PotentialTradePriceAndTimeStats;
import github.ricemonger.utils.DTOs.personal.UbiTrade;
import github.ricemonger.utils.enums.ItemRarity;
import github.ricemonger.utils.enums.TradeCategory;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static github.ricemonger.utils.services.calculators.ItemTradeTimeCalculator.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ItemTradeTimeCalculatorTest {

    private final PricesCommonValuesService commonValuesService = mock(PricesCommonValuesService.class);

    private final ItemFancyPriceCalculator itemFancyPriceCalculator = mock(ItemFancyPriceCalculator.class);

    private final ItemTradeTimeCalculator itemTradeTimeCalculator = spy(new ItemTradeTimeCalculator(commonValuesService, itemFancyPriceCalculator));

    @Test
    public void getExpectedPaymentsSuccessMinutesForExistingTradeOrNull_should_return_expected_result() {
        UbiTrade ubiTrade = new UbiTrade();
        LocalDateTime now = LocalDateTime.now();
        ubiTrade.setLastModifiedAt(now.minusDays(10));

        Integer prognosedTradeSuccessMinutes = 27000;
        doReturn(prognosedTradeSuccessMinutes).when(itemTradeTimeCalculator).getPrognosedTradeSuccessMinutesByPriceOrNull(any(), any(), any());

        int expectedTradeTime = 12600; //27000 - 1440 x 10

        int result = Math.abs(itemTradeTimeCalculator.getExpectedPaymentsSuccessMinutesForExistingTradeOrNull(ubiTrade) - expectedTradeTime);

        assertTrue(result > 5 && result < 15);

        doReturn(10).when(itemTradeTimeCalculator).getPrognosedTradeSuccessMinutesByPriceOrNull(any(), any(), any());
        assertEquals(TRADE_MANAGER_FIXED_RATE_MINUTES, itemTradeTimeCalculator.getExpectedPaymentsSuccessMinutesForExistingTradeOrNull(ubiTrade));

        doReturn(null).when(itemTradeTimeCalculator).getPrognosedTradeSuccessMinutesByPriceOrNull(any(), any(), any());
        assertNull(itemTradeTimeCalculator.getExpectedPaymentsSuccessMinutesForExistingTradeOrNull(ubiTrade));
    }

    @Test
    public void getPrognosedTradeSuccessMinutesByPriceOrNull_should_return_expected_result() {
        Item item = new Item();
        item.setMinSellPrice(1000);
        item.setMaxBuyPrice(1000);
        item.setMonthSalesPerDay(2);
        item.setPriceToBuyIn1Hour(950);
        item.setPriceToBuyIn6Hours(900);
        item.setPriceToBuyIn24Hours(850);
        item.setPriceToBuyIn168Hours(800);
        item.setPriceToBuyIn720Hours(750);

        assertEquals(TRADE_MANAGER_FIXED_RATE_MINUTES, itemTradeTimeCalculator.getPrognosedTradeSuccessMinutesByPriceOrNull(item, 1000, TradeCategory.Buy));
        assertEquals(TRADE_MANAGER_FIXED_RATE_MINUTES, itemTradeTimeCalculator.getPrognosedTradeSuccessMinutesByPriceOrNull(item, 1001, TradeCategory.Buy));

        assertEquals(MINUTES_IN_AN_HOUR, itemTradeTimeCalculator.getPrognosedTradeSuccessMinutesByPriceOrNull(item, 950, TradeCategory.Buy));
        assertEquals(MINUTES_IN_AN_HOUR, itemTradeTimeCalculator.getPrognosedTradeSuccessMinutesByPriceOrNull(item, 951, TradeCategory.Buy));

        assertEquals(MINUTES_IN_6_HOURS, itemTradeTimeCalculator.getPrognosedTradeSuccessMinutesByPriceOrNull(item, 900, TradeCategory.Buy));
        assertEquals(MINUTES_IN_6_HOURS, itemTradeTimeCalculator.getPrognosedTradeSuccessMinutesByPriceOrNull(item, 901, TradeCategory.Buy));

        assertEquals(MINUTES_IN_A_DAY, itemTradeTimeCalculator.getPrognosedTradeSuccessMinutesByPriceOrNull(item, 850, TradeCategory.Buy));
        assertEquals(MINUTES_IN_A_DAY, itemTradeTimeCalculator.getPrognosedTradeSuccessMinutesByPriceOrNull(item, 851, TradeCategory.Buy));

        assertEquals(MINUTES_IN_A_WEEK, itemTradeTimeCalculator.getPrognosedTradeSuccessMinutesByPriceOrNull(item, 800, TradeCategory.Buy));
        assertEquals(MINUTES_IN_A_WEEK, itemTradeTimeCalculator.getPrognosedTradeSuccessMinutesByPriceOrNull(item, 801, TradeCategory.Buy));

        assertEquals(MINUTES_IN_A_MONTH, itemTradeTimeCalculator.getPrognosedTradeSuccessMinutesByPriceOrNull(item, 750, TradeCategory.Buy));
        assertEquals(MINUTES_IN_A_MONTH, itemTradeTimeCalculator.getPrognosedTradeSuccessMinutesByPriceOrNull(item, 751, TradeCategory.Buy));

        assertNull(itemTradeTimeCalculator.getPrognosedTradeSuccessMinutesByPriceOrNull(item, 749, TradeCategory.Buy));

        assertEquals(TRADE_MANAGER_FIXED_RATE_MINUTES, itemTradeTimeCalculator.getPrognosedTradeSuccessMinutesByPriceOrNull(item, 1000, TradeCategory.Sell));
        assertEquals(TRADE_MANAGER_FIXED_RATE_MINUTES, itemTradeTimeCalculator.getPrognosedTradeSuccessMinutesByPriceOrNull(item, 999, TradeCategory.Sell));

        assertEquals(MINUTES_IN_A_DAY / 2, itemTradeTimeCalculator.getPrognosedTradeSuccessMinutesByPriceOrNull(item, 1050, TradeCategory.Sell));

        item.setMonthSalesPerDay(0);
        assertEquals(MINUTES_IN_A_DAY, itemTradeTimeCalculator.getPrognosedTradeSuccessMinutesByPriceOrNull(item, 1050, TradeCategory.Sell));

        item.setMonthSalesPerDay(null);
        assertEquals(MINUTES_IN_A_DAY, itemTradeTimeCalculator.getPrognosedTradeSuccessMinutesByPriceOrNull(item, 1050, TradeCategory.Sell));

        assertEquals(null, itemTradeTimeCalculator.getPrognosedTradeSuccessMinutesByPriceOrNull(item, 1100, TradeCategory.Unknown));
    }

    @Test
    public void calculatePriceAndTimeForNextFancySellPriceSale_should_return_expected_result() {
        when(commonValuesService.getMinimumPriceByRarity(ItemRarity.UNCOMMON)).thenReturn(10);

        Item item = new Item();
        item.setRarity(ItemRarity.UNCOMMON);
        item.setMinSellPrice(1000);
        item.setMonthSalesPerDay(1440);

        when(itemFancyPriceCalculator.getNextFancySellPrice(item)).thenReturn(999);

        int price = 999;
        int minutesToTrade = 1;
        PotentialTradePriceAndTimeStats expected = new PotentialTradePriceAndTimeStats(price, minutesToTrade);
        assertEquals(expected, itemTradeTimeCalculator.calculatePriceAndTimeForNextFancySellPriceSale(item));

        item.setMonthSalesPerDay(1);
        price = 999;
        minutesToTrade = 1440;
        expected = new PotentialTradePriceAndTimeStats(price, minutesToTrade);
        assertEquals(expected, itemTradeTimeCalculator.calculatePriceAndTimeForNextFancySellPriceSale(item));

        when(itemFancyPriceCalculator.getNextFancySellPrice(item)).thenReturn(10);

        item.setMinSellPrice(10);
        item.setMonthSalesPerDay(100);
        item.setSellOrdersCount(10);
        price = 10;
        minutesToTrade = 144;
        expected = new PotentialTradePriceAndTimeStats(price, minutesToTrade);
        assertEquals(expected, itemTradeTimeCalculator.calculatePriceAndTimeForNextFancySellPriceSale(item));

        item.setMinSellPrice(10);
        item.setMonthSalesPerDay(null);
        item.setSellOrdersCount(null);
        price = 10;
        minutesToTrade = 1440;
        expected = new PotentialTradePriceAndTimeStats(price, minutesToTrade);
        assertEquals(expected, itemTradeTimeCalculator.calculatePriceAndTimeForNextFancySellPriceSale(item));

        item.setMinSellPrice(10);
        item.setMonthSalesPerDay(0);
        item.setSellOrdersCount(0);
        price = 10;
        minutesToTrade = 1440;
        expected = new PotentialTradePriceAndTimeStats(price, minutesToTrade);
        assertEquals(expected, itemTradeTimeCalculator.calculatePriceAndTimeForNextFancySellPriceSale(item));

        item.setMinSellPrice(10);
        item.setMonthSalesPerDay(-1);
        item.setSellOrdersCount(-1);
        price = 10;
        minutesToTrade = 1440;
        expected = new PotentialTradePriceAndTimeStats(price, minutesToTrade);
        assertEquals(expected, itemTradeTimeCalculator.calculatePriceAndTimeForNextFancySellPriceSale(item));
    }

    @Test
    public void calculatePrognosedTimeToSellItemByNextSellPrice_should_return_expected_result() {
        when(commonValuesService.getMinimumPriceByRarity(ItemRarity.UNCOMMON)).thenReturn(10);

        Item item = new Item();
        item.setRarity(ItemRarity.UNCOMMON);
        item.setMinSellPrice(1000);
        item.setMonthSalesPerDay(1440);

        int minutesToTrade = 1;
        assertEquals(minutesToTrade, itemTradeTimeCalculator.calculatePrognosedTimeToSellItemByNextSellPrice(item));

        item.setMonthSalesPerDay(1);
        minutesToTrade = 1440;
        assertEquals(minutesToTrade, itemTradeTimeCalculator.calculatePrognosedTimeToSellItemByNextSellPrice(item));

        item.setMinSellPrice(10);
        item.setMonthSalesPerDay(100);
        item.setSellOrdersCount(10);
        minutesToTrade = 144;
        assertEquals(minutesToTrade, itemTradeTimeCalculator.calculatePrognosedTimeToSellItemByNextSellPrice(item));

        item.setMinSellPrice(10);
        item.setMonthSalesPerDay(null);
        item.setSellOrdersCount(null);
        minutesToTrade = 1440;
        assertEquals(minutesToTrade, itemTradeTimeCalculator.calculatePrognosedTimeToSellItemByNextSellPrice(item));

        item.setMinSellPrice(10);
        item.setMonthSalesPerDay(0);
        item.setSellOrdersCount(0);
        minutesToTrade = 1440;
        assertEquals(minutesToTrade, itemTradeTimeCalculator.calculatePrognosedTimeToSellItemByNextSellPrice(item));

        item.setMinSellPrice(10);
        item.setMonthSalesPerDay(-1);
        item.setSellOrdersCount(-1);
        minutesToTrade = 1440;
        assertEquals(minutesToTrade, itemTradeTimeCalculator.calculatePrognosedTimeToSellItemByNextSellPrice(item));
    }

    @Test
    public void getSameOrHigherPricesBuyOrdersAmount_should_return_expected_result() {
        Item item = new Item();
        item.setBuyOrdersCount(1000);
        item.setMaxBuyPrice(10_000);

        when(commonValuesService.getMinimumPriceByRarity(any())).thenReturn(10);
        when(itemFancyPriceCalculator.getPrevFancyBuyPriceByMaxBuyPrice(item)).thenReturn(9000);

        assertEquals(1000, itemTradeTimeCalculator.getSameOrHigherPricesBuyOrdersAmount(item, 9));
        assertEquals(1000, itemTradeTimeCalculator.getSameOrHigherPricesBuyOrdersAmount(item, 10));

        assertEquals(300, itemTradeTimeCalculator.getSameOrHigherPricesBuyOrdersAmount(item, 4999));
        assertEquals(300, itemTradeTimeCalculator.getSameOrHigherPricesBuyOrdersAmount(item, 5000));

        assertEquals(150, itemTradeTimeCalculator.getSameOrHigherPricesBuyOrdersAmount(item, 6599));
        assertEquals(150, itemTradeTimeCalculator.getSameOrHigherPricesBuyOrdersAmount(item, 6600));

        assertEquals(50, itemTradeTimeCalculator.getSameOrHigherPricesBuyOrdersAmount(item, 8999));
        assertEquals(50, itemTradeTimeCalculator.getSameOrHigherPricesBuyOrdersAmount(item, 9000));

        assertEquals(10, itemTradeTimeCalculator.getSameOrHigherPricesBuyOrdersAmount(item, 9999));
        assertEquals(10, itemTradeTimeCalculator.getSameOrHigherPricesBuyOrdersAmount(item, 10_000));

        assertEquals(0, itemTradeTimeCalculator.getSameOrHigherPricesBuyOrdersAmount(item, 10_001));
    }
}