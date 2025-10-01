package github.ricemonger.telegramBot.update_consumer.executors.tradeManagers.edit.itemFilter;

import github.ricemonger.marketplace.services.DTOs.TradeByFiltersManager;
import github.ricemonger.telegramBot.Callbacks;
import github.ricemonger.telegramBot.update_consumer.executors.AbstractBotCommandExecutor;

public class TradeByFiltersManagerEditStage7AskConfirmationFinishInput extends AbstractBotCommandExecutor {
    @Override
    protected void executeCommand() {
        processLastInput();

        TradeByFiltersManager manager = botInnerService.generateTradeByFiltersManagerByUserInput(updateInfo.getChatId());

        String text = "Your trade manager is:\n\n" + manager.toHandsomeString() + "\n\n" +
                      "Would you like to save it?";

        askYesOrNoFromInlineKeyboard(text, Callbacks.TRADE_BY_FILTERS_MANAGER_EDIT_FINISH_CONFIRMED, Callbacks.CANCEL);
    }
}
