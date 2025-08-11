package github.ricemonger.fast_sell_trade_manager.services.factories;

import github.ricemonger.fast_sell_trade_manager.services.CommonValuesService;
import github.ricemonger.fast_sell_trade_manager.services.DTOs.ItemMedianPriceAndRarity;
import github.ricemonger.fast_sell_trade_manager.services.DTOs.PotentialTrade;
import github.ricemonger.utils.DTOs.common.ItemCurrentPrices;
import github.ricemonger.utils.enums.ItemRarity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SpringBootTest
class PotentialTradeFactoryTest {
    @SpyBean
    private PotentialTradeItemsFactory potentialTradeItemsFactory;
    @MockBean
    private CommonValuesService commonValuesService;

    @Test
    public void createPotentialTradesForUser_should_create_expected_potential_trades() {
        ItemCurrentPrices currentPrices1 = new ItemCurrentPrices();
        currentPrices1.setItemId("itemId1");

        ItemMedianPriceAndRarity medianPriceAndRarity1 = new ItemMedianPriceAndRarity();
        medianPriceAndRarity1.setItemId("itemId1");
        medianPriceAndRarity1.setMonthMedianPrice(500);

        ItemCurrentPrices currentPrices2 = new ItemCurrentPrices();
        currentPrices2.setItemId("itemId2");

        ItemMedianPriceAndRarity medianPriceAndRarity2 = new ItemMedianPriceAndRarity();
        medianPriceAndRarity2.setItemId("itemId2");
        medianPriceAndRarity2.setMonthMedianPrice(500);

        ItemCurrentPrices currentPrices3 = new ItemCurrentPrices();
        currentPrices3.setItemId("itemId3");

        ItemMedianPriceAndRarity medianPriceAndRarity3 = new ItemMedianPriceAndRarity();
        medianPriceAndRarity3.setItemId("itemId3");
        medianPriceAndRarity3.setMonthMedianPrice(null);

        ItemCurrentPrices currentPrices4 = new ItemCurrentPrices();
        currentPrices4.setItemId("itemId4");

        ItemMedianPriceAndRarity medianPriceAndRarity5 = new ItemMedianPriceAndRarity();
        medianPriceAndRarity5.setItemId("itemId5");
        medianPriceAndRarity5.setMonthMedianPrice(500);

        List<ItemCurrentPrices> currentPrices = List.of(currentPrices1, currentPrices2, currentPrices3, currentPrices4);

        List<ItemMedianPriceAndRarity> medianPriceAndRarities = List.of(medianPriceAndRarity1, medianPriceAndRarity2, medianPriceAndRarity3, medianPriceAndRarity5);

        Integer minMedianPriceDifference = 500;
        Integer minMedianPriceDifferencePercentage = 100;

        doReturn(new PotentialTrade()).when(potentialTradeItemsFactory).createPotentialTradeItemsForUserOrNull(any(), any(), any(), any());
        doReturn(null).when(potentialTradeItemsFactory).createPotentialTradeItemsForUserOrNull(currentPrices1, medianPriceAndRarity1, minMedianPriceDifference, minMedianPriceDifferencePercentage);
        doReturn(new PotentialTrade("itemId2", 999, 500, 100, true)).when(potentialTradeItemsFactory).createPotentialTradeItemsForUserOrNull(currentPrices2, medianPriceAndRarity2, minMedianPriceDifference, minMedianPriceDifferencePercentage);


        List<PotentialTrade> trades = potentialTradeItemsFactory.createPotentialTradeItemsForUser(currentPrices, medianPriceAndRarities, minMedianPriceDifference, minMedianPriceDifferencePercentage);

        assertEquals(1, trades.size());
        assertEquals(new PotentialTrade("itemId2", 999, 500, 100, true), trades.get(0));
    }

    @Test
    public void createPotentialTradeForUserOrNull_should_create_expected_potential_trade_for_edge_buy_price_and_high_sell_price() {
        ItemCurrentPrices currentPrices = new ItemCurrentPrices();
        currentPrices.setItemId("itemId");
        currentPrices.setMaxBuyPrice(1000);
        currentPrices.setMinSellPrice(100000);
        ItemMedianPriceAndRarity medianPriceAndRarity = new ItemMedianPriceAndRarity();
        medianPriceAndRarity.setMonthMedianPrice(500);
        int minMedianPriceDifference = 500;
        int minMedianPriceDifferencePercentage = 100;

        PotentialTrade trade = potentialTradeItemsFactory.createPotentialTradeItemsForUserOrNull(currentPrices, medianPriceAndRarity, minMedianPriceDifference, minMedianPriceDifferencePercentage);

        PotentialTrade expected = new PotentialTrade("itemId", 999, 500, 100, true);

        assertEquals(expected, trade);
    }

    @Test
    public void createPotentialTradeForUserOrNull_should_create_expected_potential_trade_for_buy_price_edge() {
        ItemCurrentPrices currentPrices = new ItemCurrentPrices();
        currentPrices.setItemId("itemId");
        currentPrices.setMaxBuyPrice(1000);
        currentPrices.setMinSellPrice(10);
        ItemMedianPriceAndRarity medianPriceAndRarity = new ItemMedianPriceAndRarity();
        medianPriceAndRarity.setMonthMedianPrice(500);
        int minMedianPriceDifference = 500;
        int minMedianPriceDifferencePercentage = 100;

        PotentialTrade trade = potentialTradeItemsFactory.createPotentialTradeItemsForUserOrNull(currentPrices, medianPriceAndRarity, minMedianPriceDifference, minMedianPriceDifferencePercentage);

        PotentialTrade expected = new PotentialTrade("itemId", 999, 500, 100, true);

        assertEquals(expected, trade);
    }

