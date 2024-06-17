package github.ricemonger.telegramBot.executors.start.startYes;

import github.ricemonger.telegramBot.Callbacks;
import github.ricemonger.telegramBot.executors.AbstractBotCommandExecutor;

public class StartYesCallback extends AbstractBotCommandExecutor {

    @Override
    protected void executeCommand() {
        if (isRegistered()) {
            sendText("You are already registered");
        } else {
            registerUser(updateInfo.getChatId());
            askYesOrNoFromInlineKeyboard(
                    "You were successfully registered, would you like to add your first Ubisoft Credentials?",
                    Callbacks.CREDENTIALS_ADD,
                    Callbacks.SILENT_CANCEL);
        }
    }

    private void registerUser(Long chatId) {
        botInnerService.registerUser(chatId);
    }
}
