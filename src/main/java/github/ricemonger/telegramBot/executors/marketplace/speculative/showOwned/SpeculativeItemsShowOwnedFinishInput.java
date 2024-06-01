package github.ricemonger.telegramBot.executors.marketplace.speculative.showOwned;

import github.ricemonger.telegramBot.executors.AbstractBotCommandExecutor;
import github.ricemonger.telegramBot.executors.InputState;

public class SpeculativeItemsShowOwnedFinishInput extends AbstractBotCommandExecutor {
    @Override
    protected void executeCommand() {

        processLastInput(updateInfo, "Email was provided, getting items...");

        String email = botInnerService.getUserInputByState(updateInfo.getChatId(), InputState.CREDENTIALS_FULL_OR_EMAIL);

        botInnerService.clearUserInputs(updateInfo.getChatId());

        botInnerService.sendDefaultSpeculativeItemsAsMessages(updateInfo.getChatId());
    }
}
