package github.ricemonger.telegramBot.update_consumer.executors.tradeManagers.edit.oneItem.sell;

import github.ricemonger.telegramBot.update_consumer.executors.AbstractBotCommandExecutor;
import github.ricemonger.utils.enums.InputState;
import github.ricemonger.utils.exceptions.client.ItemDoesntExistException;

public class TradeByItemIdManagerSellEditStage2AskBoundaryPriceInput extends AbstractBotCommandExecutor {
    @Override
    protected void executeCommand() {
        try {
            sendText("Chosen item is:\n" + botInnerService.getItemByUserInputItemId(updateInfo.getChatId()));
            processMiddleInput(InputState.TRADE_BY_ITEM_ID_MANAGER_BOUNDARY_SELL_PRICE);
            sendText("Please enter boundary price to sell item(If value is invalid, (current min sell price - 1) will be used):");
        } catch (ItemDoesntExistException e) {
            sendText("Item not found. Please enter correct item id.");
        }
    }
}
