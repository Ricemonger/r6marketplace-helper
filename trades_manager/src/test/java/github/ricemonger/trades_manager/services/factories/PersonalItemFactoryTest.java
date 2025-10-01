package github.ricemonger.trades_manager.services.factories;

import github.ricemonger.trades_manager.services.DTOs.PersonalItem;
import github.ricemonger.trades_manager.services.DTOs.Trade;
import github.ricemonger.trades_manager.services.DTOs.TradeByFiltersManager;
import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.DTOs.personal.ItemFilter;
import github.ricemonger.utils.DTOs.personal.TradeByItemIdManager;
import github.ricemonger.utils.enums.TradeOperationType;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@SpringBootTest
class PersonalItemFactoryTest {
    @SpyBean
    private PersonalItemFactory personalItemFactory;

    @Test
    public void getPersonalItemsForUser_should_return_sets_from_byFilters_and_itemId_managers_with_higher_priority_for_itemId_managers() {
        Collection<TradeByFiltersManager> tradeByFiltersManagers = mock(Collection.class);
        Collection<TradeByItemIdManager> tradeByItemIdManagers = mock(Collection.class);
        Collection<Trade> existingSellTrades = mock(Collection.class);
        Collection<Trade> existingBuyTrades = mock(Collection.class);
        Collection<String> ownedItemsId = mock(Collection.class);
        Collection<Item> existingItems = mock(Collection.class);

        PersonalItem personalItem1Sell = new PersonalItem();
        personalItem1Sell.setItem(new Item("1"));
        personalItem1Sell.setTradeOperationType(TradeOperationType.SELL);
        personalItem1Sell.setPriorityMultiplier(2);

        PersonalItem personalItem2Sell = new PersonalItem();
        personalItem2Sell.setItem(new Item("2"));
        personalItem2Sell.setTradeOperationType(TradeOperationType.SELL);
        personalItem2Sell.setPriorityMultiplier(2);

        PersonalItem personalItem1SellLowerPriority = new PersonalItem();
        personalItem1SellLowerPriority.setItem(new Item("1"));
        personalItem1SellLowerPriority.setTradeOperationType(TradeOperationType.SELL);
        personalItem1SellLowerPriority.setPriorityMultiplier(1);

        PersonalItem personalItem2SellLowerPriority = new PersonalItem();
        personalItem2SellLowerPriority.setItem(new Item("2"));
        personalItem2SellLowerPriority.setTradeOperationType(TradeOperationType.SELL);
        personalItem2SellLowerPriority.setPriorityMultiplier(2);

        PersonalItem personalItem1Buy = new PersonalItem();
        personalItem1Buy.setItem(new Item("1"));
        personalItem1Buy.setTradeOperationType(TradeOperationType.BUY);
        personalItem1Buy.setPriorityMultiplier(2);

        PersonalItem personalItem2Buy = new PersonalItem();
        personalItem2Buy.setItem(new Item("2"));
        personalItem2Buy.setTradeOperationType(TradeOperationType.BUY);
        personalItem2Buy.setPriorityMultiplier(2);

        PersonalItem personalItem3Sell = new PersonalItem();
        personalItem3Sell.setItem(new Item("3"));
        personalItem3Sell.setTradeOperationType(TradeOperationType.SELL);
        personalItem3Sell.setPriorityMultiplier(2);

        PersonalItem personalItem4Sell = new PersonalItem();
        personalItem4Sell.setItem(new Item("4"));
        personalItem4Sell.setTradeOperationType(TradeOperationType.SELL);
        personalItem4Sell.setPriorityMultiplier(2);

        Set<PersonalItem> byFiltersSet = Set.of(personalItem1Sell, personalItem2Sell, personalItem1Buy, personalItem2Buy);

        Set<PersonalItem> byItemIdSet = Set.of(personalItem1SellLowerPriority, personalItem2SellLowerPriority, personalItem3Sell, personalItem4Sell);

        doReturn(byFiltersSet).when(personalItemFactory).getPersonalItemsFromTradeByFiltersManagersByPriority(same(tradeByFiltersManagers),
                same(existingSellTrades), same(existingBuyTrades), same(ownedItemsId), same(existingItems));
        doReturn(byItemIdSet).when(personalItemFactory).getPersonalItemsFromTradeByItemIdManagersByPriority(same(tradeByItemIdManagers),
                same(existingSellTrades), same(existingBuyTrades), same(ownedItemsId), same(existingItems));

        Set<PersonalItem> result = personalItemFactory.getPersonalItemsForUser(tradeByFiltersManagers, tradeByItemIdManagers, existingSellTrades, existingBuyTrades, ownedItemsId, existingItems);

        for (PersonalItem personalItem : result) {
            System.out.println(personalItem);
        }

        assertEquals(6, result.size());
        assertTrue(result.stream().filter(p -> p.equals(personalItem1SellLowerPriority)).anyMatch(p -> p.isFullyEqual(personalItem1SellLowerPriority)));
        assertTrue(result.stream().filter(p -> p.equals(personalItem2SellLowerPriority)).anyMatch(p -> p.isFullyEqual(personalItem2SellLowerPriority)));
        assertTrue(result.stream().filter(p -> p.equals(personalItem3Sell)).anyMatch(p -> p.isFullyEqual(personalItem3Sell)));
        assertTrue(result.stream().filter(p -> p.equals(personalItem4Sell)).anyMatch(p -> p.isFullyEqual(personalItem4Sell)));
        assertTrue(result.stream().filter(p -> p.equals(personalItem1Buy)).anyMatch(p -> p.isFullyEqual(personalItem1Buy)));
        assertTrue(result.stream().filter(p -> p.equals(personalItem2Buy)).anyMatch(p -> p.isFullyEqual(personalItem2Buy)));
    }

