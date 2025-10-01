package github.ricemonger.ubi_users_stats_fetcher.services;

import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.DTOs.common.PotentialTradeStats;
import github.ricemonger.utils.DTOs.personal.Trade;
import github.ricemonger.utils.DTOs.personal.UbiTrade;
import github.ricemonger.utils.enums.TradeCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class TradeServiceTest {
    @Autowired
    private TradeService tradeService;
    @MockBean
    private PotentialTradeStatsService potentialTradeStatsService;
    @MockBean
    private ItemDatabaseService itemDatabaseService;

    @Test
    public void calculateTradeStatsForUbiTrades_should_return_trades_with_calculated_stats() {
        UbiTrade ubiTrade1 = new UbiTrade();
        ubiTrade1.setItem(new Item("itemId1"));
        ubiTrade1.setCategory(TradeCategory.Sell);

        UbiTrade ubiTrade2 = new UbiTrade();
        ubiTrade2.setItem(new Item("itemId2"));
        ubiTrade2.setCategory(TradeCategory.Buy);

        UbiTrade ubiTrade4 = new UbiTrade();
        ubiTrade4.setItem(new Item("nonExistingItemId"));
        ubiTrade4.setCategory(TradeCategory.Buy);

        List<UbiTrade> ubiTrades = List.of(ubiTrade1, ubiTrade2, ubiTrade4);

        Item existingItem1 = new Item("itemId1");
        existingItem1.setName("existingItemId1");
        Item existingItem2 = new Item("itemId2");
        existingItem2.setName("existingItemId2");
        List<Item> existingItems = List.of(existingItem1, existingItem2);

        when(itemDatabaseService.findItemsByIds(argThat(arg -> arg.containsAll(List.of("itemId1", "itemId2"))))).thenReturn(existingItems);

        PotentialTradeStats potentialTradeStats1 = new PotentialTradeStats();
        potentialTradeStats1.setPrognosedTradeSuccessMinutes(101);
        potentialTradeStats1.setTradePriority(102L);
        PotentialTradeStats potentialTradeStats2 = new PotentialTradeStats();
        potentialTradeStats2.setPrognosedTradeSuccessMinutes(201);
        potentialTradeStats2.setTradePriority(202L);
        PotentialTradeStats potentialTradeStats3 = new PotentialTradeStats();
        potentialTradeStats3.setPrognosedTradeSuccessMinutes(301);
        potentialTradeStats3.setTradePriority(302L);
        PotentialTradeStats potentialTradeStats4 = new PotentialTradeStats();
        potentialTradeStats4.setPrognosedTradeSuccessMinutes(401);

        when(potentialTradeStatsService.calculatePotentialSellTradeStatsForExistingTrade(argThat(t -> ("itemId1").equals(t.getItemId())))).thenReturn(potentialTradeStats1);
        when(potentialTradeStatsService.calculatePotentialBuyTradeStatsForExistingTrade(argThat(t -> ("itemId2").equals(t.getItemId())))).thenReturn(potentialTradeStats2);

        Trade expected1 = new Trade(ubiTrade1);
        expected1.setItem(existingItem1);
        expected1.setMinutesToTrade(101);
        expected1.setTradePriority(102L);

        Trade expected2 = new Trade(ubiTrade2);
        expected2.setItem(existingItem2);
        expected2.setMinutesToTrade(201);
        expected2.setTradePriority(202L);

        Trade expected4 = new Trade(ubiTrade4);
        expected4.setItem(new Item("nonExistingItemId"));
        expected4.setMinutesToTrade(null);
        expected4.setTradePriority(null);


        List<Trade> result = tradeService.calculateTradeStatsForUbiTrades(ubiTrades);

        result.forEach(System.out::println);

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(res -> res.equals(expected1) && res.getItemName().equals("existingItemId1")));
        assertTrue(result.stream().anyMatch(res -> res.equals(expected2) && res.getItemName().equals("existingItemId2")));
    }
}