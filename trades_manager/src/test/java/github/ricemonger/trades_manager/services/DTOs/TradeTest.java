package github.ricemonger.trades_manager.services.DTOs;

import github.ricemonger.utils.DTOs.common.Item;
import github.ricemonger.utils.DTOs.personal.UbiTrade;
import github.ricemonger.utils.enums.TradeCategory;
import github.ricemonger.utils.enums.TradeState;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TradeTest {

    @Test
    public void constructor_should_properly_set_fields() {
        Trade trade = new Trade(
                "tradeId",
                TradeState.Created,
                TradeCategory.Buy,
                LocalDateTime.of(2021, 1, 1, 0, 0),
                LocalDateTime.of(2022, 1, 1, 0, 0),
                new Item("itemId"),
                100,
                10,
                200,
                20,
                30,
                300L
        );

        UbiTrade ubiTrade = trade.getUbiTrade();

        assertEquals("tradeId", ubiTrade.getTradeId());
        assertEquals(TradeState.Created, ubiTrade.getState());
        assertEquals(TradeCategory.Buy, ubiTrade.getCategory());
        assertEquals(LocalDateTime.of(2021, 1, 1, 0, 0), ubiTrade.getExpiresAt());
        assertEquals(LocalDateTime.of(2022, 1, 1, 0, 0), ubiTrade.getLastModifiedAt());
        assertEquals("itemId", ubiTrade.getItemId());
        assertEquals(100, ubiTrade.getSuccessPaymentPrice());
        assertEquals(10, ubiTrade.getSuccessPaymentFee());
        assertEquals(200, ubiTrade.getProposedPaymentPrice());
        assertEquals(20, ubiTrade.getProposedPaymentFee());
        assertEquals(30, trade.getMinutesToTrade());
        assertEquals(300L, trade.getTradePriority());
    }

    @Test
    public void getTradeId_should_return_null_if_null_ubiTrade() {
        Trade trade = new Trade();
        assertNull(trade.getTradeId());
    }

    @Test
    public void getTradeId_should_return_ubiTrade_tradeId() {
        Trade trade = new Trade();
        trade.setUbiTrade(new UbiTrade());
        trade.getUbiTrade().setTradeId("tradeId");
        assertEquals("tradeId", trade.getTradeId());
    }

    @Test
    public void getState_should_return_null_if_null_ubiTrade() {
        Trade trade = new Trade();
        assertNull(trade.getState());
    }

    @Test
    public void getState_should_return_ubiTrade_state() {
        Trade trade = new Trade();
        trade.setUbiTrade(new UbiTrade());
        trade.getUbiTrade().setState(TradeState.Created);
        assertEquals(TradeState.Created, trade.getState());
    }

    @Test
    public void getCategory_should_return_null_if_null_ubiTrade() {
        Trade trade = new Trade();
        assertNull(trade.getCategory());
    }

    @Test
    public void getCategory_should_return_ubiTrade_category() {
        Trade trade = new Trade();
        trade.setUbiTrade(new UbiTrade());
        trade.getUbiTrade().setCategory(TradeCategory.Buy);
        assertEquals(TradeCategory.Buy, trade.getCategory());
    }

    @Test
    public void getExpiresAt_should_return_null_if_null_ubiTrade() {
        Trade trade = new Trade();
        assertNull(trade.getExpiresAt());
    }

    @Test
    public void getExpiresAt_should_return_ubiTrade_expiresAt() {
        Trade trade = new Trade();
        trade.setUbiTrade(new UbiTrade());
        trade.getUbiTrade().setExpiresAt(LocalDateTime.of(2021, 1, 1, 0, 0));
        assertEquals(LocalDateTime.of(2021, 1, 1, 0, 0), trade.getExpiresAt());
    }

    @Test
    public void getLastModifiedAt_should_return_null_if_null_ubiTrade() {
        Trade trade = new Trade();
        assertNull(trade.getLastModifiedAt());
    }

    @Test
    public void getLastModifiedAt_should_return_ubiTrade_lastModifiedAt() {
        Trade trade = new Trade();
        trade.setUbiTrade(new UbiTrade());
        trade.getUbiTrade().setLastModifiedAt(LocalDateTime.of(2021, 1, 1, 0, 0));
        assertEquals(LocalDateTime.of(2021, 1, 1, 0, 0), trade.getLastModifiedAt());
    }

    @Test
    public void getItem_should_return_null_if_null_ubiTrade() {
        Trade trade = new Trade();
        assertNull(trade.getItem());
    }

    @Test
    public void getItem_should_return_ubiTrade_item() {
        Trade trade = new Trade();
        trade.setUbiTrade(new UbiTrade());
        trade.getUbiTrade().setItem(new Item("itemId"));
        assertEquals("itemId", trade.getItem().getItemId());
    }

    @Test
    public void getSuccessPaymentPrice_should_return_null_if_null_ubiTrade() {
        Trade trade = new Trade();
        assertNull(trade.getSuccessPaymentPrice());
    }

    @Test
    public void getSuccessPaymentPrice_should_return_ubiTrade_successPaymentPrice() {
        Trade trade = new Trade();
        trade.setUbiTrade(new UbiTrade());
        trade.getUbiTrade().setSuccessPaymentPrice(100);
        assertEquals(100, trade.getSuccessPaymentPrice());
    }

    @Test
    public void getSuccessPaymentFee_should_return_null_if_null_ubiTrade() {
        Trade trade = new Trade();
        assertNull(trade.getSuccessPaymentFee());
    }

    @Test
    public void getSuccessPaymentFee_should_return_ubiTrade_successPaymentFee() {
        Trade trade = new Trade();
        trade.setUbiTrade(new UbiTrade());
        trade.getUbiTrade().setSuccessPaymentFee(10);
        assertEquals(10, trade.getSuccessPaymentFee());
    }

    @Test
    public void getProposedPaymentPrice_should_return_null_if_null_ubiTrade() {
        Trade trade = new Trade();
        assertNull(trade.getProposedPaymentPrice());
    }

    @Test
    public void getProposedPaymentPrice_should_return_ubiTrade_proposedPaymentPrice() {
        Trade trade = new Trade();
        trade.setUbiTrade(new UbiTrade());
        trade.getUbiTrade().setProposedPaymentPrice(200);
        assertEquals(200, trade.getProposedPaymentPrice());
    }

    @Test
    public void getProposedPaymentFee_should_return_null_if_null_ubiTrade() {
        Trade trade = new Trade();
        assertNull(trade.getProposedPaymentFee());
    }

    @Test
    public void getProposedPaymentFee_should_return_ubiTrade_proposedPaymentFee() {
        Trade trade = new Trade();
        trade.setUbiTrade(new UbiTrade());
        trade.getUbiTrade().setProposedPaymentFee(20);
        assertEquals(20, trade.getProposedPaymentFee());
    }

    @Test
    public void setProposedPaymentPrice_should_set_ubiTrade_proposedPaymentPrice() {
        Trade trade = new Trade();
        trade.setUbiTrade(new UbiTrade());
        trade.setProposedPaymentPrice(200);
        assertEquals(200, trade.getUbiTrade().getProposedPaymentPrice());
    }

    @Test
    public void setCategory_should_set_ubiTrade_category() {
        Trade trade = new Trade();
        trade.setUbiTrade(new UbiTrade());
        trade.setCategory(TradeCategory.Buy);
        assertEquals(TradeCategory.Buy, trade.getUbiTrade().getCategory());
    }

    @Test
    public void setTradeId_should_set_ubiTrade_tradeId() {
        Trade trade = new Trade();
        trade.setUbiTrade(new UbiTrade());
        trade.setTradeId("tradeId");
        assertEquals("tradeId", trade.getUbiTrade().getTradeId());
    }

    @Test
    public void setItem_should_set_ubiTrade_item() {
        Trade trade = new Trade();
        trade.setUbiTrade(new UbiTrade());
        trade.setItem(new Item("itemId"));
        assertEquals("itemId", trade.getUbiTrade().getItemId());
    }

    @Test
    public void getItemId_should_return_null_if_null_ubiTrade() {
        Trade trade = new Trade();
        assertNull(trade.getItemId());
    }

    @Test
    public void getItemId_should_return_ubiTrade_itemId() {
        Trade trade = new Trade();
        trade.setUbiTrade(new UbiTrade());
        trade.getUbiTrade().setItem(new Item("itemId"));
        assertEquals("itemId", trade.getItemId());
    }

    @Test
    public void getItemName_should_return_null_if_null_ubiTrade() {
        Trade trade = new Trade();
        assertNull(trade.getItemName());
    }

    @Test
    public void getItemName_should_return_ubiTrade_itemName() {
        Trade trade = new Trade();
        trade.setUbiTrade(new UbiTrade());
        trade.getUbiTrade().setItem(new Item());
        trade.getUbiTrade().getItem().setName("itemName");
        assertEquals("itemName", trade.getItemName());
    }

    @Test
    public void isFullyEqual_should_return_true_if_same() {
        Trade trade = new Trade();
        assertTrue(trade.isFullyEqual(trade));
    }

    @Test
    public void isFullyEqual_should_return_false_if_null() {
        Trade trade = new Trade();

        assertFalse(trade.isFullyEqual(null));
    }

    @Test
    public void isFullyEqual_should_return_true_if_equal_fields() {
        UbiTrade ubiTrade1 = new UbiTrade();
        ubiTrade1.setTradeId("tradeId1");
        ubiTrade1.setState(TradeState.Created);
        ubiTrade1.setCategory(TradeCategory.Buy);
        ubiTrade1.setExpiresAt(LocalDateTime.of(2021, 1, 1, 1, 1));
        ubiTrade1.setLastModifiedAt(LocalDateTime.of(2021, 2, 1, 1, 1));
        ubiTrade1.setItem(new Item("itemId1"));
        ubiTrade1.setSuccessPaymentPrice(100);
        ubiTrade1.setSuccessPaymentFee(10);
        ubiTrade1.setProposedPaymentPrice(75);
        ubiTrade1.setProposedPaymentFee(7);

        Trade trade1 = new Trade();
        trade1.setUbiTrade(ubiTrade1);
        trade1.setMinutesToTrade(15);
        trade1.setTradePriority(16L);

        UbiTrade ubiTrade2 = new UbiTrade();
        ubiTrade2.setTradeId("tradeId1");
        ubiTrade2.setState(TradeState.Created);
        ubiTrade2.setCategory(TradeCategory.Buy);
        ubiTrade2.setExpiresAt(LocalDateTime.of(2021, 1, 1, 1, 1));
        ubiTrade2.setLastModifiedAt(LocalDateTime.of(2021, 2, 1, 1, 1));
        ubiTrade2.setItem(new Item("itemId1"));
        ubiTrade2.setSuccessPaymentPrice(100);
        ubiTrade2.setSuccessPaymentFee(10);
        ubiTrade2.setProposedPaymentPrice(75);
        ubiTrade2.setProposedPaymentFee(7);

        Trade trade2 = new Trade();
        trade2.setUbiTrade(ubiTrade2);
        trade2.setMinutesToTrade(15);
        trade2.setTradePriority(16L);

        assertTrue(trade1.isFullyEqual(trade2));
    }

    @Test
    public void isFullyEqual_should_return_false_if_different_fields() {
        UbiTrade ubiTrade1 = new UbiTrade();
        ubiTrade1.setTradeId("tradeId1");
        ubiTrade1.setState(TradeState.Created);
        ubiTrade1.setCategory(TradeCategory.Buy);
        ubiTrade1.setExpiresAt(LocalDateTime.of(2021, 1, 1, 1, 1));
        ubiTrade1.setLastModifiedAt(LocalDateTime.of(2021, 2, 1, 1, 1));
        ubiTrade1.setItem(new Item("itemId1"));
        ubiTrade1.setSuccessPaymentPrice(100);
        ubiTrade1.setSuccessPaymentFee(10);
        ubiTrade1.setProposedPaymentPrice(75);
        ubiTrade1.setProposedPaymentFee(7);

        Trade trade1 = new Trade();
        trade1.setUbiTrade(ubiTrade1);
        trade1.setMinutesToTrade(15);
        trade1.setTradePriority(16L);

        UbiTrade ubiTrade2 = new UbiTrade();
        ubiTrade2.setTradeId("tradeId1");
        ubiTrade2.setState(TradeState.Created);
        ubiTrade2.setCategory(TradeCategory.Buy);
        ubiTrade2.setExpiresAt(LocalDateTime.of(2021, 1, 1, 1, 1));
        ubiTrade2.setLastModifiedAt(LocalDateTime.of(2021, 2, 1, 1, 1));
        ubiTrade2.setItem(new Item("itemId1"));
        ubiTrade2.setSuccessPaymentPrice(100);
        ubiTrade2.setSuccessPaymentFee(10);
        ubiTrade2.setProposedPaymentPrice(75);
        ubiTrade2.setProposedPaymentFee(7);

        Trade trade2 = new Trade();
        trade2.setUbiTrade(ubiTrade2);
        trade2.setMinutesToTrade(15);
        trade2.setTradePriority(16L);

        trade1.setTradeId("tradeId2");
        assertFalse(trade1.isFullyEqual(trade2));
        trade1.setTradeId("tradeId1");
        trade1.getUbiTrade().setState(TradeState.Failed);
        assertFalse(trade1.isFullyEqual(trade2));
        trade1.getUbiTrade().setState(TradeState.Created);
        trade1.getUbiTrade().setCategory(TradeCategory.Sell);
        assertFalse(trade1.isFullyEqual(trade2));
        trade1.getUbiTrade().setCategory(TradeCategory.Buy);
        trade1.getUbiTrade().setExpiresAt(LocalDateTime.of(2021, 1, 1, 1, 2));
        assertFalse(trade1.isFullyEqual(trade2));
        trade1.getUbiTrade().setExpiresAt(LocalDateTime.of(2021, 1, 1, 1, 1));
        trade1.getUbiTrade().setLastModifiedAt(LocalDateTime.of(2021, 2, 1, 1, 2));
        assertFalse(trade1.isFullyEqual(trade2));
        trade1.getUbiTrade().setLastModifiedAt(LocalDateTime.of(2021, 2, 1, 1, 1));
        trade1.getUbiTrade().setItem(new Item("itemId2"));
        assertFalse(trade1.isFullyEqual(trade2));
        trade1.getUbiTrade().setItem(new Item("itemId1"));
        trade1.getUbiTrade().setSuccessPaymentPrice(101);
        assertFalse(trade1.isFullyEqual(trade2));
        trade1.getUbiTrade().setSuccessPaymentPrice(100);
        trade1.getUbiTrade().setSuccessPaymentFee(11);
        assertFalse(trade1.isFullyEqual(trade2));
        trade1.getUbiTrade().setSuccessPaymentFee(10);
        trade1.getUbiTrade().setProposedPaymentPrice(76);
        assertFalse(trade1.isFullyEqual(trade2));
        trade1.getUbiTrade().setProposedPaymentPrice(75);
        trade1.getUbiTrade().setProposedPaymentFee(8);
        assertFalse(trade1.isFullyEqual(trade2));
        trade1.getUbiTrade().setProposedPaymentFee(7);
        trade1.setMinutesToTrade(16);
        assertFalse(trade1.isFullyEqual(trade2));
        trade1.setMinutesToTrade(15);
        trade1.setTradePriority(17L);
        assertFalse(trade1.isFullyEqual(trade2));
    }
}