    @Test
    public void getPersonalItemsFromTradeByFiltersManagersByPriority_should_return_empty_list_for_empty_or_null_appliedFilters_or_existingItems() {
        Collection<Trade> existingSellTrades = mock(Collection.class);
        Collection<Trade> existingBuyTrades = mock(Collection.class);
        Collection<String> ownedItemsId = mock(Collection.class);
        Collection<Item> existingItems = mock(Collection.class);
        when(existingItems.isEmpty()).thenReturn(false);

        TradeByFiltersManager tradeByFiltersManager = mock(TradeByFiltersManager.class);

        assertTrue(personalItemFactory.getPersonalItemsFromTradeByFiltersManagersByPriority(List.of(), existingSellTrades, existingBuyTrades, ownedItemsId, existingItems).isEmpty());
        assertTrue(personalItemFactory.getPersonalItemsFromTradeByFiltersManagersByPriority(null, existingSellTrades, existingBuyTrades, ownedItemsId, existingItems).isEmpty());
        assertTrue(personalItemFactory.getPersonalItemsFromTradeByFiltersManagersByPriority(List.of(tradeByFiltersManager), existingSellTrades, existingBuyTrades, ownedItemsId, null).isEmpty());
        when(existingItems.isEmpty()).thenReturn(true);
        assertTrue(personalItemFactory.getPersonalItemsFromTradeByFiltersManagersByPriority(List.of(tradeByFiltersManager), existingSellTrades, existingBuyTrades, ownedItemsId, existingItems).isEmpty());
    }

