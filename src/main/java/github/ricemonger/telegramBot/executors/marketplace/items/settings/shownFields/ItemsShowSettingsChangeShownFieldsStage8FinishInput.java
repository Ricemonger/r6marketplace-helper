package github.ricemonger.telegramBot.executors.marketplace.items.settings.shownFields;

import github.ricemonger.telegramBot.executors.AbstractBotCommandExecutor;

public class ItemsShowSettingsChangeShownFieldsStage8FinishInput extends AbstractBotCommandExecutor {
    @Override
    protected void executeCommand() {
        processLastInput();

        botInnerService.setItemShowSettingsShownFieldsByUserInput(updateInfo.getChatId());

        sendText("Settings have been saved.");
    }
}
