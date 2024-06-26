package github.ricemonger.telegramBot.executors.marketplace.filters.edit;

import github.ricemonger.telegramBot.client.CallbackButton;
import github.ricemonger.telegramBot.Callbacks;
import github.ricemonger.telegramBot.executors.AbstractBotCommandExecutor;
import github.ricemonger.telegramBot.InputState;

public class FilterEditStage2AskFilterTypeInput extends AbstractBotCommandExecutor {
    @Override
    protected void executeCommand() {
        processMiddleInput(InputState.FILTER_TYPE);
        CallbackButton allow = new CallbackButton("Allow", Callbacks.FILTER_TYPE_ALLOW);
        CallbackButton deny = new CallbackButton("Deny", Callbacks.FILTER_TYPE_DENY);
        askFromInlineKeyboard("Please choose filter type:",2,allow,deny);
    }
}