    @Test
    public void getPersonalItemsFromTradeByFiltersManagersByPriority_should_create_set_of_non_null_personal_items_from_tradeByFiltersManagers_skip_lower_priority_dupes() {
        try (MockedStatic<ItemFilter> mockedStaticFilter = Mockito.mockStatic(ItemFilter.class)) {
            Collection<Trade> existingSellTrades = mock(Collection.class);
            Collection<Trade> existingBuyTrades = mock(Collection.class);
            Collection<String> ownedItemsId = mock(Collection.class);
            Collection<Item> existingItems = mock(Collection.class);
            when(existingItems.isEmpty()).thenReturn(false);

            TradeByFiltersManager tradeByFiltersManagerItemSet1 = mock(TradeByFiltersManager.class);
            TradeByFiltersManager tradeByFiltersManagerItemSet1LowerPriority = mock(TradeByFiltersManager.class);
            TradeByFiltersManager tradeByFiltersManagerItemSet1OtherOperation = mock(TradeByFiltersManager.class);
            TradeByFiltersManager tradeByFiltersManagerItemSet2 = mock(TradeByFiltersManager.class);

            List itemSet1Filters = mock(List.class);
            List itemSet2Filters = mock(List.class);

            when(tradeByFiltersManagerItemSet1.getAppliedFilters()).thenReturn(itemSet1Filters);
            when(tradeByFiltersManagerItemSet1LowerPriority.getAppliedFilters()).thenReturn(itemSet1Filters);
            when(tradeByFiltersManagerItemSet1OtherOperation.getAppliedFilters()).thenReturn(itemSet1Filters);
            when(tradeByFiltersManagerItemSet2.getAppliedFilters()).thenReturn(itemSet2Filters);

            when(tradeByFiltersManagerItemSet1LowerPriority.getPriorityMultiplier()).thenReturn(1);
            when(tradeByFiltersManagerItemSet1OtherOperation.getPriorityMultiplier()).thenReturn(5);
            when(tradeByFiltersManagerItemSet1.getPriorityMultiplier()).thenReturn(2);
            when(tradeByFiltersManagerItemSet2.getPriorityMultiplier()).thenReturn(2);

            mockedStaticFilter.when(() -> ItemFilter.filterItems(Mockito.any(), same(itemSet1Filters))).thenReturn(List.of(new Item("1"),
                    new Item("2")));
            mockedStaticFilter.when(() -> ItemFilter.filterItems(Mockito.any(), same(itemSet2Filters))).thenReturn(List.of(new Item("3"),
                    new Item("4")));

            PersonalItem personalItem1Sell = new PersonalItem();
            personalItem1Sell.setItem(new Item("1"));
            personalItem1Sell.setTradeOperationType(TradeOperationType.SELL);
            personalItem1Sell.setPriorityMultiplier(2);

            PersonalItem personalItem2Sell = new PersonalItem();
            personalItem2Sell.setItem(new Item("2"));
            personalItem2Sell.setTradeOperationType(TradeOperationType.SELL);
            personalItem2Sell.setPriorityMultiplier(2);

            doReturn(Set.of(personalItem1Sell, personalItem2Sell)).when(personalItemFactory).getPersonalItemsFromTradeByFiltersManager(same(tradeByFiltersManagerItemSet1), same(existingSellTrades), same(existingBuyTrades), same(ownedItemsId), same(existingItems));

            PersonalItem personalItem1SellLowerPriority = new PersonalItem();
            personalItem1SellLowerPriority.setItem(new Item("1"));
            personalItem1SellLowerPriority.setTradeOperationType(TradeOperationType.SELL);
            personalItem1SellLowerPriority.setPriorityMultiplier(1);

            PersonalItem personalItem2SellLowerPriority = new PersonalItem();
            personalItem2SellLowerPriority.setItem(new Item("2"));
            personalItem2SellLowerPriority.setTradeOperationType(TradeOperationType.SELL);
            personalItem2SellLowerPriority.setPriorityMultiplier(1);

            doReturn(Set.of(personalItem1SellLowerPriority, personalItem2SellLowerPriority)).when(personalItemFactory).getPersonalItemsFromTradeByFiltersManager(same(tradeByFiltersManagerItemSet1LowerPriority), same(existingSellTrades), same(existingBuyTrades), same(ownedItemsId), same(existingItems));

            PersonalItem personalItem1Buy = new PersonalItem();
            personalItem1Buy.setItem(new Item("1"));
            personalItem1Buy.setTradeOperationType(TradeOperationType.BUY);
            personalItem1Buy.setPriorityMultiplier(2);

            PersonalItem personalItem2Buy = new PersonalItem();
            personalItem2Buy.setItem(new Item("2"));
            personalItem2Buy.setTradeOperationType(TradeOperationType.BUY);
            personalItem2Buy.setPriorityMultiplier(2);

            doReturn(Set.of(personalItem1Buy, personalItem2Buy)).when(personalItemFactory).getPersonalItemsFromTradeByFiltersManager(same(tradeByFiltersManagerItemSet1OtherOperation), same(existingSellTrades), same(existingBuyTrades), same(ownedItemsId), same(existingItems));

            PersonalItem personalItem3Sell = new PersonalItem();
            personalItem3Sell.setItem(new Item("3"));
            personalItem3Sell.setTradeOperationType(TradeOperationType.SELL);
            personalItem3Sell.setPriorityMultiplier(2);

            PersonalItem personalItem4Sell = new PersonalItem();
            personalItem4Sell.setItem(new Item("4"));
            personalItem4Sell.setTradeOperationType(TradeOperationType.SELL);
            personalItem4Sell.setPriorityMultiplier(2);

            doReturn(Set.of(personalItem3Sell, personalItem4Sell)).when(personalItemFactory).getPersonalItemsFromTradeByFiltersManager(same(tradeByFiltersManagerItemSet2), same(existingSellTrades),
                    same(existingBuyTrades), same(ownedItemsId), same(existingItems));

            Set<PersonalItem> result = personalItemFactory.getPersonalItemsFromTradeByFiltersManagersByPriority(
                    Set.of(tradeByFiltersManagerItemSet1, tradeByFiltersManagerItemSet1LowerPriority, tradeByFiltersManagerItemSet1OtherOperation, tradeByFiltersManagerItemSet2),
                    existingSellTrades,
                    existingBuyTrades,
                    ownedItemsId,
                    existingItems);

            for (PersonalItem personalItem : result) {
                System.out.println(personalItem);
            }

            assertEquals(6, result.size());
            assertTrue(result.stream().filter(p -> p.equals(personalItem1Sell)).findFirst().get().isFullyEqual(personalItem1Sell));
            assertTrue(result.stream().filter(p -> p.equals(personalItem2Sell)).findFirst().get().isFullyEqual(personalItem2Sell));
            assertTrue(result.stream().filter(p -> p.equals(personalItem3Sell)).findFirst().get().isFullyEqual(personalItem3Sell));
            assertTrue(result.stream().filter(p -> p.equals(personalItem4Sell)).findFirst().get().isFullyEqual(personalItem4Sell));
            assertTrue(result.stream().filter(p -> p.equals(personalItem1Buy)).findFirst().get().isFullyEqual(personalItem1Buy));
            assertTrue(result.stream().filter(p -> p.equals(personalItem2Buy)).findFirst().get().isFullyEqual(personalItem2Buy));

        }
    }

