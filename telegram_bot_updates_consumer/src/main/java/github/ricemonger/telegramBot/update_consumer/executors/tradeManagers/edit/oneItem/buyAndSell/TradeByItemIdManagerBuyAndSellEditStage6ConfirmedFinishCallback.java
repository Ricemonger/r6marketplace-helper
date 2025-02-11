package github.ricemonger.telegramBot.update_consumer.executors.tradeManagers.edit.oneItem.buyAndSell;

import github.ricemonger.telegramBot.update_consumer.executors.AbstractBotCommandExecutor;
import github.ricemonger.utils.enums.TradeOperationType;

public class TradeByItemIdManagerBuyAndSellEditStage6ConfirmedFinishCallback extends AbstractBotCommandExecutor {
    @Override
    protected void executeCommand() {
        botInnerService.saveUserTradeByItemIdManagerByUserInput(updateInfo.getChatId(), TradeOperationType.BUY_AND_SELL);

        sendText("Trade saved successfully.");
    }
}
