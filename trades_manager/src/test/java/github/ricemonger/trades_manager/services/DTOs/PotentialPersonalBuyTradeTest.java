package github.ricemonger.trades_manager.services.DTOs;

import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.DTOs.common.PotentialTradeStats;
import github.ricemonger.utils.DTOs.personal.UbiTrade;
import github.ricemonger.utils.enums.TradeCategory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PotentialPersonalBuyTradeTest {

    @Test
    void getTradeCategory_should_return_buy() {
        PersonalItem personalItem = new PersonalItem();
        PotentialTradeStats potentialTradeStats = new PotentialTradeStats();
        PotentialPersonalBuyTrade potentialPersonalBuyTrade = new PotentialPersonalBuyTrade(personalItem, potentialTradeStats);
        assertEquals(TradeCategory.Buy, potentialPersonalBuyTrade.getTradeCategory());
    }

    @Test
    public void compareTo_should_compare_by_new_price_asc_if_itemId_priority_equal() {
        PersonalItem personalItem1 = new PersonalItem();
        personalItem1.setItem(new Item("itemId"));
        PotentialTradeStats potentialTradeStats1 = new PotentialTradeStats();
        potentialTradeStats1.setPrice(1);
        personalItem1.setPriorityMultiplier(1);
        potentialTradeStats1.setTradePriority(1L);
        PersonalItem personalItem2 = new PersonalItem();
        personalItem2.setItem(new Item("itemId"));
        PotentialTradeStats potentialTradeStats2 = new PotentialTradeStats();
        potentialTradeStats2.setPrice(5);
        personalItem2.setPriorityMultiplier(1);
        potentialTradeStats2.setTradePriority(1L);
        PotentialPersonalBuyTrade potentialTrade1 = new PotentialPersonalBuyTrade(personalItem1, potentialTradeStats1);
        PotentialPersonalBuyTrade potentialTrade2 = new PotentialPersonalBuyTrade(personalItem2, potentialTradeStats2);

        int result = potentialTrade1.compareTo(potentialTrade2);

        assertEquals(-1, result);
    }

    @Test
    public void compareTo_should_compare_by_item_Id_if_equal_priority() {
        PersonalItem personalItem1 = new PersonalItem();
        personalItem1.setItem(new Item("itemId1"));
        PersonalItem personalItem2 = new PersonalItem();
        personalItem2.setItem(new Item("itemId2"));
        PotentialTradeStats potentialTradeStats1 = new PotentialTradeStats();
        potentialTradeStats1.setTradePriority(1L);
        personalItem1.setPriorityMultiplier(1);
        PotentialTradeStats potentialTradeStats2 = new PotentialTradeStats();
        potentialTradeStats2.setTradePriority(1L);
        personalItem2.setPriorityMultiplier(1);

        PotentialPersonalBuyTrade potentialTrade1 = new PotentialPersonalBuyTrade(personalItem1, potentialTradeStats1);
        PotentialPersonalBuyTrade potentialTrade2 = new PotentialPersonalBuyTrade(personalItem2, potentialTradeStats2);

        int result = potentialTrade1.compareTo(potentialTrade2);

        assertEquals("itemId1".compareTo("itemId2"), result);
    }

    @Test
    public void compareTo_should_compare_desc_by_tradePriority_first_for_same_multiplier() {
        PersonalItem personalItem1 = new PersonalItem();
        personalItem1.setItem(new Item("itemId1"));
        PersonalItem personalItem2 = new PersonalItem();
        personalItem2.setItem(new Item("itemId1"));
        PotentialTradeStats potentialTradeStats1 = new PotentialTradeStats();
        potentialTradeStats1.setTradePriority(10L);
        personalItem1.setPriorityMultiplier(1);
        PotentialTradeStats potentialTradeStats2 = new PotentialTradeStats();
        potentialTradeStats2.setTradePriority(5L);
        personalItem2.setPriorityMultiplier(1);

        PotentialPersonalBuyTrade potentialTrade1 = new PotentialPersonalBuyTrade(personalItem1, potentialTradeStats1);
        PotentialPersonalBuyTrade potentialTrade2 = new PotentialPersonalBuyTrade(personalItem2, potentialTradeStats2);

        int result = potentialTrade1.compareTo(potentialTrade2);

        assertEquals(-1, result);
    }

    @Test
    public void compareTo_should_compare_desc_by_tradePriority_first_for_different_multiplier() {
        PersonalItem personalItem1 = new PersonalItem();
        personalItem1.setItem(new Item("itemId1"));
        PersonalItem personalItem2 = new PersonalItem();
        personalItem2.setItem(new Item("itemId1"));
        PotentialTradeStats potentialTradeStats1 = new PotentialTradeStats();
        potentialTradeStats1.setTradePriority(10L);
        personalItem1.setPriorityMultiplier(1);
        PotentialTradeStats potentialTradeStats2 = new PotentialTradeStats();
        potentialTradeStats2.setTradePriority(5L);
        personalItem2.setPriorityMultiplier(3);

        PotentialPersonalBuyTrade potentialTrade1 = new PotentialPersonalBuyTrade(personalItem1, potentialTradeStats1);
        PotentialPersonalBuyTrade potentialTrade2 = new PotentialPersonalBuyTrade(personalItem2, potentialTradeStats2);

        int result = potentialTrade1.compareTo(potentialTrade2);

        assertEquals(1, result);
    }

    @Test
    public void compareTo_should_compare_desc_by_tradePriority_first_for_negative() {
        PersonalItem personalItem1 = new PersonalItem();
        personalItem1.setItem(new Item("itemId1"));
        PersonalItem personalItem2 = new PersonalItem();
        personalItem2.setItem(new Item("itemId1"));
        PotentialTradeStats potentialTradeStats1 = new PotentialTradeStats();
        potentialTradeStats1.setTradePriority(-10L);
        personalItem1.setPriorityMultiplier(1);
        PotentialTradeStats potentialTradeStats2 = new PotentialTradeStats();
        potentialTradeStats2.setTradePriority(-5L);
        personalItem2.setPriorityMultiplier(1);

        PotentialPersonalBuyTrade potentialTrade1 = new PotentialPersonalBuyTrade(personalItem1, potentialTradeStats1);
        PotentialPersonalBuyTrade potentialTrade2 = new PotentialPersonalBuyTrade(personalItem2, potentialTradeStats2);

        int result = potentialTrade1.compareTo(potentialTrade2);

        assertEquals(1, result);
    }

    @Test
    public void compareTo_should_compare_desc_by_tradePriority_first_for_negative_diff_multiplier() {
        PersonalItem personalItem1 = new PersonalItem();
        personalItem1.setItem(new Item("itemId1"));
        PersonalItem personalItem2 = new PersonalItem();
        personalItem2.setItem(new Item("itemId1"));
        PotentialTradeStats potentialTradeStats1 = new PotentialTradeStats();
        potentialTradeStats1.setTradePriority(-10L);
        personalItem1.setPriorityMultiplier(3);
        PotentialTradeStats potentialTradeStats2 = new PotentialTradeStats();
        potentialTradeStats2.setTradePriority(-5L);
        personalItem2.setPriorityMultiplier(1);

        PotentialPersonalBuyTrade potentialTrade1 = new PotentialPersonalBuyTrade(personalItem1, potentialTradeStats1);
        PotentialPersonalBuyTrade potentialTrade2 = new PotentialPersonalBuyTrade(personalItem2, potentialTradeStats2);

        int result = potentialTrade1.compareTo(potentialTrade2);

        assertEquals(-1, result);
    }

    @Test
    public void getPriorityMultiplier_should_return_priorityMultiplier() {
        PersonalItem personalItem = new PersonalItem();
        personalItem.setPriorityMultiplier(1);
        PotentialPersonalBuyTrade PotentialPersonalBuyTrade = new PotentialPersonalBuyTrade(personalItem, null);

        assertEquals(1, PotentialPersonalBuyTrade.getPriorityMultiplier());
    }

    @Test
    public void getPriorityMultiplier_should_return_null_when_personalItem_is_null() {
        PotentialPersonalBuyTrade PotentialPersonalBuyTrade = new PotentialPersonalBuyTrade(null, null);

        Integer result = PotentialPersonalBuyTrade.getPriorityMultiplier();

        assertNull(result);
    }

    @Test
    public void getTradePriority_should_return_tradePriority() {
        PotentialTradeStats potentialTradeStats = new PotentialTradeStats();
        potentialTradeStats.setTradePriority(1L);
        PotentialPersonalBuyTrade PotentialPersonalBuyTrade = new PotentialPersonalBuyTrade(null, potentialTradeStats);

        assertEquals(1L, PotentialPersonalBuyTrade.getTradePriority());
    }

    @Test
    public void getTradePriority_should_return_null_when_potentialTradeStats_is_null() {
        PotentialPersonalBuyTrade PotentialPersonalBuyTrade = new PotentialPersonalBuyTrade(null, null);

        Long result = PotentialPersonalBuyTrade.getTradePriority();

        assertNull(result);
    }

    @Test
    public void tradeForItemAlreadyExists_should_return_tradeAlreadyExists() {
        PersonalItem personalItem = new PersonalItem();
        personalItem.setTradeAlreadyExists(true);
        PotentialPersonalBuyTrade PotentialPersonalBuyTrade = new PotentialPersonalBuyTrade(personalItem, null);

        assertTrue(PotentialPersonalBuyTrade.tradeForItemAlreadyExists());
    }

    @Test
    public void tradeForItemAlreadyExists_should_return_null_when_personalItem_is_null() {
        PotentialPersonalBuyTrade PotentialPersonalBuyTrade = new PotentialPersonalBuyTrade(null, null);

        Boolean result = PotentialPersonalBuyTrade.tradeForItemAlreadyExists();

        assertNull(result);
    }

    @Test
    public void getItemId_should_return_itemId() {
        PersonalItem personalItem = new PersonalItem();
        personalItem.setItem(new Item("itemId"));
        PotentialPersonalBuyTrade PotentialPersonalBuyTrade = new PotentialPersonalBuyTrade(personalItem, null);

        assertEquals("itemId", PotentialPersonalBuyTrade.getItemId());
    }

    @Test
    public void getItemId_should_return_null_when_personalItem_is_null() {
        PotentialPersonalBuyTrade PotentialPersonalBuyTrade = new PotentialPersonalBuyTrade(null, null);

        String result = PotentialPersonalBuyTrade.getItemId();

        assertNull(result);
    }

    @Test
    public void getNewPrice_should_return_price() {
        PotentialTradeStats potentialTradeStats = new PotentialTradeStats();
        potentialTradeStats.setPrice(1);
        PotentialPersonalBuyTrade PotentialPersonalBuyTrade = new PotentialPersonalBuyTrade(null, potentialTradeStats);

        assertEquals(1, PotentialPersonalBuyTrade.getNewPrice());
    }

    @Test
    public void getNewPrice_should_return_null_when_potentialTradeStats_is_null() {
        PotentialPersonalBuyTrade PotentialPersonalBuyTrade = new PotentialPersonalBuyTrade(null, null);

        Integer result = PotentialPersonalBuyTrade.getNewPrice();

        assertNull(result);
    }

    @Test
    public void getOldPrice_should_return_proposedPaymentPrice() {
        PersonalItem personalItem = new PersonalItem();
        Trade ubiTrade = new Trade();
        ubiTrade.setUbiTrade(new UbiTrade());
        ubiTrade.setProposedPaymentPrice(1);
        personalItem.setExistingTrade(ubiTrade);
        PotentialPersonalBuyTrade PotentialPersonalBuyTrade = new PotentialPersonalBuyTrade(personalItem, null);

        assertEquals(1, PotentialPersonalBuyTrade.getOldPrice());
    }

    @Test
    public void getOldPrice_should_return_null_when_personalItem_is_null() {
        PotentialPersonalBuyTrade PotentialPersonalBuyTrade = new PotentialPersonalBuyTrade(null, null);

        Integer result = PotentialPersonalBuyTrade.getOldPrice();

        assertNull(result);
    }

    @Test
    public void getItemName_should_return_name() {
        PersonalItem personalItem = new PersonalItem();
        Item item = new Item();
        item.setName("name");
        personalItem.setItem(item);
        PotentialPersonalBuyTrade PotentialPersonalBuyTrade = new PotentialPersonalBuyTrade(personalItem, null);

        assertEquals("name", PotentialPersonalBuyTrade.getItemName());
    }

    @Test
    public void getItemName_should_return_null_when_personalItem_is_null() {
        PotentialPersonalBuyTrade PotentialPersonalBuyTrade = new PotentialPersonalBuyTrade(null, null);
        String result = PotentialPersonalBuyTrade.getItemName();

        assertNull(result);
    }

    @Test
    public void getTradeId_should_return_tradeId() {
        PersonalItem personalItem = new PersonalItem();
        Trade ubiTrade = new Trade();
        ubiTrade.setUbiTrade(new UbiTrade());
        ubiTrade.setTradeId("tradeId");
        personalItem.setExistingTrade(ubiTrade);
        PotentialPersonalBuyTrade PotentialPersonalBuyTrade = new PotentialPersonalBuyTrade(personalItem, null);

        assertEquals("tradeId", PotentialPersonalBuyTrade.getTradeId());
    }

    @Test
    public void getTradeId_should_return_null_when_personalItem_is_null() {
        PotentialPersonalBuyTrade PotentialPersonalBuyTrade = new PotentialPersonalBuyTrade(null, null);

        String result = PotentialPersonalBuyTrade.getTradeId();

        assertNull(result);
    }

}