    @Test
    public void getPersonalItemsFromTradeByFiltersManager_should_return_empty_list_for_empty_or_null_appliedFilters_or_existingItems_or_disabled() {
        try (MockedStatic<ItemFilter> mockedStatic = Mockito.mockStatic(ItemFilter.class)) {

            List<Item> items = List.of(new Item("1"), new Item("2"));
            mockedStatic.when(() -> ItemFilter.filterItems(Mockito.any(), Mockito.any())).thenReturn(items);

            Collection<Trade> existingSellTrades = mock(Collection.class);
            Collection<Trade> existingBuyTrades = mock(Collection.class);
            Collection<String> ownedItemsId = mock(Collection.class);
            Collection<Item> existingItems = mock(Collection.class);
            when(existingItems.isEmpty()).thenReturn(false);

            TradeByFiltersManager tradeByFiltersManager = mock(TradeByFiltersManager.class);
            when(tradeByFiltersManager.getAppliedFilters()).thenReturn(List.of(new ItemFilter()));
            when(tradeByFiltersManager.getEnabled()).thenReturn(true);

            assertFalse(personalItemFactory.getPersonalItemsFromTradeByFiltersManager(tradeByFiltersManager, existingSellTrades, existingBuyTrades, ownedItemsId, existingItems).isEmpty());

            when(tradeByFiltersManager.getAppliedFilters()).thenReturn(new ArrayList<>());
            assertTrue(personalItemFactory.getPersonalItemsFromTradeByFiltersManager(tradeByFiltersManager, existingSellTrades, existingBuyTrades, ownedItemsId, existingItems).isEmpty());
            when(tradeByFiltersManager.getAppliedFilters()).thenReturn(null);
            assertTrue(personalItemFactory.getPersonalItemsFromTradeByFiltersManager(tradeByFiltersManager, existingSellTrades, existingBuyTrades, ownedItemsId, existingItems).isEmpty());
            when(tradeByFiltersManager.getAppliedFilters()).thenReturn(List.of(new ItemFilter()));
            assertTrue(personalItemFactory.getPersonalItemsFromTradeByFiltersManager(tradeByFiltersManager, existingSellTrades, existingBuyTrades, ownedItemsId, null).isEmpty());
            when(existingItems.isEmpty()).thenReturn(true);
            assertTrue(personalItemFactory.getPersonalItemsFromTradeByFiltersManager(tradeByFiltersManager, existingSellTrades, existingBuyTrades, ownedItemsId, existingItems).isEmpty());
            when(existingItems.isEmpty()).thenReturn(false);
            when(tradeByFiltersManager.getEnabled()).thenReturn(false);
            assertTrue(personalItemFactory.getPersonalItemsFromTradeByFiltersManager(tradeByFiltersManager, existingSellTrades, existingBuyTrades, ownedItemsId, existingItems).isEmpty());
            when(tradeByFiltersManager.getEnabled()).thenReturn(null);
            assertTrue(personalItemFactory.getPersonalItemsFromTradeByFiltersManager(tradeByFiltersManager, existingSellTrades, existingBuyTrades, ownedItemsId, existingItems).isEmpty());
        }
    }

    @Test
    public void getPersonalItemsFromTradeByFiltersManager_should_return_personalItems_by_filters_manager_trades_dont_exist() {
        try (
                MockedStatic<ItemFilter> mockedStaticFilter = Mockito.mockStatic(ItemFilter.class)) {
            mockedStaticFilter.when(() -> ItemFilter.filterItems(Mockito.any(), Mockito.any())).thenReturn(List.of(new Item("1"), new Item("2")));

            TradeByFiltersManager manager = new TradeByFiltersManager();
            manager.setAppliedFilters(List.of(new ItemFilter()));
            manager.setMinDifferenceFromMedianPrice(100);
            manager.setMinDifferenceFromMedianPricePercent(200);
            manager.setTradeOperationType(TradeOperationType.SELL);
            manager.setPriorityMultiplier(3);
            manager.setEnabled(true);

            Trade sellTrade = new Trade();
            sellTrade.setTradeId("sellTradeId1");
            sellTrade.setItem(new Item("2"));
            Collection<Trade> existingSellTrades = Set.of(sellTrade);

            Trade buyTrade = new Trade();
            buyTrade.setTradeId("buyTradeId2");
            buyTrade.setItem(new Item("1"));
            Collection<Trade> existingBuyTrades = Set.of(buyTrade);

            Collection<String> ownedItemsId = Set.of("1", "3");

            Collection<Item> existingItems = Set.of(new Item("1"), new Item("2"), new Item("3"));

            PersonalItem personalItem1 = new PersonalItem();
            personalItem1.setItem(new Item("1"));
            personalItem1.setSellBoundaryPrice(Integer.MIN_VALUE);
            personalItem1.setBuyBoundaryPrice(Integer.MAX_VALUE);
            personalItem1.setMinMedianPriceDifference(100);
            personalItem1.setMinMedianPriceDifferencePercent(200);
            personalItem1.setTradeOperationType(TradeOperationType.SELL);
            personalItem1.setPriorityMultiplier(3);
            personalItem1.setIsOwned(true);
            personalItem1.setTradeAlreadyExists(false);
            personalItem1.setExistingTrade(null);

            PersonalItem personalItem2 = new PersonalItem();
            personalItem2.setItem(new Item("2"));
            personalItem2.setSellBoundaryPrice(Integer.MIN_VALUE);
            personalItem2.setBuyBoundaryPrice(Integer.MAX_VALUE);
            personalItem2.setMinMedianPriceDifference(100);
            personalItem2.setMinMedianPriceDifferencePercent(200);
            personalItem2.setTradeOperationType(TradeOperationType.SELL);
            personalItem2.setPriorityMultiplier(3);
            personalItem2.setIsOwned(false);
            personalItem2.setTradeAlreadyExists(false);
            personalItem2.setExistingTrade(null);


            Set<PersonalItem> result = personalItemFactory.getPersonalItemsFromTradeByFiltersManager(manager, existingSellTrades, existingBuyTrades, ownedItemsId, existingItems);

            assertEquals(2, result.size());
            assertTrue(result.stream().filter(p -> p.equals(personalItem1)).findFirst().get().isFullyEqual(personalItem1));
            assertTrue(result.stream().filter(p -> p.equals(personalItem2)).findFirst().get().isFullyEqual(personalItem2));
        }
    }

