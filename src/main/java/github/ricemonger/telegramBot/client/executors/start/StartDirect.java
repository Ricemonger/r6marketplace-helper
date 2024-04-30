package github.ricemonger.telegramBot.client.executors.start;

import github.ricemonger.telegramBot.client.executors.AbstractBotCommandExecutor;

import static github.ricemonger.telegramBot.client.Callbacks.*;

public class StartDirect extends AbstractBotCommandExecutor {

    @Override
    protected void executeCommand() {
        if (isRegistered(updateInfo.getChatId())) {
            sendText("You are already registered");
        } else {
            askYesOrNoFromInlineKeyboard("Do you want to register?", START_YES, CANCEL);
        }
    }
}
