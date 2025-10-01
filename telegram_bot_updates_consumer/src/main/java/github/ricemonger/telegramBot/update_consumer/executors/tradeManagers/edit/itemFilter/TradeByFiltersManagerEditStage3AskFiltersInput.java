package github.ricemonger.telegramBot.update_consumer.executors.tradeManagers.edit.itemFilter;

import github.ricemonger.telegramBot.update_consumer.executors.AbstractBotCommandExecutor;
import github.ricemonger.utils.enums.InputState;

public class TradeByFiltersManagerEditStage3AskFiltersInput extends AbstractBotCommandExecutor {
    @Override
    protected void executeCommand() {
        processMiddleInput(InputState.TRADE_BY_FILTERS_MANAGER_FILTERS_NAMES);

        sendText("List of available filters:\n"
                 + botInnerService.getAllUserItemFiltersNames(updateInfo.getChatId())
                 + "\nPlease enter names of item filters, you want to apply to the trade, divided by \",\" or \"|\":");
    }
}
