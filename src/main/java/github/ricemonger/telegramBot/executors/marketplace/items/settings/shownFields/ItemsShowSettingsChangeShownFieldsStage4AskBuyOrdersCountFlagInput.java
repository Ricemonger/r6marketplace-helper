package github.ricemonger.telegramBot.executors.marketplace.items.settings.shownFields;

import github.ricemonger.telegramBot.client.Callbacks;
import github.ricemonger.telegramBot.executors.AbstractBotCommandExecutor;
import github.ricemonger.telegramBot.executors.InputState;

public class ItemsShowSettingsChangeShownFieldsStage4AskBuyOrdersCountFlagInput extends AbstractBotCommandExecutor {
    @Override
    protected void executeCommand() {
        processMiddleInput(InputState.ITEMS_SHOW_SETTING_SHOWN_FIELDS_BUY_ORDERS_COUNT);

        askYesOrNoFromInlineKeyboard("Do you want to show buy orders?", Callbacks.INPUT_CALLBACK_TRUE, Callbacks.INPUT_CALLBACK_FALSE);
    }
}
