package github.ricemonger.trades_manager.services.factories;

import github.ricemonger.trades_manager.services.DTOs.PersonalItem;
import github.ricemonger.trades_manager.services.DTOs.PotentialPersonalBuyTrade;
import github.ricemonger.trades_manager.services.DTOs.PotentialPersonalSellTrade;
import github.ricemonger.trades_manager.services.DTOs.Trade;
import github.ricemonger.trades_manager.services.PotentialTradeStatsService;
import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.DTOs.common.PotentialTradeStats;
import github.ricemonger.utils.DTOs.personal.ItemResaleLock;
import github.ricemonger.utils.enums.TradeCategory;
import github.ricemonger.utils.enums.TradeOperationType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class PotentialTradeFactoryTest {
    @SpyBean
    private PotentialTradeFactory potentialTradeFactory;
    @MockBean
    private PotentialTradeStatsService potentialTradeStatsService;

    @Test
    public void getResultingPersonalSellTrades_should_return_expected_potential_Sell_trades_more_trades_limit_left_choose_with_highest_priority_for_each_item() {
        PersonalItem personalItemExistingTrade1 = new PersonalItem();
        personalItemExistingTrade1.setItem(new Item("1"));
        personalItemExistingTrade1.setPriorityMultiplier(1);
        personalItemExistingTrade1.setTradeAlreadyExists(true);
        Trade ubiTrade1 = new Trade();
        ubiTrade1.setProposedPaymentPrice(10);
        personalItemExistingTrade1.setExistingTrade(ubiTrade1);

        PersonalItem personalItemLowerPriority2 = new PersonalItem();
        personalItemLowerPriority2.setItem(new Item("2"));
        personalItemLowerPriority2.setPriorityMultiplier(1);
        personalItemLowerPriority2.setTradeAlreadyExists(false);

        PersonalItem personalItemHigherPriority3 = new PersonalItem();
        personalItemHigherPriority3.setItem(new Item("3"));
        personalItemHigherPriority3.setPriorityMultiplier(2);
        personalItemHigherPriority3.setTradeAlreadyExists(false);

        List<PersonalItem> personalItems = List.of(personalItemExistingTrade1, personalItemLowerPriority2, personalItemHigherPriority3);
        List<ItemResaleLock> resaleLocks = List.of();

        int soldIn24h = 5;
        int sellSlots = 5;
        int sellLimit = 6;

        List<PotentialPersonalSellTrade> sellTrades = List.of(
                new PotentialPersonalSellTrade(personalItemExistingTrade1, new PotentialTradeStats(1, 2, 3L)),
                new PotentialPersonalSellTrade(personalItemExistingTrade1, new PotentialTradeStats(1, 2, 2L)),
                new PotentialPersonalSellTrade(personalItemLowerPriority2, new PotentialTradeStats(4, 5, 7L)),
                new PotentialPersonalSellTrade(personalItemLowerPriority2, new PotentialTradeStats(5, 6, 11L)),
                new PotentialPersonalSellTrade(personalItemHigherPriority3, new PotentialTradeStats(7, 8, 4L))
        );

        doReturn(sellTrades).when(potentialTradeFactory).getFilteredPotentialSellTradesForUser(any(), any());

        List<PotentialPersonalSellTrade> result = potentialTradeFactory.getResultingPersonalSellTrades(personalItems, resaleLocks, soldIn24h, sellSlots, sellLimit);
        assertEquals(2, result.size());
        assertTrue(result.contains(new PotentialPersonalSellTrade(personalItemLowerPriority2, new PotentialTradeStats(5, 6, 11L))));
        assertTrue(result.contains(new PotentialPersonalSellTrade(personalItemExistingTrade1, new PotentialTradeStats(1, 2, 3L))));
    }

    @Test
    public void getResultingPersonalSellTrades_should_return_expected_potential_Sell_trades_more_trades_then_slots_choose_with_highest_priority_for_each_item() {
        PersonalItem personalItemExistingTrade1 = new PersonalItem();
        personalItemExistingTrade1.setItem(new Item("1"));
        personalItemExistingTrade1.setPriorityMultiplier(1);
        personalItemExistingTrade1.setTradeAlreadyExists(true);
        Trade ubiTrade1 = new Trade();
        ubiTrade1.setProposedPaymentPrice(10);
        personalItemExistingTrade1.setExistingTrade(ubiTrade1);

        PersonalItem personalItemLowerPriority2 = new PersonalItem();
        personalItemLowerPriority2.setItem(new Item("2"));
        personalItemLowerPriority2.setPriorityMultiplier(1);
        personalItemLowerPriority2.setTradeAlreadyExists(false);

        PersonalItem personalItemHigherPriority3 = new PersonalItem();
        personalItemHigherPriority3.setItem(new Item("3"));
        personalItemHigherPriority3.setPriorityMultiplier(2);
        personalItemHigherPriority3.setTradeAlreadyExists(false);

        List<PersonalItem> personalItems = List.of(personalItemExistingTrade1, personalItemLowerPriority2, personalItemHigherPriority3);
        List<ItemResaleLock> resaleLocks = List.of();

        int boughtIn24h = 0;
        int sellSlots = 2;
        int sellLimit = 10;

        List<PotentialPersonalSellTrade> sellTrades = List.of(
                new PotentialPersonalSellTrade(personalItemExistingTrade1, new PotentialTradeStats(1, 2, 3L)),
                new PotentialPersonalSellTrade(personalItemLowerPriority2, new PotentialTradeStats(4, 5, 7L)),
                new PotentialPersonalSellTrade(personalItemLowerPriority2, new PotentialTradeStats(5, 6, 11L)),
                new PotentialPersonalSellTrade(personalItemHigherPriority3, new PotentialTradeStats(7, 8, 4L))
        );

        doReturn(sellTrades).when(potentialTradeFactory).getFilteredPotentialSellTradesForUser(any(), any());

        List<PotentialPersonalSellTrade> result = potentialTradeFactory.getResultingPersonalSellTrades(personalItems, resaleLocks, boughtIn24h,
                sellSlots, sellLimit);
        assertEquals(2, result.size());
        assertTrue(result.contains(new PotentialPersonalSellTrade(personalItemLowerPriority2, new PotentialTradeStats(5, 6, 11L))));
        assertTrue(result.contains(new PotentialPersonalSellTrade(personalItemHigherPriority3, new PotentialTradeStats(7, 8, 4L))));
    }

    @Test
    public void getResultingPersonalSellTrades_should_return_expected_potential_Sell_trades_more_slots_then_trades() {
        PersonalItem personalItemExistingTrade1 = new PersonalItem();
        personalItemExistingTrade1.setItem(new Item("1"));
        personalItemExistingTrade1.setPriorityMultiplier(1);
        personalItemExistingTrade1.setTradeAlreadyExists(true);
        Trade ubiTrade1 = new Trade();
        ubiTrade1.setProposedPaymentPrice(10);
        personalItemExistingTrade1.setExistingTrade(ubiTrade1);

        PersonalItem personalItemLowerPriority2 = new PersonalItem();
        personalItemLowerPriority2.setItem(new Item("2"));
        personalItemLowerPriority2.setPriorityMultiplier(1);
        personalItemLowerPriority2.setTradeAlreadyExists(false);

        PersonalItem personalItemHigherPriority3 = new PersonalItem();
        personalItemHigherPriority3.setItem(new Item("3"));
        personalItemHigherPriority3.setPriorityMultiplier(2);
        personalItemHigherPriority3.setTradeAlreadyExists(false);

        List<PersonalItem> personalItems = List.of(personalItemExistingTrade1, personalItemLowerPriority2, personalItemHigherPriority3);
        List<ItemResaleLock> resaleLocks = List.of();

        int boughtIn24h = 0;
        int sellSlots = 2;
        int sellLimit = 10;

        List<PotentialPersonalSellTrade> sellTrades = List.of(
                new PotentialPersonalSellTrade(personalItemExistingTrade1, new PotentialTradeStats(1, 2, 3L))
        );

        doReturn(sellTrades).when(potentialTradeFactory).getFilteredPotentialSellTradesForUser(any(), any());
        List<PotentialPersonalSellTrade> result = potentialTradeFactory.getResultingPersonalSellTrades(personalItems, resaleLocks, boughtIn24h, sellSlots,
                sellLimit);

        assertEquals(1, result.size());
        assertEquals(new PotentialPersonalSellTrade(personalItemExistingTrade1, new PotentialTradeStats(1, 2, 3L)), result.get(0));
    }

    @Test
    public void getFilteredPotentialSellTradesForUser_should_filter_personal_items_and_create_potential_sell_trades() {
        PersonalItem notOwnedPersonalItem = new PersonalItem();
        notOwnedPersonalItem.setItem(new Item("1"));
        notOwnedPersonalItem.setIsOwned(false);
        notOwnedPersonalItem.setTradeOperationType(TradeOperationType.SELL);
        PotentialTradeStats potentialTradeStats1 = new PotentialTradeStats(1, 2, 3L);
        when(potentialTradeStatsService.getPotentialSellTradesStatsOfItem(notOwnedPersonalItem.getItem())).thenReturn(List.of(potentialTradeStats1));

        PersonalItem wrongOperationTypePersonalItem = new PersonalItem();
        wrongOperationTypePersonalItem.setItem(new Item("2"));
        wrongOperationTypePersonalItem.setIsOwned(true);
        wrongOperationTypePersonalItem.setTradeOperationType(TradeOperationType.BUY);
        PotentialTradeStats potentialTradeStats2 = new PotentialTradeStats(4, 5, 6L);
        when(potentialTradeStatsService.getPotentialSellTradesStatsOfItem(wrongOperationTypePersonalItem.getItem())).thenReturn(List.of(potentialTradeStats2));

        PersonalItem resaleLockedPersonalItem = new PersonalItem();
        resaleLockedPersonalItem.setItem(new Item("6"));
        resaleLockedPersonalItem.setIsOwned(true);
        resaleLockedPersonalItem.setTradeOperationType(TradeOperationType.SELL);
        PotentialTradeStats potentialTradeStats12 = new PotentialTradeStats(4, 5, 6L);
        when(potentialTradeStatsService.getPotentialSellTradesStatsOfItem(resaleLockedPersonalItem.getItem())).thenReturn(List.of(potentialTradeStats12));

        PersonalItem alreadyExistingTradeWithWrongCategoryPersonalItem = new PersonalItem();
        alreadyExistingTradeWithWrongCategoryPersonalItem.setItem(new Item("3"));
        alreadyExistingTradeWithWrongCategoryPersonalItem.setIsOwned(true);
        alreadyExistingTradeWithWrongCategoryPersonalItem.setTradeOperationType(TradeOperationType.BUY_AND_SELL);
        Trade ubiTrade1 = new Trade();
        ubiTrade1.setCategory(TradeCategory.Buy);
        ubiTrade1.setProposedPaymentPrice(30);
        ubiTrade1.setMinutesToTrade(20);
        ubiTrade1.setTradePriority(100L);
        alreadyExistingTradeWithWrongCategoryPersonalItem.setTradeAlreadyExists(true);
        alreadyExistingTradeWithWrongCategoryPersonalItem.setExistingTrade(ubiTrade1);
        alreadyExistingTradeWithWrongCategoryPersonalItem.setSellBoundaryPrice(25);
        PotentialTradeStats potentialTradeStats3 = new PotentialTradeStats(25, 8, 9L);
        PotentialTradeStats potentialTradeStats4 = new PotentialTradeStats(26, 11, 12L);
        PotentialTradeStats potentialTradeStatsBoundaryPrice7 = new PotentialTradeStats(24, 20, 21L);
        PotentialTradeStats ubiTradeStatsWrongCategory = new PotentialTradeStats(30, 20, 100L);
        when(potentialTradeStatsService.getPotentialSellTradesStatsOfItem(alreadyExistingTradeWithWrongCategoryPersonalItem.getItem())).thenReturn(List.of(potentialTradeStats3, potentialTradeStats4, potentialTradeStatsBoundaryPrice7));

        PersonalItem alreadyExistingTradeWithProperCategoryPersonalItem = new PersonalItem();
        alreadyExistingTradeWithProperCategoryPersonalItem.setItem(new Item("4"));
        alreadyExistingTradeWithProperCategoryPersonalItem.setIsOwned(true);
        alreadyExistingTradeWithProperCategoryPersonalItem.setTradeOperationType(TradeOperationType.SELL);
        Trade ubiTrade2 = new Trade();
        ubiTrade2.setCategory(TradeCategory.Sell);
        ubiTrade2.setProposedPaymentPrice(33);
        ubiTrade2.setMinutesToTrade(20);
        ubiTrade2.setTradePriority(100L);
        alreadyExistingTradeWithProperCategoryPersonalItem.setTradeAlreadyExists(true);
        alreadyExistingTradeWithProperCategoryPersonalItem.setExistingTrade(ubiTrade2);
        alreadyExistingTradeWithProperCategoryPersonalItem.setSellBoundaryPrice(44);
        alreadyExistingTradeWithProperCategoryPersonalItem.getItem().setMonthMedianPrice(30);
        alreadyExistingTradeWithProperCategoryPersonalItem.setMinMedianPriceDifference(14);
        alreadyExistingTradeWithProperCategoryPersonalItem.setMinMedianPriceDifferencePercent(50);
        PotentialTradeStats potentialTradeStats5 = new PotentialTradeStats(45, 14, 15L);
        PotentialTradeStats potentialTradeStats6 = new PotentialTradeStats(46, 17, 18L);
        PotentialTradeStats potentialTradeStatsMedianPriceDiffPercent9 = new PotentialTradeStats(44, 26, 27L);
        PotentialTradeStats ubiTradeStatsProperCategory = new PotentialTradeStats(33, 20, 100L);
        when(potentialTradeStatsService.getPotentialSellTradesStatsOfItem(alreadyExistingTradeWithProperCategoryPersonalItem.getItem())).thenReturn(List.of(potentialTradeStats5, potentialTradeStats6, potentialTradeStatsMedianPriceDiffPercent9));

        PersonalItem noAlreadyExistingTradePersonalItem = new PersonalItem();
        noAlreadyExistingTradePersonalItem.setItem(new Item("5"));
        noAlreadyExistingTradePersonalItem.setIsOwned(true);
        noAlreadyExistingTradePersonalItem.setTradeAlreadyExists(false);
        noAlreadyExistingTradePersonalItem.setTradeOperationType(TradeOperationType.BUY_AND_SELL);
        noAlreadyExistingTradePersonalItem.setSellBoundaryPrice(200);
        noAlreadyExistingTradePersonalItem.getItem().setMonthMedianPrice(200);
        noAlreadyExistingTradePersonalItem.setMinMedianPriceDifference(50);
        noAlreadyExistingTradePersonalItem.setMinMedianPriceDifferencePercent(10);
        PotentialTradeStats potentialTradeStatsMedianPriceDiff8 = new PotentialTradeStats(249, 23, 24L);
        PotentialTradeStats potentialTradeStats10 = new PotentialTradeStats(250, 29, 30L);
        PotentialTradeStats potentialTradeStats11 = new PotentialTradeStats(251, 32, 33L);
        when(potentialTradeStatsService.getPotentialSellTradesStatsOfItem(noAlreadyExistingTradePersonalItem.getItem())).thenReturn(List.of(potentialTradeStatsMedianPriceDiff8, potentialTradeStats10, potentialTradeStats11));

        List<ItemResaleLock> resaleLocks = List.of(new ItemResaleLock("6", LocalDateTime.of(2021, 1, 1, 0, 0)));


        List<PotentialPersonalSellTrade> result = potentialTradeFactory.getFilteredPotentialSellTradesForUser(
                List.of(notOwnedPersonalItem, wrongOperationTypePersonalItem, resaleLockedPersonalItem, alreadyExistingTradeWithWrongCategoryPersonalItem, alreadyExistingTradeWithProperCategoryPersonalItem, noAlreadyExistingTradePersonalItem), resaleLocks);

        verify(potentialTradeStatsService, times(0)).getPotentialSellTradesStatsOfItem(notOwnedPersonalItem.getItem());
        verify(potentialTradeStatsService, times(0)).getPotentialSellTradesStatsOfItem(wrongOperationTypePersonalItem.getItem());
        verify(potentialTradeStatsService, times(0)).getPotentialSellTradesStatsOfItem(resaleLockedPersonalItem.getItem());

        assertTrue(result.contains(new PotentialPersonalSellTrade(alreadyExistingTradeWithWrongCategoryPersonalItem, potentialTradeStats3)));
        assertTrue(result.contains(new PotentialPersonalSellTrade(alreadyExistingTradeWithWrongCategoryPersonalItem, potentialTradeStats4)));
        assertFalse(result.contains(new PotentialPersonalSellTrade(alreadyExistingTradeWithWrongCategoryPersonalItem, potentialTradeStatsBoundaryPrice7)));
        assertFalse(result.contains(new PotentialPersonalSellTrade(alreadyExistingTradeWithWrongCategoryPersonalItem, ubiTradeStatsWrongCategory)));

        assertTrue(result.contains(new PotentialPersonalSellTrade(alreadyExistingTradeWithProperCategoryPersonalItem, potentialTradeStats5)));
        assertTrue(result.contains(new PotentialPersonalSellTrade(alreadyExistingTradeWithProperCategoryPersonalItem, potentialTradeStats6)));
        assertFalse(result.contains(new PotentialPersonalSellTrade(alreadyExistingTradeWithProperCategoryPersonalItem, potentialTradeStatsMedianPriceDiffPercent9)));
        assertTrue(result.contains(new PotentialPersonalSellTrade(alreadyExistingTradeWithProperCategoryPersonalItem, ubiTradeStatsProperCategory)));

        assertFalse(result.contains(new PotentialPersonalSellTrade(noAlreadyExistingTradePersonalItem, potentialTradeStatsMedianPriceDiff8)));
        assertTrue(result.contains(new PotentialPersonalSellTrade(noAlreadyExistingTradePersonalItem, potentialTradeStats10)));
        assertTrue(result.contains(new PotentialPersonalSellTrade(noAlreadyExistingTradePersonalItem, potentialTradeStats11)));

        assertEquals(7, result.size());
    }

    @Test
    public void getResultingPersonalBuyTrades_should_return_expected_potential_buy_trades_low_credit_amount_choose_with_highest_priority_for_each_item() {
        PersonalItem personalItemExistingTrade1 = new PersonalItem();
        personalItemExistingTrade1.setItem(new Item("1"));
        personalItemExistingTrade1.setPriorityMultiplier(1);
        personalItemExistingTrade1.setTradeAlreadyExists(true);
        Trade ubiTrade1 = new Trade();
        ubiTrade1.setProposedPaymentPrice(10);
        personalItemExistingTrade1.setExistingTrade(ubiTrade1);

        PersonalItem personalItemLowerPriority2 = new PersonalItem();
        personalItemLowerPriority2.setItem(new Item("2"));
        personalItemLowerPriority2.setPriorityMultiplier(1);
        personalItemLowerPriority2.setTradeAlreadyExists(false);

        PersonalItem personalItemHigherPriority3 = new PersonalItem();
        personalItemHigherPriority3.setItem(new Item("3"));
        personalItemHigherPriority3.setPriorityMultiplier(2);
        personalItemHigherPriority3.setTradeAlreadyExists(false);

        int creditAmount = 10;
        int boughtIn24h = 5;
        int buySlots = 5;
        int buyLimit = 10;

        List<PotentialPersonalBuyTrade> buyTrades = List.of(
                new PotentialPersonalBuyTrade(personalItemHigherPriority3, new PotentialTradeStats(5, 5, 1L)),
                new PotentialPersonalBuyTrade(personalItemExistingTrade1, new PotentialTradeStats(1, 2, 2L)),
                new PotentialPersonalBuyTrade(personalItemExistingTrade1, new PotentialTradeStats(5, 2, 3L)),
                new PotentialPersonalBuyTrade(personalItemLowerPriority2, new PotentialTradeStats(1, 5, 7L)),
                new PotentialPersonalBuyTrade(personalItemHigherPriority3, new PotentialTradeStats(1, 8, 4L)),
                new PotentialPersonalBuyTrade(personalItemLowerPriority2, new PotentialTradeStats(10, 6, 11L))
        );

        List<PersonalItem> personalItems = List.of(personalItemExistingTrade1, personalItemLowerPriority2, personalItemHigherPriority3);

        doReturn(buyTrades).when(potentialTradeFactory).getFilteredPotentialBuyTradesForUser(any());

        List<PotentialPersonalBuyTrade> result = potentialTradeFactory.getResultingPersonalBuyTrades(personalItems, creditAmount, boughtIn24h, buySlots, buyLimit);
        assertEquals(3, result.size());
        assertTrue(result.contains(new PotentialPersonalBuyTrade(personalItemLowerPriority2, new PotentialTradeStats(10, 6, 11L))));
        assertTrue(result.contains(new PotentialPersonalBuyTrade(personalItemExistingTrade1, new PotentialTradeStats(5, 2, 3L))));
        assertTrue(result.contains(new PotentialPersonalBuyTrade(personalItemHigherPriority3, new PotentialTradeStats(5, 5, 1L))));
    }

    @Test
    public void getResultingPersonalBuyTrades_should_return_expected_potential_buy_trades_more_trades_limit_left_choose_with_highest_priority_for_each_item() {
        PersonalItem personalItemExistingTrade1 = new PersonalItem();
        personalItemExistingTrade1.setItem(new Item("1"));
        personalItemExistingTrade1.setPriorityMultiplier(1);
        personalItemExistingTrade1.setTradeAlreadyExists(true);
        Trade ubiTrade1 = new Trade();
        ubiTrade1.setProposedPaymentPrice(10);
        personalItemExistingTrade1.setExistingTrade(ubiTrade1);

        PersonalItem personalItemLowerPriority2 = new PersonalItem();
        personalItemLowerPriority2.setItem(new Item("2"));
        personalItemLowerPriority2.setPriorityMultiplier(1);
        personalItemLowerPriority2.setTradeAlreadyExists(false);

        PersonalItem personalItemHigherPriority3 = new PersonalItem();
        personalItemHigherPriority3.setItem(new Item("3"));
        personalItemHigherPriority3.setPriorityMultiplier(2);
        personalItemHigherPriority3.setTradeAlreadyExists(false);

        List<PersonalItem> personalItems = List.of(personalItemExistingTrade1, personalItemLowerPriority2, personalItemHigherPriority3);

        int creditAmount = 1000;
        int boughtIn24h = 5;
        int buySlots = 5;
        int buyLimit = 6;

        List<PotentialPersonalBuyTrade> buyTrades = List.of(
                new PotentialPersonalBuyTrade(personalItemExistingTrade1, new PotentialTradeStats(1, 2, 3L)),
                new PotentialPersonalBuyTrade(personalItemExistingTrade1, new PotentialTradeStats(1, 2, 2L)),
                new PotentialPersonalBuyTrade(personalItemLowerPriority2, new PotentialTradeStats(4, 5, 7L)),
                new PotentialPersonalBuyTrade(personalItemLowerPriority2, new PotentialTradeStats(5, 6, 11L)),
                new PotentialPersonalBuyTrade(personalItemHigherPriority3, new PotentialTradeStats(7, 8, 4L))
        );

        doReturn(buyTrades).when(potentialTradeFactory).getFilteredPotentialBuyTradesForUser(any());

        List<PotentialPersonalBuyTrade> result = potentialTradeFactory.getResultingPersonalBuyTrades(personalItems, creditAmount, boughtIn24h, buySlots, buyLimit);
        assertEquals(2, result.size());
        assertTrue(result.contains(new PotentialPersonalBuyTrade(personalItemLowerPriority2, new PotentialTradeStats(5, 6, 11L))));
        assertTrue(result.contains(new PotentialPersonalBuyTrade(personalItemExistingTrade1, new PotentialTradeStats(1, 2, 3L))));
    }

    @Test
    public void getResultingPersonalBuyTrades_should_return_expected_potential_buy_trades_more_trades_then_slots_choose_with_highest_priority_for_each_item() {
        PersonalItem personalItemExistingTrade1 = new PersonalItem();
        personalItemExistingTrade1.setItem(new Item("1"));
        personalItemExistingTrade1.setPriorityMultiplier(1);
        personalItemExistingTrade1.setTradeAlreadyExists(true);
        Trade ubiTrade1 = new Trade();
        ubiTrade1.setProposedPaymentPrice(10);
        personalItemExistingTrade1.setExistingTrade(ubiTrade1);

        PersonalItem personalItemLowerPriority2 = new PersonalItem();
        personalItemLowerPriority2.setItem(new Item("2"));
        personalItemLowerPriority2.setPriorityMultiplier(1);
        personalItemLowerPriority2.setTradeAlreadyExists(false);

        PersonalItem personalItemHigherPriority3 = new PersonalItem();
        personalItemHigherPriority3.setItem(new Item("3"));
        personalItemHigherPriority3.setPriorityMultiplier(2);
        personalItemHigherPriority3.setTradeAlreadyExists(false);

        List<PersonalItem> personalItems = List.of(personalItemExistingTrade1, personalItemLowerPriority2, personalItemHigherPriority3);

        int creditAmount = 1000;
        int boughtIn24h = 0;
        int buySlots = 2;
        int buyLimit = 10;

        List<PotentialPersonalBuyTrade> buyTrades = List.of(
                new PotentialPersonalBuyTrade(personalItemExistingTrade1, new PotentialTradeStats(1, 2, 3L)),
                new PotentialPersonalBuyTrade(personalItemLowerPriority2, new PotentialTradeStats(4, 5, 7L)),
                new PotentialPersonalBuyTrade(personalItemLowerPriority2, new PotentialTradeStats(5, 6, 11L)),
                new PotentialPersonalBuyTrade(personalItemHigherPriority3, new PotentialTradeStats(7, 8, 4L))
        );

        doReturn(buyTrades).when(potentialTradeFactory).getFilteredPotentialBuyTradesForUser(any());

        List<PotentialPersonalBuyTrade> result = potentialTradeFactory.getResultingPersonalBuyTrades(personalItems, creditAmount, boughtIn24h, buySlots, buyLimit);
        assertEquals(2, result.size());
        assertTrue(result.contains(new PotentialPersonalBuyTrade(personalItemLowerPriority2, new PotentialTradeStats(5, 6, 11L))));
        assertTrue(result.contains(new PotentialPersonalBuyTrade(personalItemHigherPriority3, new PotentialTradeStats(7, 8, 4L))));
    }

    @Test
    public void getResultingPersonalBuyTrades_should_return_expected_potential_buy_trades_more_slots_then_trades() {
        PersonalItem personalItemExistingTrade1 = new PersonalItem();
        personalItemExistingTrade1.setItem(new Item("1"));
        personalItemExistingTrade1.setPriorityMultiplier(1);
        personalItemExistingTrade1.setTradeAlreadyExists(true);
        Trade ubiTrade1 = new Trade();
        ubiTrade1.setProposedPaymentPrice(10);
        personalItemExistingTrade1.setExistingTrade(ubiTrade1);

        PersonalItem personalItemLowerPriority2 = new PersonalItem();
        personalItemLowerPriority2.setItem(new Item("2"));
        personalItemLowerPriority2.setPriorityMultiplier(1);
        personalItemLowerPriority2.setTradeAlreadyExists(false);

        PersonalItem personalItemHigherPriority3 = new PersonalItem();
        personalItemHigherPriority3.setItem(new Item("3"));
        personalItemHigherPriority3.setPriorityMultiplier(2);
        personalItemHigherPriority3.setTradeAlreadyExists(false);

        List<PersonalItem> personalItems = List.of(personalItemExistingTrade1, personalItemLowerPriority2, personalItemHigherPriority3);

        int creditAmount = 1000;
        int boughtIn24h = 0;
        int buySlots = 2;
        int buyLimit = 10;

        List<PotentialPersonalBuyTrade> buyTrades = List.of(
                new PotentialPersonalBuyTrade(personalItemExistingTrade1, new PotentialTradeStats(1, 2, 3L))
        );

        doReturn(buyTrades).when(potentialTradeFactory).getFilteredPotentialBuyTradesForUser(any());
        List<PotentialPersonalBuyTrade> result = potentialTradeFactory.getResultingPersonalBuyTrades(personalItems, creditAmount, boughtIn24h, buySlots, buyLimit);

        assertEquals(1, result.size());
        assertEquals(new PotentialPersonalBuyTrade(personalItemExistingTrade1, new PotentialTradeStats(1, 2, 3L)), result.get(0));
    }

    @Test
    public void getFilteredPotentialBuyTradesForUser_should_filter_personal_items_and_create_potential_buy_trades() {
        PersonalItem ownedPersonalItem = new PersonalItem();
        ownedPersonalItem.setItem(new Item("1"));
        ownedPersonalItem.setIsOwned(true);
        ownedPersonalItem.setTradeOperationType(TradeOperationType.BUY);
        PotentialTradeStats potentialTradeStats1 = new PotentialTradeStats(1, 2, 3L);
        when(potentialTradeStatsService.getPotentialBuyTradesStatsOfItem(ownedPersonalItem.getItem())).thenReturn(List.of(potentialTradeStats1));

        PersonalItem wrongOperationTypePersonalItem = new PersonalItem();
        wrongOperationTypePersonalItem.setItem(new Item("2"));
        wrongOperationTypePersonalItem.setIsOwned(false);
        wrongOperationTypePersonalItem.setTradeOperationType(TradeOperationType.SELL);
        PotentialTradeStats potentialTradeStats2 = new PotentialTradeStats(4, 5, 6L);
        when(potentialTradeStatsService.getPotentialBuyTradesStatsOfItem(wrongOperationTypePersonalItem.getItem())).thenReturn(List.of(potentialTradeStats2));

        PersonalItem alreadyExistingTradeWithWrongCategoryPersonalItem = new PersonalItem();
        alreadyExistingTradeWithWrongCategoryPersonalItem.setItem(new Item("3"));
        alreadyExistingTradeWithWrongCategoryPersonalItem.setIsOwned(false);
        alreadyExistingTradeWithWrongCategoryPersonalItem.setTradeOperationType(TradeOperationType.BUY_AND_SELL);
        Trade ubiTrade1 = new Trade();
        ubiTrade1.setCategory(TradeCategory.Sell);
        ubiTrade1.setProposedPaymentPrice(30);
        ubiTrade1.setMinutesToTrade(20);
        ubiTrade1.setTradePriority(100L);
        alreadyExistingTradeWithWrongCategoryPersonalItem.setTradeAlreadyExists(true);
        alreadyExistingTradeWithWrongCategoryPersonalItem.setExistingTrade(ubiTrade1);
        alreadyExistingTradeWithWrongCategoryPersonalItem.setBuyBoundaryPrice(25);
        PotentialTradeStats potentialTradeStats3 = new PotentialTradeStats(25, 8, 9L);
        PotentialTradeStats potentialTradeStats4 = new PotentialTradeStats(24, 11, 12L);
        PotentialTradeStats potentialTradeStatsBoundaryPrice7 = new PotentialTradeStats(26, 20, 21L);
        PotentialTradeStats ubiTradeStatsWrongCategory = new PotentialTradeStats(30, 20, 100L);
        when(potentialTradeStatsService.getPotentialBuyTradesStatsOfItem(alreadyExistingTradeWithWrongCategoryPersonalItem.getItem())).thenReturn(List.of(potentialTradeStats3, potentialTradeStats4, potentialTradeStatsBoundaryPrice7));

        PersonalItem alreadyExistingTradeWithProperCategoryPersonalItem = new PersonalItem();
        alreadyExistingTradeWithProperCategoryPersonalItem.setItem(new Item("4"));
        alreadyExistingTradeWithProperCategoryPersonalItem.setIsOwned(false);
        alreadyExistingTradeWithProperCategoryPersonalItem.setTradeOperationType(TradeOperationType.BUY);
        Trade ubiTrade2 = new Trade();
        ubiTrade2.setCategory(TradeCategory.Buy);
        ubiTrade2.setProposedPaymentPrice(30);
        ubiTrade2.setMinutesToTrade(20);
        ubiTrade2.setTradePriority(100L);
        alreadyExistingTradeWithProperCategoryPersonalItem.setTradeAlreadyExists(true);
        alreadyExistingTradeWithProperCategoryPersonalItem.setExistingTrade(ubiTrade2);
        alreadyExistingTradeWithProperCategoryPersonalItem.setBuyBoundaryPrice(25);
        alreadyExistingTradeWithProperCategoryPersonalItem.getItem().setMonthMedianPrice(30);
        alreadyExistingTradeWithProperCategoryPersonalItem.setMinMedianPriceDifference(10);
        alreadyExistingTradeWithProperCategoryPersonalItem.setMinMedianPriceDifferencePercent(46);
        PotentialTradeStats potentialTradeStats5 = new PotentialTradeStats(13, 14, 15L);
        PotentialTradeStats potentialTradeStats6 = new PotentialTradeStats(16, 17, 18L);
        PotentialTradeStats potentialTradeStatsNullPrice = new PotentialTradeStats(null, 17, 18L);
        PotentialTradeStats potentialTradeStatsNullTime = new PotentialTradeStats(16, null, 18L);
        PotentialTradeStats potentialTradeStatsNullPriority = new PotentialTradeStats(16, 17, null);
        PotentialTradeStats potentialTradeStatsMedianPriceDiffPercent9 = new PotentialTradeStats(17, 26, 27L);
        PotentialTradeStats ubiTradeStatsProperCategory = new PotentialTradeStats(30, 20, 100L);
        when(potentialTradeStatsService.getPotentialBuyTradesStatsOfItem(alreadyExistingTradeWithProperCategoryPersonalItem.getItem())).thenReturn(List.of(potentialTradeStats5, potentialTradeStats6, potentialTradeStatsMedianPriceDiffPercent9, potentialTradeStatsNullPrice, potentialTradeStatsNullTime, potentialTradeStatsNullPriority));

        PersonalItem nullPriceExistingTradePersonalItem = new PersonalItem();
        nullPriceExistingTradePersonalItem.setItem(new Item("6"));
        nullPriceExistingTradePersonalItem.setIsOwned(false);
        nullPriceExistingTradePersonalItem.setTradeOperationType(TradeOperationType.BUY);
        Trade ubiTrade3 = new Trade();
        ubiTrade3.setCategory(TradeCategory.Buy);
        ubiTrade3.setProposedPaymentPrice(null);
        ubiTrade3.setMinutesToTrade(20);
        ubiTrade3.setTradePriority(100L);
        nullPriceExistingTradePersonalItem.setTradeAlreadyExists(true);
        nullPriceExistingTradePersonalItem.setExistingTrade(ubiTrade3);
        nullPriceExistingTradePersonalItem.setBuyBoundaryPrice(25);
        nullPriceExistingTradePersonalItem.getItem().setMonthMedianPrice(30);
        nullPriceExistingTradePersonalItem.setMinMedianPriceDifference(10);
        nullPriceExistingTradePersonalItem.setMinMedianPriceDifferencePercent(46);
        PotentialTradeStats ubiTradeStatsNullPrice = new PotentialTradeStats(null, 20, 100L);
        when(potentialTradeStatsService.getPotentialBuyTradesStatsOfItem(nullPriceExistingTradePersonalItem.getItem())).thenReturn(List.of());

        PersonalItem nullTimeExistingTradePersonalItem = new PersonalItem();
        nullTimeExistingTradePersonalItem.setItem(new Item("7"));
        nullTimeExistingTradePersonalItem.setIsOwned(false);
        nullTimeExistingTradePersonalItem.setTradeOperationType(TradeOperationType.BUY);
        Trade ubiTrade4 = new Trade();
        ubiTrade4.setCategory(TradeCategory.Buy);
        ubiTrade4.setProposedPaymentPrice(10);
        ubiTrade4.setMinutesToTrade(null);
        ubiTrade4.setTradePriority(100L);
        nullTimeExistingTradePersonalItem.setTradeAlreadyExists(true);
        nullTimeExistingTradePersonalItem.setExistingTrade(ubiTrade4);
        nullTimeExistingTradePersonalItem.setBuyBoundaryPrice(25);
        nullTimeExistingTradePersonalItem.getItem().setMonthMedianPrice(30);
        nullTimeExistingTradePersonalItem.setMinMedianPriceDifference(10);
        nullTimeExistingTradePersonalItem.setMinMedianPriceDifferencePercent(46);
        PotentialTradeStats ubiTradeStatsNullTime = new PotentialTradeStats(10, null, 100L);
        when(potentialTradeStatsService.getPotentialBuyTradesStatsOfItem(nullTimeExistingTradePersonalItem.getItem())).thenReturn(List.of());

        PersonalItem nullPriorityExistingTradePersonalItem = new PersonalItem();
        nullPriorityExistingTradePersonalItem.setItem(new Item("8"));
        nullPriorityExistingTradePersonalItem.setIsOwned(false);
        nullPriorityExistingTradePersonalItem.setTradeOperationType(TradeOperationType.BUY);
        Trade ubiTrade5 = new Trade();
        ubiTrade5.setCategory(TradeCategory.Buy);
        ubiTrade5.setProposedPaymentPrice(10);
        ubiTrade5.setMinutesToTrade(20);
        ubiTrade5.setTradePriority(null);
        nullPriorityExistingTradePersonalItem.setTradeAlreadyExists(true);
        nullPriorityExistingTradePersonalItem.setExistingTrade(ubiTrade5);
        nullPriorityExistingTradePersonalItem.setBuyBoundaryPrice(25);
        nullPriorityExistingTradePersonalItem.getItem().setMonthMedianPrice(30);
        nullPriorityExistingTradePersonalItem.setMinMedianPriceDifference(10);
        nullPriorityExistingTradePersonalItem.setMinMedianPriceDifferencePercent(46);
        PotentialTradeStats ubiTradeStatsNullPriority = new PotentialTradeStats(10, 20, null);
        when(potentialTradeStatsService.getPotentialBuyTradesStatsOfItem(nullPriorityExistingTradePersonalItem.getItem())).thenReturn(List.of());


        PersonalItem noAlreadyExistingTradePersonalItem = new PersonalItem();
        noAlreadyExistingTradePersonalItem.setItem(new Item("5"));
        noAlreadyExistingTradePersonalItem.setIsOwned(false);
        noAlreadyExistingTradePersonalItem.setTradeAlreadyExists(false);
        noAlreadyExistingTradePersonalItem.setTradeOperationType(TradeOperationType.BUY_AND_SELL);
        noAlreadyExistingTradePersonalItem.setBuyBoundaryPrice(300);
        noAlreadyExistingTradePersonalItem.getItem().setMonthMedianPrice(250);
        noAlreadyExistingTradePersonalItem.setMinMedianPriceDifference(50);
        noAlreadyExistingTradePersonalItem.setMinMedianPriceDifferencePercent(19);
        PotentialTradeStats potentialTradeStatsMedianPriceDiff8 = new PotentialTradeStats(201, 23, 24L);
        PotentialTradeStats potentialTradeStats10 = new PotentialTradeStats(200, 29, 30L);
        PotentialTradeStats potentialTradeStats11 = new PotentialTradeStats(199, 32, 33L);
        when(potentialTradeStatsService.getPotentialBuyTradesStatsOfItem(noAlreadyExistingTradePersonalItem.getItem())).thenReturn(List.of(potentialTradeStatsMedianPriceDiff8, potentialTradeStats10, potentialTradeStats11));

        List<PotentialPersonalBuyTrade> result = potentialTradeFactory.getFilteredPotentialBuyTradesForUser(List.of(ownedPersonalItem, wrongOperationTypePersonalItem, alreadyExistingTradeWithWrongCategoryPersonalItem, alreadyExistingTradeWithProperCategoryPersonalItem, nullPriceExistingTradePersonalItem, nullTimeExistingTradePersonalItem, nullPriorityExistingTradePersonalItem, noAlreadyExistingTradePersonalItem));

        verify(potentialTradeStatsService, times(0)).getPotentialBuyTradesStatsOfItem(ownedPersonalItem.getItem());
        verify(potentialTradeStatsService, times(0)).getPotentialBuyTradesStatsOfItem(wrongOperationTypePersonalItem.getItem());

        assertTrue(result.contains(new PotentialPersonalBuyTrade(alreadyExistingTradeWithWrongCategoryPersonalItem, potentialTradeStats3)));
        assertTrue(result.contains(new PotentialPersonalBuyTrade(alreadyExistingTradeWithWrongCategoryPersonalItem, potentialTradeStats4)));

        assertTrue(result.contains(new PotentialPersonalBuyTrade(alreadyExistingTradeWithProperCategoryPersonalItem, potentialTradeStats5)));
        assertTrue(result.contains(new PotentialPersonalBuyTrade(alreadyExistingTradeWithProperCategoryPersonalItem, potentialTradeStats6)));
        assertTrue(result.contains(new PotentialPersonalBuyTrade(alreadyExistingTradeWithProperCategoryPersonalItem, ubiTradeStatsProperCategory)));

        assertTrue(result.contains(new PotentialPersonalBuyTrade(noAlreadyExistingTradePersonalItem, potentialTradeStats10)));
        assertTrue(result.contains(new PotentialPersonalBuyTrade(noAlreadyExistingTradePersonalItem, potentialTradeStats11)));

        assertEquals(7, result.size());
    }
}