package github.ricemonger.telegramBot.executors.itemFilters.edit;

import github.ricemonger.telegramBot.InputState;
import github.ricemonger.telegramBot.executors.AbstractBotCommandExecutor;

public class FilterEditStage15AskMinLastSoldPriceInput extends AbstractBotCommandExecutor {
    @Override
    protected void executeCommand() {
        processMiddleInput(InputState.ITEM_FILTER_MIN_LAST_SOLD_PRICE);
        askFromInlineKeyboardOrSkip("Please enter minimum last sold price:", 1);
    }
}
