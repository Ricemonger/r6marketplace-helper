package github.ricemonger.telegramBot.executors.marketplace.tradeManagers.createUpdate.oneItem.buyAndSell;

import github.ricemonger.telegramBot.InputState;
import github.ricemonger.telegramBot.executors.AbstractBotCommandExecutor;

public class TradesOneItemBuyAndSellEditStage3AskStartingSellPriceInput extends AbstractBotCommandExecutor {
    @Override
    protected void executeCommand() {
        processMiddleInput(InputState.TRADES_EDIT_ONE_ITEM_STARTING_SELL_PRICE);

        askFromInlineKeyboardOrSkip("Please enter starting price to sell item or skip to make it equal to boundary sell price:", 1);
    }
}
