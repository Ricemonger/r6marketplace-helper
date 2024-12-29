package github.ricemonger.trades_manager.postgres.entities.manageable_users;

import github.ricemonger.utils.enums.TradeCategory;
import github.ricemonger.utils.enums.TradeState;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TradeEntityTest {

    @Test
    public void isEqual_should_return_true_for_equal_id(){
        TradeEntity id1 = new TradeEntity();
        id1.setTradeId("tradeId");
        id1.setState(TradeState.Created);
        id1.setCategory(TradeCategory.Buy);
        id1.setExpiresAt(LocalDateTime.of(2021, 1, 1, 0, 0));
        id1.setLastModifiedAt(LocalDateTime.of(2022, 1, 1, 0, 0));
        id1.setSuccessPaymentPrice(100);
        id1.setSuccessPaymentFee(10);
        id1.setProposedPaymentPrice(200);
        id1.setProposedPaymentFee(20);
        id1.setMinutesToTrade(30);
        id1.setTradePriority(300L);

        TradeEntity id2 = new TradeEntity();
        id2.setTradeId("tradeId");
        id2.setState(TradeState.Failed);
        id2.setCategory(TradeCategory.Sell);
        id2.setExpiresAt(LocalDateTime.of(2023, 1, 1, 0, 0));
        id2.setLastModifiedAt(LocalDateTime.of(2024, 1, 1, 0, 0));
        id2.setSuccessPaymentPrice(300);
        id2.setSuccessPaymentFee(30);
        id2.setProposedPaymentPrice(400);
        id2.setProposedPaymentFee(40);
        id2.setMinutesToTrade(50);
        id2.setTradePriority(500L);

        assertTrue(id1.isEqual(id2));
    }

    @Test
    public void isEqual_should_return_false_for_different_id(){
        TradeEntity id1 = new TradeEntity();
        id1.setTradeId("tradeId1");
        TradeEntity id2 = new TradeEntity();

        id2.setTradeId("tradeId2");
        assertNotEquals(id1, id2);
    }
}