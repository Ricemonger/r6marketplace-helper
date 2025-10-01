package github.ricemonger.item_trade_stats_calculator.services;

import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.DTOs.common.ItemDaySalesStatsByItemId;
import github.ricemonger.utils.DTOs.common.PotentialTradeStats;
import github.ricemonger.utils.services.calculators.ItemTradeStatsCalculator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class PotentialTradeStatsServiceTest {
    @Autowired
    private PotentialTradeStatsService potentialTradeStatsService;
    @MockBean
    private ItemTradeStatsCalculator itemTradeStatsCalculator;

    @Test
    public void calculatePotentialBuyTradeStatsForTime_should_return_calculator_result() {
        Item item = mock(Item.class);
        List<ItemDaySalesStatsByItemId> resultingPerDayStats = mock(List.class);
        Integer minutesToTrade = 0;

        PotentialTradeStats stats = mock(PotentialTradeStats.class);

        when(itemTradeStatsCalculator.calculatePotentialBuyTradeStatsForTime(same(item), same(resultingPerDayStats), same(minutesToTrade))).thenReturn(stats);

        PotentialTradeStats result = potentialTradeStatsService.calculatePotentialBuyTradeStatsForTime(item, resultingPerDayStats, minutesToTrade);

        assertSame(stats, result);
    }

    @Test
    public void calculatePotentialBuyTradeStatsByMinSellPrice_should_return_calculator_result() {
        Item item = mock(Item.class);

        PotentialTradeStats stats = mock(PotentialTradeStats.class);

        when(itemTradeStatsCalculator.calculatePotentialBuyTradeStatsByMinSellPrice(same(item))).thenReturn(stats);

        PotentialTradeStats result = potentialTradeStatsService.calculatePotentialBuyTradeStatsByMinSellPrice(item);

        assertSame(stats, result);
    }

    @Test
    public void calculatePotentialSellTradeStatsByMaxBuyPrice_should_return_calculator_result() {
        Item item = mock(Item.class);

        PotentialTradeStats stats = mock(PotentialTradeStats.class);

        when(itemTradeStatsCalculator.calculatePotentialSellTradeStatsByMaxBuyPrice(same(item))).thenReturn(stats);

        PotentialTradeStats result = potentialTradeStatsService.calculatePotentialSellTradeStatsByMaxBuyPrice(item);

        assertSame(stats, result);
    }

    @Test
    public void calculatePotentialSellTradeStatsByNextFancySellPrice_should_return_calculator_result() {
        Item item = mock(Item.class);

        PotentialTradeStats stats = mock(PotentialTradeStats.class);

        when(itemTradeStatsCalculator.calculatePotentialSellTradeStatsByNextFancySellPrice(same(item))).thenReturn(stats);

        PotentialTradeStats result = potentialTradeStatsService.calculatePotentialSellTradeStatsByNextFancySellPrice(item);

        assertSame(stats, result);
    }
}