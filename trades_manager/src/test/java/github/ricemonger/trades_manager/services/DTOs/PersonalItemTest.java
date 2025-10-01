package github.ricemonger.trades_manager.services.DTOs;

import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.enums.ItemRarity;
import github.ricemonger.utils.enums.TradeOperationType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PersonalItemTest {

    @Test
    public void getItemId_should_return_itemId() {
        PersonalItem personalItem = new PersonalItem();
        personalItem.setItem(new Item("itemId"));
        assertEquals("itemId", personalItem.getItemId());
    }

    @Test
    public void getItemId_should_return_null_when_item_is_null() {
        PersonalItem personalItem = new PersonalItem();
        personalItem.setItem(null);
        assertNull(personalItem.getItemId());
    }

    @Test
    public void getName_should_return_name() {
        PersonalItem personalItem = new PersonalItem();
        Item item = new Item();
        item.setName("name");
        personalItem.setItem(item);
        assertEquals("name", personalItem.getName());
    }

    @Test
    public void getName_should_return_null_when_item_is_null() {
        PersonalItem personalItem = new PersonalItem();
        personalItem.setItem(null);
        assertNull(personalItem.getName());
    }

    @Test
    public void getRarity_should_return_rarity() {
        PersonalItem personalItem = new PersonalItem();
        Item item = new Item();
        item.setRarity(ItemRarity.UNCOMMON);
        personalItem.setItem(item);
        assertEquals(ItemRarity.UNCOMMON, personalItem.getRarity());
    }

    @Test
    public void getRarity_should_return_null_when_item_is_null() {
        PersonalItem personalItem = new PersonalItem();
        personalItem.setItem(null);
        assertNull(personalItem.getRarity());
    }

    @Test
    public void getLastSoldAt_should_return_lastSoldAt() {
        PersonalItem personalItem = new PersonalItem();
        Item item = new Item();
        item.setLastSoldAt(LocalDateTime.of(2021, 1, 1, 1, 1));
        personalItem.setItem(item);
        assertEquals(LocalDateTime.of(2021, 1, 1, 1, 1), personalItem.getLastSoldAt());
    }

    @Test
    public void getLastSoldAt_should_return_null_when_item_is_null() {
        PersonalItem personalItem = new PersonalItem();
        personalItem.setItem(null);
        assertNull(personalItem.getLastSoldAt());
    }

    @Test
    public void getMonthMedianPrice_should_return_monthMedianPrice() {
        PersonalItem personalItem = new PersonalItem();
        Item item = new Item();
        item.setMonthMedianPrice(100);
        personalItem.setItem(item);
        assertEquals(100, personalItem.getMonthMedianPrice());
    }

    @Test
    public void getMonthMedianPrice_should_return_null_when_item_is_null() {
        PersonalItem personalItem = new PersonalItem();
        personalItem.setItem(null);
        assertNull(personalItem.getMonthMedianPrice());
    }

    @Test
    public void getProposedPaymentPrice_should_return_proposedPaymentPrice() {
        PersonalItem personalItem = new PersonalItem();
        Trade ubiTrade = new Trade();
        ubiTrade.setProposedPaymentPrice(100);
        personalItem.setExistingTrade(ubiTrade);
        assertEquals(100, personalItem.getProposedPaymentPrice());
    }

    @Test
    public void getProposedPaymentPrice_should_return_null_when_existingTrade_is_null() {
        PersonalItem personalItem = new PersonalItem();
        personalItem.setExistingTrade(null);
        assertNull(personalItem.getProposedPaymentPrice());
    }

    @Test
    public void getTradeId_should_return_tradeId() {
        PersonalItem personalItem = new PersonalItem();
        Trade ubiTrade = new Trade();
        ubiTrade.setTradeId("tradeId");
        personalItem.setExistingTrade(ubiTrade);
        assertEquals("tradeId", personalItem.getTradeId());
    }

    @Test
    public void getTradeId_should_return_null_when_existingTrade_is_null() {
        PersonalItem personalItem = new PersonalItem();
        personalItem.setExistingTrade(null);
        assertNull(personalItem.getTradeId());
    }

    @Test
    public void hashCode_should_return_same_hashCode_for_same_item_and_tradeOperationType() {
        Item item = new Item();
        item.setItemId("itemId");
        PersonalItem personalItem1 = new PersonalItem();
        personalItem1.setItem(item);
        personalItem1.setTradeOperationType(TradeOperationType.BUY);
        personalItem1.setSellBoundaryPrice(100);
        personalItem1.setBuyBoundaryPrice(200);
        personalItem1.setMinMedianPriceDifference(300);
        personalItem1.setMinMedianPriceDifferencePercent(400);
        personalItem1.setPriorityMultiplier(500);
        personalItem1.setIsOwned(true);
        personalItem1.setTradeAlreadyExists(true);
        personalItem1.setExistingTrade(new Trade());

        PersonalItem personalItem2 = new PersonalItem();
        personalItem2.setItem(item);
        personalItem2.setTradeOperationType(TradeOperationType.BUY);
        personalItem2.setSellBoundaryPrice(1000);
        personalItem2.setBuyBoundaryPrice(2000);
        personalItem2.setMinMedianPriceDifference(3000);
        personalItem2.setMinMedianPriceDifferencePercent(4000);
        personalItem2.setPriorityMultiplier(5000);
        personalItem2.setIsOwned(false);
        personalItem2.setTradeAlreadyExists(false);
        personalItem2.setExistingTrade(null);

        assertEquals(personalItem1.hashCode(), personalItem2.hashCode());
    }

    @Test
    public void equals_should_return_true_for_equal_objects() {
        Item item = Mockito.mock(Item.class);
        Trade ubiTrade1 = Mockito.mock(Trade.class);
        Trade ubiTrade2 = Mockito.mock(Trade.class);

        PersonalItem personalItem1 = new PersonalItem();
        personalItem1.setItem(item);
        personalItem1.setTradeOperationType(TradeOperationType.BUY);
        personalItem1.setSellBoundaryPrice(100);
        personalItem1.setBuyBoundaryPrice(200);
        personalItem1.setMinMedianPriceDifference(300);
        personalItem1.setMinMedianPriceDifferencePercent(400);
        personalItem1.setPriorityMultiplier(500);
        personalItem1.setIsOwned(true);
        personalItem1.setTradeAlreadyExists(true);
        personalItem1.setExistingTrade(ubiTrade1);

        PersonalItem personalItem2 = new PersonalItem();
        personalItem2.setItem(item);
        personalItem2.setTradeOperationType(TradeOperationType.BUY);
        personalItem2.setSellBoundaryPrice(100);
        personalItem2.setBuyBoundaryPrice(200);
        personalItem2.setMinMedianPriceDifference(300);
        personalItem2.setMinMedianPriceDifferencePercent(400);
        personalItem2.setPriorityMultiplier(5000);
        personalItem2.setIsOwned(false);
        personalItem2.setTradeAlreadyExists(false);
        personalItem2.setExistingTrade(ubiTrade2);

        assertEquals(personalItem1, personalItem2);
    }

    @Test
    public void equals_should_return_false_for_different_objects() {
        Item item = Mockito.mock(Item.class);
        Trade ubiTrade = Mockito.mock(Trade.class);

        when(item.isFullyEquals(any())).thenReturn(true);
        when(ubiTrade.isFullyEqual(any())).thenReturn(true);

        PersonalItem personalItem1 = new PersonalItem();
        personalItem1.setItem(item);
        personalItem1.setTradeOperationType(TradeOperationType.BUY);
        personalItem1.setSellBoundaryPrice(100);
        personalItem1.setBuyBoundaryPrice(200);
        personalItem1.setMinMedianPriceDifference(300);
        personalItem1.setMinMedianPriceDifferencePercent(400);
        personalItem1.setPriorityMultiplier(500);
        personalItem1.setIsOwned(true);
        personalItem1.setTradeAlreadyExists(true);
        personalItem1.setExistingTrade(ubiTrade);

        PersonalItem personalItem2 = new PersonalItem();
        personalItem2.setItem(item);
        personalItem2.setTradeOperationType(TradeOperationType.BUY);
        personalItem2.setSellBoundaryPrice(100);
        personalItem2.setBuyBoundaryPrice(200);
        personalItem2.setMinMedianPriceDifference(300);
        personalItem2.setMinMedianPriceDifferencePercent(400);
        personalItem2.setPriorityMultiplier(500);
        personalItem2.setIsOwned(true);
        personalItem2.setTradeAlreadyExists(true);
        personalItem2.setExistingTrade(ubiTrade);

        when(item.isFullyEquals(any())).thenReturn(false);
        assertFalse(personalItem1.isFullyEqual(personalItem2));
        when(item.isFullyEquals(any())).thenReturn(true);
        personalItem1.setTradeOperationType(TradeOperationType.SELL);
        assertFalse(personalItem1.isFullyEqual(personalItem2));
        personalItem1.setTradeOperationType(TradeOperationType.BUY);
        personalItem1.setSellBoundaryPrice(1000);
        assertFalse(personalItem1.isFullyEqual(personalItem2));
        personalItem1.setSellBoundaryPrice(100);
        personalItem1.setBuyBoundaryPrice(2000);
        assertFalse(personalItem1.isFullyEqual(personalItem2));
        personalItem1.setBuyBoundaryPrice(200);
        personalItem1.setMinMedianPriceDifference(3000);
        assertFalse(personalItem1.isFullyEqual(personalItem2));
        personalItem1.setMinMedianPriceDifference(300);
        personalItem1.setMinMedianPriceDifferencePercent(4000);
        assertFalse(personalItem1.isFullyEqual(personalItem2));
    }

    @Test
    public void equals_should_return_false_for_null() {
        Item item = new Item();
        item.setItemId("itemId");

        PersonalItem personalItem1 = new PersonalItem();
        personalItem1.setItem(item);
        personalItem1.setTradeOperationType(TradeOperationType.BUY);
        personalItem1.setSellBoundaryPrice(100);
        personalItem1.setBuyBoundaryPrice(200);
        personalItem1.setMinMedianPriceDifference(300);
        personalItem1.setMinMedianPriceDifferencePercent(400);
        personalItem1.setPriorityMultiplier(500);
        personalItem1.setIsOwned(true);
        personalItem1.setTradeAlreadyExists(true);
        personalItem1.setExistingTrade(new Trade());

        assertFalse(personalItem1.equals(null));
    }

    @Test
    public void isFullyEqual_should_return_true_for_equal_objects() {
        Item item = Mockito.mock(Item.class);
        Trade ubiTrade = Mockito.mock(Trade.class);

        when(item.isFullyEquals(any())).thenReturn(true);
        when(ubiTrade.isFullyEqual(any())).thenReturn(true);

        PersonalItem personalItem1 = new PersonalItem();
        personalItem1.setItem(item);
        personalItem1.setTradeOperationType(TradeOperationType.BUY);
        personalItem1.setSellBoundaryPrice(100);
        personalItem1.setBuyBoundaryPrice(200);
        personalItem1.setMinMedianPriceDifference(300);
        personalItem1.setMinMedianPriceDifferencePercent(400);
        personalItem1.setPriorityMultiplier(500);
        personalItem1.setIsOwned(true);
        personalItem1.setTradeAlreadyExists(true);
        personalItem1.setExistingTrade(ubiTrade);

        PersonalItem personalItem2 = new PersonalItem();
        personalItem2.setItem(item);
        personalItem2.setTradeOperationType(TradeOperationType.BUY);
        personalItem2.setSellBoundaryPrice(100);
        personalItem2.setBuyBoundaryPrice(200);
        personalItem2.setMinMedianPriceDifference(300);
        personalItem2.setMinMedianPriceDifferencePercent(400);
        personalItem2.setPriorityMultiplier(500);
        personalItem2.setIsOwned(true);
        personalItem2.setTradeAlreadyExists(true);
        personalItem2.setExistingTrade(ubiTrade);

        assertTrue(personalItem1.isFullyEqual(personalItem2));
    }

    @Test
    public void isFullyEqual_should_return_false_for_different_objects() {
        Item item = Mockito.mock(Item.class);
        Trade ubiTrade = Mockito.mock(Trade.class);

        when(item.isFullyEquals(any())).thenReturn(true);
        when(ubiTrade.isFullyEqual(any())).thenReturn(true);

        PersonalItem personalItem1 = new PersonalItem();
        personalItem1.setItem(item);
        personalItem1.setTradeOperationType(TradeOperationType.BUY);
        personalItem1.setSellBoundaryPrice(100);
        personalItem1.setBuyBoundaryPrice(200);
        personalItem1.setMinMedianPriceDifference(300);
        personalItem1.setMinMedianPriceDifferencePercent(400);
        personalItem1.setPriorityMultiplier(500);
        personalItem1.setIsOwned(true);
        personalItem1.setTradeAlreadyExists(true);
        personalItem1.setExistingTrade(ubiTrade);

        PersonalItem personalItem2 = new PersonalItem();
        personalItem2.setItem(item);
        personalItem2.setTradeOperationType(TradeOperationType.BUY);
        personalItem2.setSellBoundaryPrice(100);
        personalItem2.setBuyBoundaryPrice(200);
        personalItem2.setMinMedianPriceDifference(300);
        personalItem2.setMinMedianPriceDifferencePercent(400);
        personalItem2.setPriorityMultiplier(500);
        personalItem2.setIsOwned(true);
        personalItem2.setTradeAlreadyExists(true);
        personalItem2.setExistingTrade(ubiTrade);

        when(item.isFullyEquals(any())).thenReturn(false);
        assertFalse(personalItem1.isFullyEqual(personalItem2));
        when(item.isFullyEquals(any())).thenReturn(true);
        personalItem1.setTradeOperationType(TradeOperationType.SELL);
        assertFalse(personalItem1.isFullyEqual(personalItem2));
        personalItem1.setTradeOperationType(TradeOperationType.BUY);
        personalItem1.setSellBoundaryPrice(1000);
        assertFalse(personalItem1.isFullyEqual(personalItem2));
        personalItem1.setSellBoundaryPrice(100);
        personalItem1.setBuyBoundaryPrice(2000);
        assertFalse(personalItem1.isFullyEqual(personalItem2));
        personalItem1.setBuyBoundaryPrice(200);
        personalItem1.setMinMedianPriceDifference(3000);
        assertFalse(personalItem1.isFullyEqual(personalItem2));
        personalItem1.setMinMedianPriceDifference(300);
        personalItem1.setMinMedianPriceDifferencePercent(4000);
        assertFalse(personalItem1.isFullyEqual(personalItem2));
        personalItem1.setMinMedianPriceDifferencePercent(400);
        personalItem1.setPriorityMultiplier(5000);
        assertFalse(personalItem1.isFullyEqual(personalItem2));
        personalItem1.setPriorityMultiplier(500);
        personalItem1.setIsOwned(false);
        assertFalse(personalItem1.isFullyEqual(personalItem2));
        personalItem1.setIsOwned(true);
        personalItem1.setTradeAlreadyExists(false);
        assertFalse(personalItem1.isFullyEqual(personalItem2));
        personalItem1.setTradeAlreadyExists(true);
        when(ubiTrade.isFullyEqual(any())).thenReturn(false);
        assertFalse(personalItem1.isFullyEqual(personalItem2));
    }

    @Test
    public void isFullyEqual_should_return_false_for_null() {
        PersonalItem personalItem1 = new PersonalItem();

        assertFalse(personalItem1.isFullyEqual(null));
    }

}