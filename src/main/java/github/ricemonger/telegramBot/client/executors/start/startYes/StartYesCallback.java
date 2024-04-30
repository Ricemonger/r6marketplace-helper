package github.ricemonger.telegramBot.client.executors.start.startYes;

import github.ricemonger.telegramBot.client.Callbacks;
import github.ricemonger.telegramBot.client.executors.AbstractBotCommandExecutor;

public class StartYesCallback extends AbstractBotCommandExecutor {

    @Override
    protected void executeCommand() {
        if (isRegistered(updateInfo.getChatId())) {
            sendText("You are already registered");
        } else {
            registerUser(updateInfo.getChatId());
            askYesOrNoFromInlineKeyboard(
                    "You were successfully registered, would you like to add your first Ubisoft Credentials?",
                    Callbacks.ADD_UBISOFT_CREDENTIALS_YES,
                    Callbacks.SILENT_CANCEL);
        }
    }

    private void registerUser(Long chatId) {
        botService.registerUser(chatId);
    }
}