    @Test
    public void createPotentialTradeForUserOrNull_should_create_expected_potential_trade_for_buy_price_no_edge() {
        ItemCurrentPrices currentPrices = new ItemCurrentPrices();
        currentPrices.setItemId("itemId");
        currentPrices.setMaxBuyPrice(1500);
        currentPrices.setMinSellPrice(10000);
        ItemMedianPriceAndRarity medianPriceAndRarity = new ItemMedianPriceAndRarity();
        medianPriceAndRarity.setMonthMedianPrice(500);
        int minMedianPriceDifference = 500;
        int minMedianPriceDifferencePercentage = 100;

        PotentialTrade trade = potentialTradeItemsFactory.createPotentialTradeItemsForUserOrNull(currentPrices, medianPriceAndRarity, minMedianPriceDifference, minMedianPriceDifferencePercentage);

        PotentialTrade expected = new PotentialTrade("itemId", 1499, 1000, 200, true);

        assertEquals(expected, trade);
    }

    @Test
    public void createPotentialTradeForUserOrNull_should_create_expected_potential_trade_for_sell_price_edge() {
        ItemCurrentPrices currentPrices = new ItemCurrentPrices();
        currentPrices.setItemId("itemId");
        currentPrices.setMaxBuyPrice(999);
        currentPrices.setMinSellPrice(1000);
        ItemMedianPriceAndRarity medianPriceAndRarity = new ItemMedianPriceAndRarity();
        medianPriceAndRarity.setMonthMedianPrice(500);
        int minMedianPriceDifference = 500;
        int minMedianPriceDifferencePercentage = 100;

        PotentialTrade trade = potentialTradeItemsFactory.createPotentialTradeItemsForUserOrNull(currentPrices, medianPriceAndRarity, minMedianPriceDifference, minMedianPriceDifferencePercentage);

        PotentialTrade expected = new PotentialTrade("itemId", 999, 500, 100, false);

        assertEquals(expected, trade);
    }

    @Test
    public void createPotentialTradeForUserOrNull_should_create_expected_potential_trade_for_sell_price_no_edge() {
        ItemCurrentPrices currentPrices = new ItemCurrentPrices();
        currentPrices.setItemId("itemId");
        currentPrices.setMaxBuyPrice(null);
        currentPrices.setMinSellPrice(1500);
        ItemMedianPriceAndRarity medianPriceAndRarity = new ItemMedianPriceAndRarity();
        medianPriceAndRarity.setMonthMedianPrice(500);
        int minMedianPriceDifference = 500;
        int minMedianPriceDifferencePercentage = 100;

        PotentialTrade trade = potentialTradeItemsFactory.createPotentialTradeItemsForUserOrNull(currentPrices, medianPriceAndRarity, minMedianPriceDifference, minMedianPriceDifferencePercentage);

        PotentialTrade expected = new PotentialTrade("itemId", 1499, 1000, 200, false);

        assertEquals(expected, trade);
    }

    @Test
    public void createPotentialTradeForUserOrNull_should_create_expected_potential_trade_for_null_sell_price() {
        ItemCurrentPrices currentPrices = new ItemCurrentPrices();
        currentPrices.setItemId("itemId");
        currentPrices.setMaxBuyPrice(1500);
        currentPrices.setMinSellPrice(null);
        ItemMedianPriceAndRarity medianPriceAndRarity = new ItemMedianPriceAndRarity();
        medianPriceAndRarity.setRarity(ItemRarity.RARE);
        medianPriceAndRarity.setMonthMedianPrice(500);
        int minMedianPriceDifference = 500;
        int minMedianPriceDifferencePercentage = 100;

        when(commonValuesService.getMaximumPriceByRarity(ItemRarity.RARE)).thenReturn(10000);

        PotentialTrade trade = potentialTradeItemsFactory.createPotentialTradeItemsForUserOrNull(currentPrices, medianPriceAndRarity, minMedianPriceDifference, minMedianPriceDifferencePercentage);

        PotentialTrade expected = new PotentialTrade("itemId", 1499, 1000, 200, true);

        assertEquals(expected, trade);
    }

    @Test
    public void createPotentialTradeForUserOrNull_should_return_null_if_low_prices() {
        ItemCurrentPrices currentPrices = new ItemCurrentPrices();
        currentPrices.setItemId("itemId");
        currentPrices.setMaxBuyPrice(999);
        currentPrices.setMinSellPrice(999);
        ItemMedianPriceAndRarity medianPriceAndRarity = new ItemMedianPriceAndRarity();
        medianPriceAndRarity.setMonthMedianPrice(500);
        int minMedianPriceDifference = 500;
        int minMedianPriceDifferencePercentage = 100;

        PotentialTrade trade = potentialTradeItemsFactory.createPotentialTradeItemsForUserOrNull(currentPrices, medianPriceAndRarity, minMedianPriceDifference, minMedianPriceDifferencePercentage);

        assertNull(trade);
    }
}