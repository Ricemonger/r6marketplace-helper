package github.ricemonger.trades_manager.services.DTOs;

import github.ricemonger.utils.enums.CentralTradeManagerCommandType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TradeManagerCommandTest {

    @Test
    public void compareTo_should_compareByCommandType_only() {
        TradeManagerCommand buyOrderCancel = new TradeManagerCommand(null, null, CentralTradeManagerCommandType.BUY_ORDER_CANCEL, null, null, null, null);
        TradeManagerCommand buyOrderUpdate = new TradeManagerCommand(null, null, CentralTradeManagerCommandType.BUY_ORDER_UPDATE, null, null, null, null);
        TradeManagerCommand buyOrderCreate = new TradeManagerCommand(null, null, CentralTradeManagerCommandType.BUY_ORDER_CREATE, null, null, null, null);
        TradeManagerCommand sellOrderCancel = new TradeManagerCommand(null, null, CentralTradeManagerCommandType.SELL_ORDER_CANCEL, null, null, null, null);
        TradeManagerCommand sellOrderUpdate = new TradeManagerCommand(null, null, CentralTradeManagerCommandType.SELL_ORDER_UPDATE, null, null, null, null);
        TradeManagerCommand sellOrderCreate = new TradeManagerCommand(null, null, CentralTradeManagerCommandType.SELL_ORDER_CREATE, null, null, null, null);

        Assertions.assertEquals(-1, buyOrderCancel.compareTo(buyOrderUpdate));
        Assertions.assertEquals(-1, buyOrderUpdate.compareTo(buyOrderCreate));
        Assertions.assertEquals(-1, buyOrderCreate.compareTo(sellOrderCancel));
        Assertions.assertEquals(-1, sellOrderCancel.compareTo(sellOrderUpdate));
        Assertions.assertEquals(-1, sellOrderUpdate.compareTo(sellOrderCreate));
    }
}