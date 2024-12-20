package github.ricemonger.utils.DTOs;

import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.DTOs.personal.PersonalItem;
import github.ricemonger.utils.DTOs.personal.UbiTrade;
import github.ricemonger.utils.enums.ItemRarity;
import github.ricemonger.utils.enums.TradeOperationType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

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
        UbiTrade ubiTrade = new UbiTrade();
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
        UbiTrade ubiTrade = new UbiTrade();
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
        personalItem1.setExistingTrade(new UbiTrade());

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
    public void equals_should_return_true_for_same_item_and_tradeOperationType() {
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
        personalItem1.setExistingTrade(new UbiTrade());

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

        assertEquals(personalItem1, personalItem2);
    }

    @Test
    public void equals_should_return_false_for_different_itemId_or_tradeOperation_Type(){
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
        personalItem1.setExistingTrade(new UbiTrade());

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
        personalItem2.setExistingTrade(new UbiTrade());

        personalItem1.setItem(new Item("itemId2"));
        assertNotEquals(personalItem1, personalItem2);
        personalItem1.setItem(item);
        personalItem1.setTradeOperationType(TradeOperationType.SELL);
        assertNotEquals(personalItem1, personalItem2);
    }

    @Test
    public void equals_should_return_false_for_null(){
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
        personalItem1.setExistingTrade(new UbiTrade());

        assertFalse(personalItem1.equals(null));
    }
}