    @Test
    public void getPersonalItemsFromTradeByFiltersManager_should_return_personalItems_by_filters_manager_trades_exist() {
        try (
                MockedStatic<ItemFilter> mockedStaticFilter = Mockito.mockStatic(ItemFilter.class)) {
            mockedStaticFilter.when(() -> ItemFilter.filterItems(Mockito.any(), Mockito.any())).thenReturn(List.of(new Item("1"), new Item("2")));

            TradeByFiltersManager manager = new TradeByFiltersManager();
            manager.setAppliedFilters(List.of(new ItemFilter()));
            manager.setMinDifferenceFromMedianPrice(100);
            manager.setMinDifferenceFromMedianPricePercent(200);
            manager.setTradeOperationType(TradeOperationType.SELL);
            manager.setPriorityMultiplier(3);
            manager.setEnabled(true);

            Trade sellTrade = new Trade();
            sellTrade.setTradeId("sellTradeId1");
            sellTrade.setItem(new Item("1"));
            Collection<Trade> existingSellTrades = Set.of(sellTrade);

            Trade buyTrade = new Trade();
            buyTrade.setTradeId("buyTradeId2");
            buyTrade.setItem(new Item("2"));
            Collection<Trade> existingBuyTrades = Set.of(buyTrade);

            Collection<String> ownedItemsId = Set.of("1", "3");

            Collection<Item> existingItems = Set.of(new Item("1"), new Item("2"), new Item("3"));

            PersonalItem personalItem1 = new PersonalItem();
            personalItem1.setItem(new Item("1"));
            personalItem1.setSellBoundaryPrice(Integer.MIN_VALUE);
            personalItem1.setBuyBoundaryPrice(Integer.MAX_VALUE);
            personalItem1.setMinMedianPriceDifference(100);
            personalItem1.setMinMedianPriceDifferencePercent(200);
            personalItem1.setTradeOperationType(TradeOperationType.SELL);
            personalItem1.setPriorityMultiplier(3);
            personalItem1.setIsOwned(true);
            personalItem1.setTradeAlreadyExists(true);
            personalItem1.setExistingTrade(sellTrade);

            PersonalItem personalItem2 = new PersonalItem();
            personalItem2.setItem(new Item("2"));
            personalItem2.setSellBoundaryPrice(Integer.MIN_VALUE);
            personalItem2.setBuyBoundaryPrice(Integer.MAX_VALUE);
            personalItem2.setMinMedianPriceDifference(100);
            personalItem2.setMinMedianPriceDifferencePercent(200);
            personalItem2.setTradeOperationType(TradeOperationType.SELL);
            personalItem2.setPriorityMultiplier(3);
            personalItem2.setIsOwned(false);
            personalItem2.setTradeAlreadyExists(true);
            personalItem2.setExistingTrade(buyTrade);


            Set<PersonalItem> result = personalItemFactory.getPersonalItemsFromTradeByFiltersManager(manager, existingSellTrades, existingBuyTrades, ownedItemsId, existingItems);

            assertEquals(2, result.size());
            assertTrue(result.stream().filter(p -> p.equals(personalItem1)).findFirst().get().isFullyEqual(personalItem1));
            assertTrue(result.stream().filter(p -> p.equals(personalItem2)).findFirst().get().isFullyEqual(personalItem2));
        }
    }

