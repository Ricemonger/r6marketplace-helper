package github.ricemonger.telegramBot.executors.marketplace.trades.createUpdate.oneItem.sell;

import github.ricemonger.telegramBot.InputState;
import github.ricemonger.telegramBot.executors.AbstractBotCommandExecutor;
import github.ricemonger.utils.exceptions.ItemNotFoundException;

public class TradesOneItemSellEditStage3AskStartingPriceInput extends AbstractBotCommandExecutor {
    @Override
    protected void executeCommand() {
        processMiddleInput(InputState.TRADES_EDIT_ONE_ITEM_STARTING_SELL_PRICE);

        try {
            sendText("Chosen item is:\n" + botInnerService.getItemByPlannedOneItemTradeEditUserInput(updateInfo.getChatId()));
        } catch (ItemNotFoundException e) {
            sendText("Item not found. Please enter correct item id.");
            cancel();
            return;
        }

        askFromInlineKeyboardOrSkip("Please enter starting price to sell item or skip to make it equal to boundary sell price:", 1);
    }
}
