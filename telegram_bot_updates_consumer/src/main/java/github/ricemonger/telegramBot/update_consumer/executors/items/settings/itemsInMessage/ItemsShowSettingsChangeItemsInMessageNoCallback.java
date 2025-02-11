package github.ricemonger.telegramBot.update_consumer.executors.items.settings.itemsInMessage;

import github.ricemonger.telegramBot.update_consumer.executors.AbstractBotCommandExecutor;

public class ItemsShowSettingsChangeItemsInMessageNoCallback extends AbstractBotCommandExecutor {
    @Override
    protected void executeCommand() {
        botInnerService.setUserItemShowSettingsFewItemsInMessageFlag(updateInfo.getChatId(), false);
        sendText("One item will be shown per one message");
    }
}
