package github.ricemonger.telegramBot.executors.start.startYes;

import github.ricemonger.telegramBot.Callbacks;
import github.ricemonger.telegramBot.executors.AbstractBotCommandExecutor;

public class StartYesCallback extends AbstractBotCommandExecutor {

    @Override
    protected void executeCommand() {
        if (isRegistered()) {
            sendText("You are already registered");
        } else {
            botInnerService.registerUser(updateInfo.getChatId());
            askYesOrNoFromInlineKeyboard(
                    "You were successfully registered, would you like to add your first Ubisoft Credentials?",
                    Callbacks.UBI_ACCOUNT_ENTRY_LINK,
                    Callbacks.CANCEL_SILENT);
        }
    }
}
