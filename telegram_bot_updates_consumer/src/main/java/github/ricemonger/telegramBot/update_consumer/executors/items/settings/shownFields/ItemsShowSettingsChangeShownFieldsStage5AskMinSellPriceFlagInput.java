package github.ricemonger.telegramBot.update_consumer.executors.items.settings.shownFields;

import github.ricemonger.telegramBot.Callbacks;
import github.ricemonger.telegramBot.update_consumer.executors.AbstractBotCommandExecutor;
import github.ricemonger.utils.enums.InputState;

public class ItemsShowSettingsChangeShownFieldsStage5AskMinSellPriceFlagInput extends AbstractBotCommandExecutor {
    @Override
    protected void executeCommand() {
        processMiddleInput(InputState.ITEMS_SHOW_SETTING_SHOWN_FIELDS_MIN_SELL_PRICE);

        askYesOrNoFromInlineKeyboard("Do you want to show min sell price?", Callbacks.INPUT_CALLBACK_TRUE, Callbacks.INPUT_CALLBACK_FALSE);
    }
}
