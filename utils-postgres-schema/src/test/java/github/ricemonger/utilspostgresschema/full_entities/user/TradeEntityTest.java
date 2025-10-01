package github.ricemonger.utilspostgresschema.full_entities.user;

import github.ricemonger.utils.enums.TradeCategory;
import github.ricemonger.utils.enums.TradeState;
import github.ricemonger.utilspostgresschema.full_entities.item.ItemEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TradeEntityTest {

    @Test
    public void hashCode_should_return_equal_hash_for_equal_ids() {
        TradeEntity tradeEntity1 = new TradeEntity();
        tradeEntity1.setTradeId("1");
        tradeEntity1.setState(TradeState.Created);
        tradeEntity1.setCategory(TradeCategory.Sell);
        tradeEntity1.setExpiresAt(LocalDateTime.now());
        tradeEntity1.setLastModifiedAt(LocalDateTime.now());
        tradeEntity1.setItem(new ItemEntity());
        tradeEntity1.getItem().setItemId("1");
        tradeEntity1.setSuccessPaymentPrice(1);
        tradeEntity1.setSuccessPaymentFee(1);
        tradeEntity1.setProposedPaymentPrice(1);
        tradeEntity1.setProposedPaymentFee(1);
        tradeEntity1.setMinutesToTrade(1);
        tradeEntity1.setTradePriority(1L);
        TradeEntity tradeEntity2 = new TradeEntity();
        tradeEntity2.setTradeId("1");

        assertEquals(tradeEntity1.hashCode(), tradeEntity2.hashCode());
    }

    @Test
    public void equals_should_return_true_for_same() {
        TradeEntity tradeEntity1 = new TradeEntity();
        tradeEntity1.setTradeId("1");

        assertEquals(tradeEntity1, tradeEntity1);
    }

    @Test
    public void equals_should_return_true_for_same_ids() {
        TradeEntity tradeEntity1 = new TradeEntity();
        tradeEntity1.setTradeId("1");
        tradeEntity1.setState(TradeState.Created);
        tradeEntity1.setCategory(TradeCategory.Sell);
        tradeEntity1.setExpiresAt(LocalDateTime.now());
        tradeEntity1.setLastModifiedAt(LocalDateTime.now());
        tradeEntity1.setItem(new ItemEntity());
        tradeEntity1.getItem().setItemId("1");
        tradeEntity1.setSuccessPaymentPrice(1);
        tradeEntity1.setSuccessPaymentFee(1);
        tradeEntity1.setProposedPaymentPrice(1);
        tradeEntity1.setProposedPaymentFee(1);
        tradeEntity1.setMinutesToTrade(1);
        tradeEntity1.setTradePriority(1L);
        TradeEntity tradeEntity2 = new TradeEntity();
        tradeEntity2.setTradeId("1");

        assertEquals(tradeEntity1, tradeEntity2);
    }

    @Test
    public void equals_should_return_false_for_different_ids() {
        TradeEntity tradeEntity1 = new TradeEntity();
        tradeEntity1.setTradeId("1");
        TradeEntity tradeEntity2 = new TradeEntity();
        tradeEntity2.setTradeId("2");

        assertNotEquals(tradeEntity1, tradeEntity2);
    }

    @Test
    public void equals_should_return_false_for_null() {
        TradeEntity tradeEntity1 = new TradeEntity();
        tradeEntity1.setTradeId("1");

        assertNotEquals(null, tradeEntity1);
    }

    @Test
    public void isFullyEqual_should_return_true_if_same() {
        TradeEntity tradeEntity1 = new TradeEntity();

        assertTrue(tradeEntity1.isFullyEqual(tradeEntity1));
    }

    @Test
    public void isFullyEqual_should_return_true_if_fully_equal() {
        TradeEntity tradeEntity1 = new TradeEntity();
        tradeEntity1.setTradeId("tradeId1");
        tradeEntity1.setState(TradeState.Created);
        tradeEntity1.setCategory(TradeCategory.Sell);
        tradeEntity1.setExpiresAt(LocalDateTime.of(2021, 1, 1, 1, 1));
        tradeEntity1.setLastModifiedAt(LocalDateTime.of(2021, 1, 2, 1, 1));
        tradeEntity1.setItem(new ItemEntity("itemId1"));
        tradeEntity1.setSuccessPaymentPrice(1);
        tradeEntity1.setSuccessPaymentFee(2);
        tradeEntity1.setProposedPaymentPrice(3);
        tradeEntity1.setProposedPaymentFee(4);
        tradeEntity1.setMinutesToTrade(5);
        tradeEntity1.setTradePriority(6L);

        TradeEntity tradeEntity2 = new TradeEntity();
        tradeEntity2.setTradeId("tradeId1");
        tradeEntity2.setState(TradeState.Created);
        tradeEntity2.setCategory(TradeCategory.Sell);
        tradeEntity2.setExpiresAt(LocalDateTime.of(2021, 1, 1, 1, 1));
        tradeEntity2.setLastModifiedAt(LocalDateTime.of(2021, 1, 2, 1, 1));
        tradeEntity2.setItem(new ItemEntity("itemId1"));
        tradeEntity2.setSuccessPaymentPrice(1);
        tradeEntity2.setSuccessPaymentFee(2);
        tradeEntity2.setProposedPaymentPrice(3);
        tradeEntity2.setProposedPaymentFee(4);
        tradeEntity2.setMinutesToTrade(5);
        tradeEntity2.setTradePriority(6L);


        assertTrue(tradeEntity1.isFullyEqual(tradeEntity2));
    }

    @Test
    public void isFullyEqual_should_return_false_if_null() {
        TradeEntity tradeEntity1 = new TradeEntity();

        assertFalse(tradeEntity1.isFullyEqual(null));
    }

    @Test
    public void isFullyEqual_should_return_false_if_not_fully_equal() {
        TradeEntity tradeEntity1 = new TradeEntity();
        tradeEntity1.setTradeId("tradeId1");
        tradeEntity1.setState(TradeState.Created);
        tradeEntity1.setCategory(TradeCategory.Sell);
        tradeEntity1.setExpiresAt(LocalDateTime.of(2021, 1, 1, 1, 1));
        tradeEntity1.setLastModifiedAt(LocalDateTime.of(2021, 1, 2, 1, 1));
        tradeEntity1.setItem(new ItemEntity("itemId1"));
        tradeEntity1.setSuccessPaymentPrice(1);
        tradeEntity1.setSuccessPaymentFee(2);
        tradeEntity1.setProposedPaymentPrice(3);
        tradeEntity1.setProposedPaymentFee(4);
        tradeEntity1.setMinutesToTrade(5);
        tradeEntity1.setTradePriority(6L);

        TradeEntity tradeEntity2 = new TradeEntity();
        tradeEntity2.setTradeId("tradeId1");
        tradeEntity2.setState(TradeState.Created);
        tradeEntity2.setCategory(TradeCategory.Sell);
        tradeEntity2.setExpiresAt(LocalDateTime.of(2021, 1, 1, 1, 1));
        tradeEntity2.setLastModifiedAt(LocalDateTime.of(2021, 1, 2, 1, 1));
        tradeEntity2.setItem(new ItemEntity("itemId1"));
        tradeEntity2.setSuccessPaymentPrice(1);
        tradeEntity2.setSuccessPaymentFee(2);
        tradeEntity2.setProposedPaymentPrice(3);
        tradeEntity2.setProposedPaymentFee(4);
        tradeEntity2.setMinutesToTrade(5);
        tradeEntity2.setTradePriority(6L);

        assertTrue(tradeEntity1.isFullyEqual(tradeEntity2));
        tradeEntity1.setTradeId("tradeId2");
        assertFalse(tradeEntity1.isFullyEqual(tradeEntity2));
        tradeEntity1.setTradeId("tradeId1");
        tradeEntity1.setState(TradeState.Failed);
        assertFalse(tradeEntity1.isFullyEqual(tradeEntity2));
        tradeEntity1.setState(TradeState.Created);
        tradeEntity1.setCategory(TradeCategory.Buy);
        assertFalse(tradeEntity1.isFullyEqual(tradeEntity2));
        tradeEntity1.setCategory(TradeCategory.Sell);
        tradeEntity1.setExpiresAt(LocalDateTime.of(2021, 1, 1, 1, 2));
        assertFalse(tradeEntity1.isFullyEqual(tradeEntity2));
        tradeEntity1.setExpiresAt(LocalDateTime.of(2021, 1, 1, 1, 1));
        tradeEntity1.setLastModifiedAt(LocalDateTime.of(2021, 1, 2, 1, 2));
        assertFalse(tradeEntity1.isFullyEqual(tradeEntity2));
        tradeEntity1.setLastModifiedAt(LocalDateTime.of(2021, 1, 2, 1, 1));
        tradeEntity1.setItem(new ItemEntity("itemId2"));
        assertFalse(tradeEntity1.isFullyEqual(tradeEntity2));
        tradeEntity1.setItem(new ItemEntity("itemId1"));
        tradeEntity1.setSuccessPaymentPrice(2);
        assertFalse(tradeEntity1.isFullyEqual(tradeEntity2));
        tradeEntity1.setSuccessPaymentPrice(1);
        tradeEntity1.setSuccessPaymentFee(3);
        assertFalse(tradeEntity1.isFullyEqual(tradeEntity2));
        tradeEntity1.setSuccessPaymentFee(2);
        tradeEntity1.setProposedPaymentPrice(4);
        assertFalse(tradeEntity1.isFullyEqual(tradeEntity2));
        tradeEntity1.setProposedPaymentPrice(3);
        tradeEntity1.setProposedPaymentFee(5);
        assertFalse(tradeEntity1.isFullyEqual(tradeEntity2));
        tradeEntity1.setProposedPaymentFee(4);
        tradeEntity1.setMinutesToTrade(6);
        assertFalse(tradeEntity1.isFullyEqual(tradeEntity2));
        tradeEntity1.setMinutesToTrade(5);
        tradeEntity1.setTradePriority(7L);
        assertFalse(tradeEntity1.isFullyEqual(tradeEntity2));
    }
}