    @Test
    public void getPersonalItemsFromTradeByItemIdManagersByPriority_should_return_empty_list_if_managers_or_existing_items_is_null_or_empty() {
        TradeByItemIdManager tradeByItemIdManagerItem1 = mock(TradeByItemIdManager.class);

        Collection<Trade> existingSellTrades = mock(Collection.class);
        Collection<Trade> existingBuyTrades = mock(Collection.class);
        Collection<String> ownedItemsId = mock(Collection.class);
        Collection<Item> existingItems = mock(Collection.class);
        when(existingItems.isEmpty()).thenReturn(false);

        assertTrue(personalItemFactory.getPersonalItemsFromTradeByItemIdManagersByPriority(null, existingSellTrades, existingBuyTrades, ownedItemsId, existingItems).isEmpty());
        assertTrue(personalItemFactory.getPersonalItemsFromTradeByItemIdManagersByPriority(Set.of(), existingSellTrades, existingBuyTrades, ownedItemsId, existingItems).isEmpty());

        assertTrue(personalItemFactory.getPersonalItemsFromTradeByItemIdManagersByPriority(Set.of(tradeByItemIdManagerItem1), existingSellTrades, existingBuyTrades, ownedItemsId, null).isEmpty());
        when(existingItems.isEmpty()).thenReturn(false);
        assertTrue(personalItemFactory.getPersonalItemsFromTradeByItemIdManagersByPriority(Set.of(tradeByItemIdManagerItem1), existingSellTrades, existingBuyTrades, ownedItemsId, existingItems).isEmpty());
    }

    @Test
    public void getPersonalItemsFromTradeByItemIdManagersByPriority_should_create_set_of_non_null_personal_items_from_trade_by_item_id_managers_skip_lower_priority_dupes() {
        TradeByItemIdManager tradeByItemIdManagerItem1 = mock(TradeByItemIdManager.class);
        TradeByItemIdManager tradeByItemIdManagerItem1OtherOperation = mock(TradeByItemIdManager.class);
        TradeByItemIdManager tradeByItemIdManagerItem1LowerPriority = mock(TradeByItemIdManager.class);
        TradeByItemIdManager tradeByItemIdManagerItem2 = mock(TradeByItemIdManager.class);
        TradeByItemIdManager tradeByItemIdManagerNull = mock(TradeByItemIdManager.class);

        Collection<Trade> existingSellTrades = mock(Collection.class);
        Collection<Trade> existingBuyTrades = mock(Collection.class);
        Collection<String> ownedItemsId = mock(Collection.class);
        Collection<Item> existingItems = mock(Collection.class);
        when(existingItems.isEmpty()).thenReturn(false);

        when(tradeByItemIdManagerItem1LowerPriority.getPriorityMultiplier()).thenReturn(1);
        when(tradeByItemIdManagerItem1OtherOperation.getPriorityMultiplier()).thenReturn(5);
        when(tradeByItemIdManagerItem1.getPriorityMultiplier()).thenReturn(2);
        when(tradeByItemIdManagerItem2.getPriorityMultiplier()).thenReturn(2);
        when(tradeByItemIdManagerNull.getPriorityMultiplier()).thenReturn(3);

        PersonalItem personalItem1 = new PersonalItem();
        personalItem1.setItem(new Item("1"));
        personalItem1.setTradeOperationType(TradeOperationType.SELL);
        personalItem1.setPriorityMultiplier(2);

        PersonalItem personalItem1OtherOperation = new PersonalItem();
        personalItem1OtherOperation.setItem(new Item("1"));
        personalItem1OtherOperation.setTradeOperationType(TradeOperationType.BUY);

        PersonalItem personalItem1LowerPriority = new PersonalItem();
        personalItem1LowerPriority.setItem(new Item("1"));
        personalItem1LowerPriority.setTradeOperationType(TradeOperationType.SELL);
        personalItem1LowerPriority.setPriorityMultiplier(1);

        PersonalItem personalItem2 = new PersonalItem();
        personalItem2.setItem(new Item("2"));
        personalItem2.setTradeOperationType(TradeOperationType.SELL);

        doReturn(personalItem1).when(personalItemFactory).getPersonalItemFromTradeByItemIdManagerOrNull(tradeByItemIdManagerItem1, existingSellTrades,
                existingBuyTrades,
                ownedItemsId, existingItems);
        doReturn(personalItem1OtherOperation).when(personalItemFactory).getPersonalItemFromTradeByItemIdManagerOrNull(tradeByItemIdManagerItem1OtherOperation, existingSellTrades,
                existingBuyTrades, ownedItemsId, existingItems);
        doReturn(personalItem1LowerPriority).when(personalItemFactory).getPersonalItemFromTradeByItemIdManagerOrNull(tradeByItemIdManagerItem1LowerPriority, existingSellTrades,
                existingBuyTrades, ownedItemsId, existingItems);
        doReturn(personalItem2).when(personalItemFactory).getPersonalItemFromTradeByItemIdManagerOrNull(tradeByItemIdManagerItem2, existingSellTrades,
                existingBuyTrades,
                ownedItemsId, existingItems);
        doReturn(null).when(personalItemFactory).getPersonalItemFromTradeByItemIdManagerOrNull(tradeByItemIdManagerNull, existingSellTrades,
                existingBuyTrades,
                ownedItemsId, existingItems);

        Set<PersonalItem> result = personalItemFactory.getPersonalItemsFromTradeByItemIdManagersByPriority(
                Set.of(tradeByItemIdManagerItem1, tradeByItemIdManagerItem1OtherOperation, tradeByItemIdManagerItem1LowerPriority, tradeByItemIdManagerItem2, tradeByItemIdManagerNull),
                existingSellTrades,
                existingBuyTrades,
                ownedItemsId,
                existingItems);

        System.out.println("Result:");

        for (PersonalItem item : result) {
            System.out.println(item);
        }

        assertTrue(result.contains(personalItem1));
        assertTrue(result.contains(personalItem1OtherOperation));
        assertTrue(result.contains(personalItem2));
        assertEquals(3, result.size());
        assertEquals(2, (int) result.stream().filter(p -> p.equals(personalItem1)).findFirst().get().getPriorityMultiplier());
    }

