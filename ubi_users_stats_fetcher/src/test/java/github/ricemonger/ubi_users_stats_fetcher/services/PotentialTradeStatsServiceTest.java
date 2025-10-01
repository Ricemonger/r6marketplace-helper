package github.ricemonger.ubi_users_stats_fetcher.services;

import github.ricemonger.utils.DTOs.common.PotentialTradeStats;
import github.ricemonger.utils.DTOs.personal.UbiTrade;
import github.ricemonger.utils.services.calculators.ItemTradeStatsCalculator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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
    public void calculatePotentialBuyTradeStatsForExistingTrade_should_return_calculator_result() {
        UbiTrade ubiTrade = mock(UbiTrade.class);

        PotentialTradeStats stats = mock(PotentialTradeStats.class);

        when(itemTradeStatsCalculator.calculatePotentialBuyTradeStatsForExistingTrade(same(ubiTrade))).thenReturn(stats);

        PotentialTradeStats result = potentialTradeStatsService.calculatePotentialBuyTradeStatsForExistingTrade(ubiTrade);

        assertSame(stats, result);
    }

    @Test
    public void calculatePotentialSellTradeStatsForExistingTrade_should_return_calculator_result() {
        UbiTrade ubiTrade = mock(UbiTrade.class);

        PotentialTradeStats stats = mock(PotentialTradeStats.class);

        when(itemTradeStatsCalculator.calculatePotentialSellTradeStatsForExistingTrade(same(ubiTrade))).thenReturn(stats);

        PotentialTradeStats result = potentialTradeStatsService.calculatePotentialSellTradeStatsForExistingTrade(ubiTrade);

        assertSame(stats, result);
    }
}