package github.ricemonger.trades_manager.services.factories;

import github.ricemonger.trades_manager.services.CommonValuesService;
import github.ricemonger.trades_manager.services.PotentialTradeStatsService;
import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.DTOs.common.PotentialTradePriceAndTimeStats;
import github.ricemonger.utils.DTOs.common.PotentialTradeStats;
import github.ricemonger.utils.enums.ItemRarity;
import github.ricemonger.utils.services.calculators.ItemTradeTimeCalculator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static github.ricemonger.utils.services.calculators.ItemTradeTimeCalculator.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SpringBootTest
class PotentialTradeStatsServiceTest {
    @SpyBean
    private PotentialTradeStatsService potentialTradeStatsService;
    @MockBean
    private CommonValuesService commonValuesService;
    @MockBean
    private ItemTradeTimeCalculator itemTradeTimeCalculator;

    @Test
    public void getPotentialBuyTradesStatsOfItem_should_return_expected_result() {
        Item item = new Item();
        item.setRarity(ItemRarity.UNCOMMON);

        assertEquals(0, potentialTradeStatsService.getPotentialBuyTradesStatsOfItem(item).size());

        when(commonValuesService.getMinimumPriceByRarity(ItemRarity.UNCOMMON)).thenReturn(10);

        item.setPriorityToBuyByMinSellPrice(1L);
        List<PotentialTradeStats> result = potentialTradeStatsService.getPotentialBuyTradesStatsOfItem(item);
        assertEquals(1, result.size());
        assertEquals(new PotentialTradeStats(10, TRADE_MANAGER_FIXED_RATE_MINUTES, item.getPriorityToBuyByMinSellPrice()), result.get(0));

        item.setMinSellPrice(0);
        result = potentialTradeStatsService.getPotentialBuyTradesStatsOfItem(item);
        assertEquals(1, result.size());
        assertEquals(new PotentialTradeStats(10, TRADE_MANAGER_FIXED_RATE_MINUTES, item.getPriorityToBuyByMinSellPrice()), result.get(0));

        item.setMinSellPrice(1);
        result = potentialTradeStatsService.getPotentialBuyTradesStatsOfItem(item);
        assertEquals(1, result.size());
        assertEquals(new PotentialTradeStats(1, TRADE_MANAGER_FIXED_RATE_MINUTES, item.getPriorityToBuyByMinSellPrice()), result.get(0));

        item.setPriceToBuyIn1Hour(2);
        item.setPriorityToBuyIn1Hour(20L);
        result = potentialTradeStatsService.getPotentialBuyTradesStatsOfItem(item);
        assertEquals(2, result.size());
        assertEquals(new PotentialTradeStats(item.getPriceToBuyIn1Hour(), MINUTES_IN_AN_HOUR, item.getPriorityToBuyIn1Hour()), result.get(1));

        item.setPriceToBuyIn6Hours(3);
        item.setPriorityToBuyIn6Hours(30L);
        result = potentialTradeStatsService.getPotentialBuyTradesStatsOfItem(item);
        assertEquals(3, result.size());
        assertEquals(new PotentialTradeStats(item.getPriceToBuyIn6Hours(), MINUTES_IN_6_HOURS, item.getPriorityToBuyIn6Hours()), result.get(2));

        item.setPriceToBuyIn24Hours(4);
        item.setPriorityToBuyIn24Hours(40L);
        result = potentialTradeStatsService.getPotentialBuyTradesStatsOfItem(item);
        assertEquals(4, result.size());
        assertEquals(new PotentialTradeStats(item.getPriceToBuyIn24Hours(), MINUTES_IN_A_DAY, item.getPriorityToBuyIn24Hours()), result.get(3));

        item.setPriceToBuyIn168Hours(5);
        item.setPriorityToBuyIn168Hours(50L);
        result = potentialTradeStatsService.getPotentialBuyTradesStatsOfItem(item);
        assertEquals(5, result.size());
        assertEquals(new PotentialTradeStats(item.getPriceToBuyIn168Hours(), MINUTES_IN_A_WEEK, item.getPriorityToBuyIn168Hours()), result.get(4));

        item.setPriceToBuyIn720Hours(6);
        item.setPriorityToBuyIn720Hours(60L);
        result = potentialTradeStatsService.getPotentialBuyTradesStatsOfItem(item);
        assertEquals(6, result.size());
        assertEquals(new PotentialTradeStats(item.getPriceToBuyIn720Hours(), MINUTES_IN_A_MONTH, item.getPriorityToBuyIn720Hours()), result.get(5));
    }

    @Test
    public void getPotentialSellTradesStatsOfItem_should_return_expected_result() {
        Item item = new Item();

        assertEquals(0, potentialTradeStatsService.getPotentialSellTradesStatsOfItem(item).size());

        item.setPriorityToSellByNextFancySellPrice(3L);
        PotentialTradeStats fancyStats = new PotentialTradeStats(1, 2, item.getPriorityToSellByNextFancySellPrice());
        doReturn(new PotentialTradePriceAndTimeStats(fancyStats.getPrice(), fancyStats.getPrognosedTradeSuccessMinutes())).when(itemTradeTimeCalculator).calculatePriceAndTimeForNextFancySellPriceSale(same(item));

        List<PotentialTradeStats> result = potentialTradeStatsService.getPotentialSellTradesStatsOfItem(item);
        assertEquals(1, result.size());
        assertEquals(fancyStats, result.get(0));

        item.setPriorityToSellByNextFancySellPrice(3L);
        item.setPriorityToSellByMaxBuyPrice(4L);
        item.setMaxBuyPrice(5);
        PotentialTradeStats maxBuyStats = new PotentialTradeStats(item.getMaxBuyPrice(), TRADE_MANAGER_FIXED_RATE_MINUTES, item.getPriorityToSellByMaxBuyPrice());

        result = potentialTradeStatsService.getPotentialSellTradesStatsOfItem(item);
        assertEquals(2, result.size());
        assertEquals(fancyStats, result.get(0));
        assertEquals(maxBuyStats, result.get(1));

        item.setMaxBuyPrice(null);
        result = potentialTradeStatsService.getPotentialSellTradesStatsOfItem(item);
        assertEquals(1, result.size());
        assertEquals(fancyStats, result.get(0));


        item.setPriorityToSellByMaxBuyPrice(null);
        item.setMaxBuyPrice(5);

        result = potentialTradeStatsService.getPotentialSellTradesStatsOfItem(item);
        assertEquals(1, result.size());
        assertEquals(fancyStats, result.get(0));
    }
}