    @Test
    public void getPersonalItemFromTradeByItemIdManager_should_return_expected_null_if_item_doesnt_existOrNull_or_disabled_manager() {
        TradeByItemIdManager tradeByItemIdManager = getDefaultItemIdManager();
        Collection<Trade> existingSellTrades = new HashSet<>();
        Collection<Trade> existingBuyTrades = new HashSet<>();
        Collection<String> ownedItemsId = new HashSet<>();
        Collection<Item> existingItems = Set.of(new Item("1"));
        tradeByItemIdManager.setEnabled(true);

        assertNotNull(personalItemFactory.getPersonalItemFromTradeByItemIdManagerOrNull(tradeByItemIdManager, existingSellTrades, existingBuyTrades, ownedItemsId, existingItems));

        existingItems = Set.of(new Item("12"));
        assertNull(personalItemFactory.getPersonalItemFromTradeByItemIdManagerOrNull(tradeByItemIdManager, existingSellTrades, existingBuyTrades, ownedItemsId, existingItems));

        existingItems = null;
        assertNull(personalItemFactory.getPersonalItemFromTradeByItemIdManagerOrNull(tradeByItemIdManager, existingSellTrades, existingBuyTrades, ownedItemsId, existingItems));

        existingItems = Set.of(new Item("1"));
        tradeByItemIdManager.setEnabled(false);
        assertNull(personalItemFactory.getPersonalItemFromTradeByItemIdManagerOrNull(tradeByItemIdManager, existingSellTrades, existingBuyTrades, ownedItemsId, existingItems));
    }

    @Test
    public void getPersonalItemFromTradeByItemIdManager_should_return_expected_object_isOwned_false_and_buy_trade_doesnt_existOrNull() {
        TradeByItemIdManager tradeByItemIdManager = getDefaultItemIdManager();

        Trade sellTrade = new Trade();
        sellTrade.setTradeId("tradeId");
        sellTrade.setItem(new Item("1"));
        Collection<Trade> existingSellTrades = Set.of(sellTrade);

        Trade buyTrade = new Trade();
        buyTrade.setTradeId("tradeId");
        buyTrade.setItem(new Item("2"));
        Collection<Trade> existingBuyTrades = Set.of(buyTrade);

        Collection<String> ownedItemsId = new HashSet<>();

        Collection<Item> existingItems = Set.of(new Item("1"));

        PersonalItem expected = new PersonalItem();
        expected.setItem(new Item("1"));
        expected.setSellBoundaryPrice(100);
        expected.setBuyBoundaryPrice(200);
        expected.setMinMedianPriceDifference(Integer.MIN_VALUE);
        expected.setMinMedianPriceDifferencePercent(Integer.MIN_VALUE);
        expected.setTradeOperationType(TradeOperationType.SELL);
        expected.setPriorityMultiplier(3);
        expected.setIsOwned(false);
        expected.setTradeAlreadyExists(false);
        expected.setExistingTrade(null);

        assertTrue(expected.isFullyEqual(personalItemFactory.getPersonalItemFromTradeByItemIdManagerOrNull(tradeByItemIdManager, existingSellTrades, existingBuyTrades, ownedItemsId, existingItems)));
    }

    @Test
    public void getPersonalItemFromTradeByItemIdManager_should_return_expected_object_isOwned_false_and_buy_trade_existsOrNull() {
        TradeByItemIdManager tradeByItemIdManager = getDefaultItemIdManager();

        Trade sellTrade = new Trade();
        sellTrade.setTradeId("tradeId");
        sellTrade.setItem(new Item("2"));
        Collection<Trade> existingSellTrades = Set.of(sellTrade);

        Trade buyTrade = new Trade();
        buyTrade.setTradeId("tradeId");
        buyTrade.setItem(new Item("1"));
        Collection<Trade> existingBuyTrades = Set.of(buyTrade);

        Collection<String> ownedItemsId = new HashSet<>();

        Collection<Item> existingItems = Set.of(new Item("1"));

        PersonalItem expected = new PersonalItem();
        expected.setItem(new Item("1"));
        expected.setSellBoundaryPrice(100);
        expected.setBuyBoundaryPrice(200);
        expected.setMinMedianPriceDifference(Integer.MIN_VALUE);
        expected.setMinMedianPriceDifferencePercent(Integer.MIN_VALUE);
        expected.setTradeOperationType(TradeOperationType.SELL);
        expected.setPriorityMultiplier(3);
        expected.setIsOwned(false);
        expected.setTradeAlreadyExists(true);
        expected.setExistingTrade(buyTrade);

        assertTrue(expected.isFullyEqual(personalItemFactory.getPersonalItemFromTradeByItemIdManagerOrNull(tradeByItemIdManager, existingSellTrades, existingBuyTrades, ownedItemsId, existingItems)));
    }

    @Test
    public void getPersonalItemFromTradeByItemIdManager_should_return_expected_object_all_other_category_tradeOrNull() {
        TradeByItemIdManager tradeByItemIdManager = getDefaultItemIdManager();

        Trade sellTrade = new Trade();
        sellTrade.setTradeId("tradeId");
        sellTrade.setItem(new Item("2"));
        Collection<Trade> existingSellTrades = Set.of(sellTrade);

        Trade buyTrade = new Trade();
        buyTrade.setTradeId("tradeId");
        buyTrade.setItem(new Item("1"));
        Collection<Trade> existingBuyTrades = Set.of(buyTrade);

        Collection<String> ownedItemsId = new HashSet<>();
        ownedItemsId.add("1");

        Collection<Item> existingItems = Set.of(new Item("1"));

        PersonalItem expected = new PersonalItem();
        expected.setItem(new Item("1"));
        expected.setSellBoundaryPrice(100);
        expected.setBuyBoundaryPrice(200);
        expected.setMinMedianPriceDifference(Integer.MIN_VALUE);
        expected.setMinMedianPriceDifferencePercent(Integer.MIN_VALUE);
        expected.setTradeOperationType(TradeOperationType.SELL);
        expected.setPriorityMultiplier(3);
        expected.setIsOwned(true);
        expected.setTradeAlreadyExists(false);
        expected.setExistingTrade(null);

        assertTrue(expected.isFullyEqual(personalItemFactory.getPersonalItemFromTradeByItemIdManagerOrNull(tradeByItemIdManager, existingSellTrades, existingBuyTrades, ownedItemsId, existingItems)));
    }

    @Test
    public void getPersonalItemFromTradeByItemIdManager_OrNull_should_return_expected_object_all_fields() {
        TradeByItemIdManager tradeByItemIdManager = getDefaultItemIdManager();

        Trade sellTrade = new Trade();
        sellTrade.setTradeId("tradeId");
        sellTrade.setItem(new Item("1"));
        Collection<Trade> existingSellTrades = Set.of(sellTrade);

        Trade buyTrade = new Trade();
        buyTrade.setTradeId("tradeId");
        buyTrade.setItem(new Item("2"));
        Collection<Trade> existingBuyTrades = Set.of(buyTrade);

        Collection<String> ownedItemsId = new HashSet<>();
        ownedItemsId.add("1");

        Collection<Item> existingItems = Set.of(new Item("1"), new Item("2"));

        PersonalItem expected = new PersonalItem();
        expected.setItem(new Item("1"));
        expected.setSellBoundaryPrice(100);
        expected.setBuyBoundaryPrice(200);
        expected.setMinMedianPriceDifference(Integer.MIN_VALUE);
        expected.setMinMedianPriceDifferencePercent(Integer.MIN_VALUE);
        expected.setTradeOperationType(TradeOperationType.SELL);
        expected.setPriorityMultiplier(3);
        expected.setIsOwned(true);
        expected.setTradeAlreadyExists(true);
        expected.setExistingTrade(sellTrade);

        assertTrue(expected.isFullyEqual(personalItemFactory.getPersonalItemFromTradeByItemIdManagerOrNull(tradeByItemIdManager, existingSellTrades, existingBuyTrades, ownedItemsId, existingItems)));
    }

    private TradeByItemIdManager getDefaultItemIdManager() {
        TradeByItemIdManager tradeByItemIdManager = new TradeByItemIdManager();
        tradeByItemIdManager.setItemId("1");
        tradeByItemIdManager.setEnabled(true);
        tradeByItemIdManager.setTradeOperationType(TradeOperationType.SELL);
        tradeByItemIdManager.setSellBoundaryPrice(100);
        tradeByItemIdManager.setBuyBoundaryPrice(200);
        tradeByItemIdManager.setPriorityMultiplier(3);
        return tradeByItemIdManager;
    }

}